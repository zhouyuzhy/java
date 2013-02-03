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
 * File     : FetionExecutor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

/**
 *
 * 抽象的执行池接口，飞信客户端使用这个执行池来提交操作给另外一个线程运行
 * 多个飞信客户端可以共享一个多线程执行池，可以提高效率
 *
 * @author solosky <solosky772@qq.com>
 */
public interface FetionExecutor
{

	/**
	 * 提交任务
	 * @param runnable
	 */
	public void submitTask(Runnable task);
	
	/**
	 * 启动执行器
	 */
	public void startExecutor();

	/**
	 * 关闭执行器
	 */
	public void stopExecutor();

}