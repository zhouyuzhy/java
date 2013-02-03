/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : MapleFetion2
 * Package  : net.solosky.net.maplefetion.net
 * File     : TransferService.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TimerTask;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.chain.AbstractProcessor;
import net.solosky.maplefetion.client.ResponseHandler;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.sipc.SipcStatus;
import net.solosky.maplefetion.util.SliceSipcResponseHelper;
import net.solosky.maplefetion.util.TimeHelper;

import org.apache.log4j.Logger;

/**
 * 
 * 传输服务 提供发出包和接收包的管理，并处理超时异常
 * 
 * @author solosky <solosky772@qq.com>
 */
public class TransferService extends AbstractProcessor
{

	/**
	 * 已发送请求队列
	 */
	private Queue<SipcRequest> requestQueue;
	
	/**
	 * 分块传输的消息队列
	 */
	private Queue<SliceSipcResponseHelper> slicedResponseQueue;
	
	/**
	 * 飞信上下文
	 */
	private FetionContext context;
	
	/**
	 * 主要用操作定时任务
	 */
	private TimerTask sipcTimeoutCheckTask;
	
	/**
	 * 日志记录
	 */
	private static Logger logger = Logger.getLogger(TransferService.class);

	/**
	 * 默认构造函数
	 */
	public TransferService(FetionContext context)
	{
		this.context = context;
		this.requestQueue = new LinkedList<SipcRequest>();
		this.slicedResponseQueue = new LinkedList<SliceSipcResponseHelper>();
	}

	/**
	 * 处理接受的包 在已发送队列中查找相对应的发出包，然后放入接受包中
	 */
	@Override
	protected Object doProcessIncoming(Object o) throws FetionException
	{
		//-_-!!! , 好吧，我承认这里判断逻辑有点乱。。
		if (o instanceof SipcResponse) {
			SipcResponse response = (SipcResponse) o;
			//先检查这个回复是不是分块回复的一部分
			SliceSipcResponseHelper helper = this.findSliceResponseHelper(response);
			if(helper!=null){
				//如果是分块回复，就添加到分块回复列表中
				helper.addSliceSipcResponse(response);
				if(helper.isFullSipcResponseRecived()){
					synchronized (slicedResponseQueue) {
						slicedResponseQueue.remove(helper);
					}
					
					//构造新的回复
					SipcResponse some = helper.getFullSipcResponse();
					this.handleFullResponse(some);
					
					return some;	//返回新的完整的请求
				}else{
					return null;	//分块回复还没接收完，返回null停止处理链
				}
			}else if(response.getStatusCode()==SipcStatus.PARTIAL){	//可能是第一次收到的分块回复
				helper = new SliceSipcResponseHelper(response);
				synchronized (slicedResponseQueue) {
					slicedResponseQueue.add(helper);
				}
				return null;		//第一个分块回复，返回null停止处理链
			}else{	//不是分块回复的对象就直接进行下一步操作
				this.handleFullResponse(response);
				return response;
			}
		}else if(o instanceof SipcNotify){
			this.handleFullNotify((SipcNotify) o);
			return (SipcNotify) o;	
		}else{
			return null;	//未知类型，一般不会发生。。
		}
	}

