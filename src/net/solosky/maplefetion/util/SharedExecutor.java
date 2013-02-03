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
 * Package  : net.solosky.maplefetion.util
 * File     : SharedExecutor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 *
 * 共享的执行池，
 * 这个执行池使用一个固定数目的或者不固定数目的线程池来和多个飞信实例共享
 *
 * @author solosky <solosky772@qq.com>
 */
public class SharedExecutor implements FetionExecutor
{
	/**
	 * 执行服务 
	 */
	private ExecutorService executorService;
	
	/**
	 * LOGGER
	 */
	private static Logger logger = Logger.getLogger(SingleExecutor.class);
	

	/**
	 * 固定的线程池
	 * @param size 线程最大数量
	 */
	public SharedExecutor(int size)
	{
		this.executorService = Executors.newFixedThreadPool(size);
	}
	
	/**
	 * 不限大小的线程池，但会自动回收
	 */
	public SharedExecutor()
	{
		this.executorService = Executors.newCachedThreadPool();
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.util.FetionExecutor#startExecutor()
     */
    @Override
    public void startExecutor()
    {
    	//什么也不做
    }

	/* (non-Javadoc)
     * @see nnet.solosky.maplefetion.util.FetionExecutor#stopExecutor()
     */
    @Override
    public void stopExecutor()
    {
    	//什么 也不做
    }
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.util.FetionExecutor#submitTask(java.lang.Runnable)
     */
    @Override
    public void submitTask(Runnable task)
    {
    	this.executorService.submit(task);
    	logger.debug("Execute task submitted:"+task.getClass().getName());
    }
    
    /**
     * 这个方法才是真正的关闭执行池哦 
     * 在所有客户端退出的时候一定要调用这个方法来释放线程资源
     */
    public void reallyStopExecutor()
    {
    	this.executorService.shutdown();
    }

}
