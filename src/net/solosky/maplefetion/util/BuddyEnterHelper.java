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
 * Package  : net.solosky.maplefetion.client.dialog
 * File     : BuddyEnterFuture.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-1
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.client.dialog.DialogException;

/**
 *
 *  等待好友进入对话框
 *  
 *  这个工具目前只能实现等待一个好友进入对话，如果同时邀请了多个好友进入对话这个工具就会判断错误
 *  TODO 完善对同时邀请多个好友进入对话的等待操作
 *
 * @author solosky <solosky772@qq.com>
 */
public class BuddyEnterHelper
{
	/**
	 * 锁，利用这个对象来实现等待
	 */
	private Object lock;
	
	/**
	 * 当前的好友
	 */
	private Buddy curBuddy;
	
	/**
	 * 构造函数
	 */
	public BuddyEnterHelper()
	{
		this.lock = new Object();
	}
	
	/**
	 * 等待好友进入对话框
	 * @param buddy
	 * @throws InterruptedException 
	 * @throws InterruptedException 
	 */
	public void waitBuddyEnter(Buddy buddy, long timeout) throws DialogException, InterruptedException
	{
		synchronized (lock) {
			//检查是否提前通知
			if(this.curBuddy!=null && this.curBuddy.getUri().equals(buddy.getUri()))
				return;
			
			lock.wait(timeout);
			
			//如果等待失败，抛出异常
			if(this.curBuddy==null || !this.curBuddy.getUri().equals(buddy.getUri()))
				throw new DialogException("Buddy enter dialog failed. Buddy="+buddy);
		}
	}
	
	
	/**
	 * 通知等待线程，好友已经进入了   
	 * @param buddy
	 */
	public void buddyEntered(Buddy buddy)
	{
		synchronized (lock) {
			this.curBuddy = buddy;
			lock.notifyAll();
        }
	}
}
