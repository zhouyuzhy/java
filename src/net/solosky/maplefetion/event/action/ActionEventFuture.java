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
 * Package  : net.solosky.maplefetion.event.action
 * File     : ActionEventFuture.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.TransferException;


/**
 *
 * 异步等待操作结果
 * 可以使用这个类来实现同步操作
 *
 * @author solosky <solosky772@qq.com>
 */
public class ActionEventFuture implements ActionEventListener
{
	//锁
	private Object lock;
	//事件结果
	private ActionEvent event;
	//是否已经通知
	private boolean isNotifyed;
	
	/**
	 * 构造函数
	 */
	public ActionEventFuture()
	{
		this.lock = new Object();
		this.clear();
	}
	
	/**
	 * 等待操作结果<br />
	 * 事实上这个操作不会一直等待，因为传输层如果在请求发出的后的指定时间内没有收到回复，<br />
	 * 并且重发过一定次数后仍没有收到回复，就会抛出请求超时异常<br />
	 * 为了防止出现无限期等待，这里还是设置了一个默认的超时值，为SIPC信令包存活时间*SIPC默认重发次数<br />
	 * 建议使用这个方法等待操作结果
	 * 
	 * @throws InterruptedException 	如果等待过程被中断就抛出中断异常
	 * @throws TransferException    	如果等待过程中出现网络异常就抛出
	 * @throws SystemException			如果处理结果的过程中出现未知的异常抛出
	 * @throws ReqeustTimeoutExcetion 	如果请求超时会跑出请求超时异常
	 */
	public ActionEvent waitActionEventWithException() throws RequestTimeoutException, InterruptedException, TransferException, SystemException
	{
		long maxWaitingTime = FetionConfig.getInteger("fetion.sip.default-alive-time")*FetionConfig.getInteger("fetion.sip.default-retry-times")*1000;
		return this.waitActionEventWithException(maxWaitingTime);
	}
	/**
	 * 在指定的时间内等待操作结果，
	 * 如果在指定的时间操作仍没有完成 或者操作已经发生了超时操作，就抛出RequestTimeoutException
	 * 
	 * @throws InterruptedException 	如果等待过程被中断就抛出中断异常
	 * @throws TransferException    	如果等待过程中出现网络异常就抛出
	 * @throws SystemException 			如果处理结果的过程中出现未知的异常抛出
	 * @throws ReqeustTimeoutExcetion 	如果请求超时会抛出请求超时异常
	 */
	public ActionEvent waitActionEventWithException(long timeout) throws RequestTimeoutException, InterruptedException, TransferException, SystemException
	{
		synchronized (lock) {
	        //判断是否已经提前通知过了
			if(this.isNotifyed)
				return this.event;
			
			//尝试等待
	        lock.wait(timeout);
	        
             //如果在等待的时间内没有通知，就抛出请求超时异常
             if(!this.isNotifyed || this.event==null ) {
            	 throw new RequestTimeoutException();
             }
             
             //如果当前的事件是超时事件，也抛出超时异常
             if(this.event.getEventType()==ActionEventType.TIMEOUT){
            	 throw new RequestTimeoutException();
             }
             
             //如果当前事件是传输错误事件，抛出传输异常
             if(this.event.getEventType()==ActionEventType.TRANSFER_ERROR){
            	 throw new TransferException();
             }
             
             //如果当前事件是系统错误事件，抛出系统错误异常
             if(this.event.getEventType()==ActionEventType.SYSTEM_ERROR){
            	 SystemErrorEvent evt = (SystemErrorEvent) this.event;
            	 throw new SystemException(evt.getCause());
             }
             
             //如果不是上面的任何一种，就直接返回事件
             return this.event;
        }
	}

	/**
	 * 在指定的时间内等待时间结果，不抛出任何异常，异常被封装为错误事件返回
	 * @param timeout	等待多长时间，如果超过这个时间就抛出超时异常
	 * @return	操作成功返回SuccessEvent<br />
	 * 			操作失败返回FailureEvent<br />
	 * 			超时返回         TimeoutEvent<br />
	 *			网络错误返回TransferErrorEvent<br />
	 *			其他错误返回SystemErrorEvent<br />
	 */
	public ActionEvent waitActionEventWithoutException(long timeout)
	{
		try {
			return this.waitActionEventWithException(timeout);
		} catch (RequestTimeoutException e) {
			return new TimeoutEvent();
		} catch (TransferException e) {
			return new TransferErrorEvent();
		} catch (SystemException e) {
			return new SystemErrorEvent(e);
		} catch (InterruptedException e) {
			return new SystemErrorEvent(e);
		}
	}
	
	/**
	 * 无限时间等待时间结果，不抛出任何异常，异常被封装为错误事件返回
	 * 这个方法不会永远等待，在数据包超时后会也会发生超时异常
	 * @return	操作成功返回SuccessEvent
	 * 			操作失败返回FailureEvent
	 * 			超时返回         TimeoutEvent
	 *			网络错误返回TransferErrorEvent
	 *			其他错误返回SystemErrorEvent
	 */
	public ActionEvent waitActionEventWithoutException()
	{
		long maxWaitingTime = FetionConfig.getInteger("fetion.sip.default-alive-time")*FetionConfig.getInteger("fetion.sip.default-retry-times")*1000;
		return this.waitActionEventWithoutException(maxWaitingTime);
	}
	
	/**
	 * 设置操作结果
	 * @param status
	 */
	public void setActionEvent(ActionEvent event)
	{
		synchronized (lock) {
	        this.isNotifyed = true;
	        this.event = event;
	        this.lock.notifyAll();
        }
	}
	
	/**
	 * 清除等待结果，便于下一次等待
	 */
	public void clear()
	{
		synchronized (lock) {
			this.event      = null;
	        this.isNotifyed = false;
        }
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.event.action.ActionEventListener#fireEevent(net.solosky.maplefetion.event.ActionEvent)
	 */
	@Override
	public void fireEevent(ActionEvent event)
	{
		this.setActionEvent(event);
	}
}
