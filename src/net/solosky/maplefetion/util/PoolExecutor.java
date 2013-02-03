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
 * File     : PoolExecutor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-7-14
 * License  : Apache License 2.0 
 */

package net.solosky.maplefetion.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.solosky.maplefetion.util.FetionExecutor;

/**
*
*  执行池，这个执行器可以给定一个java.util.concurrent.ExecutorService实例来完成执行工作
*  默认构造器创建了一个缓存的执行池 Executors.newCachedThreadPool();
*
* @author solosky <solosky772@qq.com>
*/

public class PoolExecutor implements FetionExecutor {

	/**
	 * 执行服务
	 */
	private ExecutorService es;
	
	/**
	 * 使用一个执行服务对象来构建执行池
	 * @param es
	 */
	public PoolExecutor(ExecutorService es)
	{
		this.es = es;
	}
	
	/**
	 * 默认使用可缓存的执行池
	 */
	public PoolExecutor()
	{
		this.es =Executors.newCachedThreadPool();
	}
	@Override
	public void startExecutor() {
	}

	@Override
	public void stopExecutor() {
		es.shutdownNow();
	}

	@Override
	public void submitTask(Runnable arg0) {
		es.submit(arg0);
	}

}
