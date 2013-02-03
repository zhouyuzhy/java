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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.util
 * File     : ResponseFuture.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-01-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.client.ResponseHandler;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;


/**
 *
 *  对象等待
 *  这个是个同步工具，可以让一个线程等待另一个线程的结果
 *  NOTE: 这个类存在潜在的提前通知的问题，并且如果需要等待多次回复的话，有可能会出现只能通知一次的问题，先暂时这样
 *
 * @author solosky <solosky772@qq.com> 
 */
public class ResponseFuture implements ResponseHandler
{
	/**
	 * 等待对象
	 */
	private SipcResponse response;
	
	/**
	 * 同步锁
	 */
	private Object lock;
	
	/**
	 * 是否有人再等等
	 */
	private boolean isWaiting;
	
	/**
	 * 是否超时
	 */
	private boolean isTimeout;
	
	/**
	 * 是否出现传输错误
	 */
	private boolean isError;
	
	/**
	 * 使用RequestFuture包装Request,方便同步处理回复结果
	 * @param request
	 * @return
	 */
	public static ResponseFuture wrap(SipcRequest request)
	{
		ResponseFuture future = new ResponseFuture();
		request.setResponseHandler(future);
		return future;
	}
	
	/**
	 * 默认构造函数
	 */
	public ResponseFuture()
	{
		this.response = null;
		this.isWaiting = false;
		this.isTimeout = false;
		this.lock  = new Object();
	}
	
	/**
	 * 等待回复
	 * 如果结果没有返回，就在此等待
	 * @return  等待结果对象
	 * @throws RequestTimeoutException  	如果请求超时抛出超时异常
	 * @throws InterruptedException 		如果等待被中断抛出中断异常
	 * @throws TransferException 			如果请求发送失败抛出传输异常
	 */
	public SipcResponse waitResponse() throws RequestTimeoutException, InterruptedException, TransferException
	{
		synchronized (lock) {
			this.isWaiting = true;
			this.response   = null;
            lock.wait();
            if(this.isTimeout) {
            	throw new RequestTimeoutException();
            }
            if(this.isError) {
            	throw new TransferException();
            }

	        this.isWaiting = false;
	        return this.response;
        }
	}
	
	/**
	 * 等待对象收到
	 * @param target 等待对象
	 */
	private void responseRecived(SipcResponse response)
	{
		synchronized (lock) {
	        this.response = response;
	        lock.notifyAll();
        }
	}
	
	/**
	 * 请求超时
	 * @param exception
	 */
	private void setResponseTimeout()
	{
		synchronized (lock) {
			this.isTimeout = true;
	        lock.notifyAll();
        }
	}
	
	
	/**
	 * 设置请求传输错误
	 */
	private void setResponseError()
	{
		synchronized (lock) {
	        this.isError = true;
	        lock.notifyAll();
        }
	}
	
	/**
	 * 判断是否有人在等待
	 * 这个函数并不阻塞
	 * @return
	 */
	public boolean isWaiting()
	{
		return this.isWaiting;
	}
	
	
	/**
	 * 返回回复，这个函数立即返回不必等待
	 * @return
	 */
	public SipcResponse getResponse()
	{
		synchronized (lock) {
	        return this.response;
        }
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.ResponseHandler#handle(net.solosky.maplefetion.sipc.SipcResponse)
     */
    @Override
    public void handle(SipcResponse response) throws FetionException
    {
    	this.responseRecived(response);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.ResponseHandler#timeout(net.solosky.maplefetion.sipc.SipcRequest)
     */
    @Override
    public void timeout(SipcRequest request)
    {
    	this.setResponseTimeout();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.ResponseHandler#error(net.solosky.maplefetion.sipc.SipcRequest)
     */
    @Override
    public void ioerror(SipcRequest request)
    {
    	this.setResponseError();
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.ResponseHandler#syserror(net.solosky.maplefetion.sipc.SipcRequest, java.lang.Throwable)
	 */
	@Override
	public void syserror(SipcRequest request, Throwable throwable)
	{
		this.setResponseError();
	}

}
