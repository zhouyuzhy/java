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
 * Package  : net.solosky.maplefetion.event.notify
 * File     : BuddyMessageEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.notify;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;

/**
 *
 * 好友消息事件
 *
 * @author solosky <solosky772@qq.com>
 *
 */ 
public class BuddyMessageEvent extends NotifyEvent
{

	/**
	 * 来自好友
	 */
	private Buddy buddy;
	
	/**
	 * 好友对话代理
	 */
	private ChatDialogProxy dialogProxy;
	
	/**
	 * 消息对象
	 */
	private Message message;
	
	/**
	 * @param buddy
	 * @param dialogProxy
	 * @param message
	 */
	public BuddyMessageEvent(Buddy buddy,
			ChatDialogProxy dialogProxy, Message message)
	{
		this.buddy = buddy;
		this.dialogProxy = dialogProxy;
		this.message = message;
	}



	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.event.NotifyEvent#getEventType()
	 */
	@Override
	public NotifyEventType getEventType()
	{
		return NotifyEventType.BUDDY_MESSAGE;
	}



	/**
	 * @return the buddy
	 */
	public Buddy getBuddy()
	{
		return buddy;
	}


	/**
	 * @return the dialogProxy
	 */
	public ChatDialogProxy getDialogProxy()
	{
		return dialogProxy;
	}



	/**
	 * @return the message
	 */
	public Message getMessage()
	{
		return message;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "BuddyMessageEvent [buddy=" + buddy + ", dialogProxy="
				+ dialogProxy + ", message=" + message + ", EventType="
				+ getEventType() + "]";
	}

}
