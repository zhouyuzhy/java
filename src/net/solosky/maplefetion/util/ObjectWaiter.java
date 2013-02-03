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
 * File     : ObjectWaiter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-26
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;



/**
 *
 *  对象等待
 *  这个是个同步工具，可以让一个线程等待另一个线程的结果
 *
 * @author solosky <solosky772@qq.com> 
 */
public class ObjectWaiter<T>
{
	/**
	 * 等待对象
	 */
	private T target;
	
	/**
	 * 异常，如果发生的话
	 */
	private Exception exception;
	
	/**
	 * 同步锁
	 */
	private Object lock;
	
	/**
	 * 是否结果已经到达，这个标志变量是为了解决提前通知的问题
	 */
	private boolean isObjectArrived;
	
	/**
	 * 是否有人再等等
	 */
	private boolean isWaiting;
	
	/**
	 * 默认构造函数
	 */
	public ObjectWaiter()
	{
		this.target = null;
		this.exception = null;
		this.isWaiting = false;
		this.isObjectArrived = false;
		this.lock  = new Object();
	}
	
	/**
	 * 等待对象
	 * 如果结果没有返回，就在此等待
	 * @param time 等待多久如果结果没有到来就返回
	 * @return  等待结果对象
	 * @throws Exception 如果超时或结果出现异常就抛出
	 */
	public T waitObject(long timeout) throws ExecutionException, TimeoutException, InterruptedException
	{
		synchronized (lock) {
			
			//判断是否有结果返回，如果有直接返回结果，因为这里可能存在提前通知的可能
			if(this.isObjectArrived) {
				return this.getObject();
			}
			
			//没有结果返回，直接等待指定的时间，超时抛出异常，如果结果也有异常，就抛出异常，没有异常就返回结果
			//TODO ..这里应该对结果异常和超时异常分开抛出
			//重置一些变量，防止误用
			this.isWaiting = true;
			this.target   = null;
			this.exception = null;
			
			//等待指定的时间，注意这里超时也抛出和结果异常同样的异常，没有区分
	        lock.wait(timeout);
	        
	        //返回结果
	        return this.getObject();
        }
	}
	
	
	/**
	 * 等待结果，如果结果没有到来就一直等待
	 * @return
	 * @throws InterruptedException 
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws Exception
	 */
	public T waitObject() throws ExecutionException, TimeoutException, InterruptedException
	{
		return this.waitObject(0);
	}
	
	/**
	 * 等待对象收到
	 * @param target 等待对象
	 */
	public void objectArrive(T target)
	{
		synchronized (lock) {
			this.isObjectArrived = true;
	        this.target = target;
	        lock.notifyAll();
        }
	}
	
	/**
	 * 事件发生了异常
	 * @param exception
	 */
	public void objectException(Exception exception)
	{
		synchronized (lock) {
			this.target = null;
			this.isObjectArrived = true;
	        this.exception = exception;
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
	 * 判断是否有异常发生
	 * @return
	 */
	public boolean hasException()
	{
		return this.exception!=null;
	}
	
	/**
	 * 返回异常
	 * @return
	 */
	public Exception getException()
	{
		return this.exception;
	}
	
	/**
	 * 根据当前结果返回对象
	 * 如果有异常就抛出异常，如果没有异常就返回结果对象
	 * @throws ExecutionException 
	 * @throws TimeoutException 
	 * @throws Exception 
	 */
	public T getObject() throws ExecutionException, TimeoutException
	{
		if(this.exception!=null) {
			throw new ExecutionException(this.exception);
		}else if(this.target!=null) {
			return this.target;
		}else {
			throw new TimeoutException();
		}
	}
	
}
