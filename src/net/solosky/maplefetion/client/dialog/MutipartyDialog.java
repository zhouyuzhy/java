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
 * File     : MatipartyDialog.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-14
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.ArrayList;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.event.action.ActionEventListener;

/**
 *
 * 多发对话的接口
 *
 * @author solosky <solosky772@qq.com>
 */
public interface MutipartyDialog
{
	/**
	 * 返回所有的参与者 用户除外
	 * @return
	 */
	public ArrayList<Buddy> getBuddyList();
	
	/**
	 * 邀请好友
	 * @param buddy		好友对象
	 * @param listener 操作监听器
	 */
	public void inviteBuddy(Buddy buddy, ActionEventListener listener);
	
	/**
	 * 用户进入了对话
	 * @param buddy
	 */
	public void buddyEntered(Buddy buddy);
	
	/**
	 * 用户未能进入对话
	 */
	public void buddyFailed(Buddy buddy);
	
	/**
	 * 用户离开了对话
	 * @param buddy
	 */
	public void buddyLeft(Buddy buddy);
}