	/**
	 * 处理发送的包 如果这个包需要回复，就把这个包加入到已发送的队列中，然后隔一段时间后检查是否超时
	 */
	@Override
	protected Object doProcessOutcoming(Object o) throws FetionException
	{
		if (o instanceof SipcRequest) {
			SipcRequest request = (SipcRequest) o;
			//判断需要回复次数大于0才放人队列中
			if(request.getNeedReplyTimes()>0) {
				synchronized(this.requestQueue) {
					this.requestQueue.add(request);
				}
			}
		}
		return super.doProcessOutcoming(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.solosky.net.maplefetion.chain.Processor#getProcessorName()
	 */
	@Override
	public String getProcessorName()
	{
		return TransferService.class.getName();
	}

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#stopProcessor()
     */
    @Override
    public void stopProcessor(FetionException ex) throws FetionException
    {
    	//停止超时检查定时任务
    	this.sipcTimeoutCheckTask.cancel();
    	this.context.getFetionTimer().clearCanceledTask();
    	//通知当前发送队列中的请求都超时
    	synchronized (requestQueue) {
    		Iterator<SipcRequest> it = this.requestQueue.iterator();
        	while(it.hasNext()) {
        		SipcRequest request = it.next();
        		ResponseHandler handler = request.getResponseHandler();
        		if(handler!=null) {
        			if(ex==null){
        				handler.timeout(request);
        			}else if(ex instanceof TransferException){
        				handler.ioerror(request);
        			}else if(ex instanceof SystemException){
        				handler.syserror(request, ex.getCause());
        			}else{
        				handler.timeout(request);
        			}
        		}
        	}
    	}
    }
    
    
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#startProcessor()
     */
    @Override
    public void startProcessor() throws FetionException
    {
    	this.sipcTimeoutCheckTask = new SipMessageTimeOutCheckTask();
    	int checkTimeoutInterval  = FetionConfig.getInteger("fetion.sip.check-alive-interval")*1000;
    	int checkTimeoutDelay     = 50*100;
	    this.context.getFetionTimer().scheduleTask(
	    						this.sipcTimeoutCheckTask, checkTimeoutDelay, checkTimeoutInterval);
    }
    

	/**
	 * 
	 * 内部类，实现了超时检查任务 简单的委托给队列管理器处理
	 * 
	 * @author solosky <solosky772@qq.com>
	 */
	public class SipMessageTimeOutCheckTask extends TimerTask
	{
		@Override
		public void run()
		{
			try {
				checkTimeOutRequest();
			} catch (FetionException e) {
				raiseException(e);
			}
		}
	}

	/**
	 * 在已发送队列中查找对应发送信令
	 * 
	 * @param response
	 * @return
	 */
	public SipcRequest findRequest(SipcResponse response)
	{
		synchronized(this.requestQueue) {
    		Iterator<SipcRequest> it = this.requestQueue.iterator();
    		SipcRequest request = null;
    		String resCallID = response.getHeader(SipcHeader.CALLID).getValue();
    		String resSequence = response.getHeader(SipcHeader.SEQUENCE).getValue();
    		String reqCallID = null;
    		String reqSequence = null;
    		while (it.hasNext()) {
    			request = it.next();
    			reqCallID = request.getHeader(SipcHeader.CALLID).getValue();
    			reqSequence = request.getHeader(SipcHeader.SEQUENCE).getValue();
    			//如果callID和sequence都相等的话就找到了指定的请求
    			if (resCallID.equals(reqCallID) && resSequence.equals(reqSequence)) {
    				return request;
    			}
    		}
    		return null;
		}
	}
	
	/**
	 * 查找这个回复是否是分块回复的一部分
	 * @param response
	 * @return
	 */
	private SliceSipcResponseHelper findSliceResponseHelper(SipcResponse response)
	{
		synchronized(this.slicedResponseQueue) {
			Iterator<SliceSipcResponseHelper> it = this.slicedResponseQueue.iterator();
    		int resCallID = response.getCallID();
    		String resSequence = response.getSequence();
    		SliceSipcResponseHelper helper = null;
    		
    		while (it.hasNext()) {
    			helper = it.next();
    			if (helper.getCallid()==resCallID && helper.getSequence().equals(resSequence)) {
    				return helper;
    			}
    		}
    		return null;
		}
	}
	

	/**
	 * 检查超时的包，如果没有超过指定的重发次数，就重发 如果只要有一个包超出了重发的次数，就抛出超时异常
	 * 
	 * @throws FetionException
	 */
	private void checkTimeOutRequest() throws FetionException
	{
		int curTime     = TimeHelper.nowTimeUnixStamp();
		int maxTryTimes = FetionConfig.getInteger("fetion.sip.default-retry-times");
		int aliveTime   = FetionConfig.getInteger("fetion.sip.default-alive-time");

		//这里需要同步了。。 
		synchronized (this.requestQueue) {
			
			//感觉JAVA集合中的队列怪怪的，特别是命名，peek poll如果不看注释根本不知道是什么含义，接口不明确啊，能望文生义那种接口最好
			//比如getFrist() getLast() addFirst() addLast() peekFirst() peekLast()...
			//更无语的是，如果队列为空调用peek()居然会抛异常，因为这个原因不得不在遍历队列的时候判断下队列的大小..
			//所以这里采用的是集合的遍历方式，更加简洁，代码也更加优美点..
			
			ArrayList<SipcRequest> timeoutList = new ArrayList<SipcRequest>();		//超时的包的列表
			Iterator<SipcRequest> it = this.requestQueue.iterator();
			
			//首先把超时的包找出来
			while(it.hasNext()){
				SipcRequest request = it.next(); 
				if(request.getAliveTime()<curTime){				//超过了存活期，
					//因为这里如果判断超时并重发，这个包会重新添加到队列中，当前迭代的列表会增加元素，迭代器就会抛出更改异常
					//所以这里重新建立了一个超时列表，并把超时的包放入列表中，在当前迭代结束后才处理超时包
					it.remove();							//先从队列中移除
					timeoutList.add(request);				//添加到重发包列表
				}else{
					//未超过存活期，啥也不干
				}
			}
			
			//遍历超时包列表，并根据条件判断是否重发或者通知超时
			it = timeoutList.iterator();
			while(it.hasNext()){
				SipcRequest request = it.next();
				if(request.getRetryTimes()<maxTryTimes){		//重发次数未超过指定的次数，重新发送这个包
					logger.debug("Request was timeout, now sending it again... Request="+request);
					request.incRetryTimes();					//递增重试次数
					request.setAliveTime(curTime+aliveTime);	//更新存活时间
					this.processOutcoming(request);				// 重新发送这个包
				}else{											//这个包已经超过重发次数，通知超时
					logger.warn("A request was sent three times, handle this timeout exception...Request="+request);
					this.handleRequestTimeout(request);
				}
			
			}
		}
	}

	/**
	 * 处理包超时异常
	 * 
	 * @param timeoutMessage
	 * @throws FetionException 
	 */
	private void handleRequestTimeout(SipcRequest request) throws FetionException
	{
		//发出包设置了超时处理器，就调用超时处理器，否则抛出超时异常，结束整个程序
		if (request.getResponseHandler() != null) {
			request.getResponseHandler().timeout(request);
		}else {
			logger.warn("Request already was timeout, but there wasn't a timeout handler, ignore it. Request="+request);
		}
	}
	
	/**
	 * 处理收到的完整回复，主要完成查找对应的请求包并注入到回复中
	 * @param response
	 */
	private boolean handleFullResponse(SipcResponse response)
	{
		// 如果是回复的话，查找对应的请求，并通知回复等待对象
		SipcRequest request = this.findRequest(response);
		response.setRequest(request);
		
		//找到了对应的请求，然后判断是否回复已经到了指定的回复次数，如果到了就从队列中移除，如果没有，就不移除
		if(request!=null) {
			request.incReplyTimes();
			if(request.getNeedReplyTimes()==request.getReplyTimes()) {
				synchronized(this.requestQueue) {
					this.requestQueue.remove(request);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * 处理收到完整通知
	 * @param notify
	 */
	private boolean handleFullNotify(SipcNotify notify)
	{
		//目前什么也不做
		return true;
	}

}
