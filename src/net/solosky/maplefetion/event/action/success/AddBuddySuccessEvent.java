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
 * Package  : net.solosky.maplefetion.event.action.success
 * File     : AddBuddySuccessEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action.success;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.event.action.SuccessEvent;

/**
*
* 添加好友成功，返回这个好友对象
*
* @author solosky <solosky772@qq.com>
*
*/
public class AddBuddySuccessEvent extends SuccessEvent {

	/**
	 * 找到的好友对象
	 */
	private Buddy addBuddy;
	/**
	 * @param response
	 */
	public AddBuddySuccessEvent(Buddy buddy)
	{
		this.addBuddy = buddy;
	}
	/**
	 * @return the foundBuddy
	 */
	public Buddy getAddBuddy()
	{
		return addBuddy;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "FindBuddySuccessEvent [foundBuddy=" + addBuddy
				+ ", EventType=" + getEventType() + "]";
	}
}
