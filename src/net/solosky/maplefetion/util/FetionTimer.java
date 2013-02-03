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
 * File     : FetionTimer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.TimerTask;

/**
 *
 * 飞信定时器接口
 * 多个飞信客户端可以共享一个定时器来提高效率
 *
 * @author solosky <solosky772@qq.com>
 */
public interface FetionTimer
{
	/**
	 * 计划一个任务
	 * @param task		任务对象
	 * @param delay		在多少时间后开始执行
	 * @param period	执行的周期是多少
	 */
	public void scheduleTask(TimerTask task, long delay, long period);
	
	/**
	 * 移除已经取消的任务
	 */
	public void clearCanceledTask();
	
	/**
	 * 启动计时器
	 */
	public void startTimer();
	
	/**
	 * 停止计数器
	 */
	public void stopTimer();
}
