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
 * File     : InviteReceivedEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : InviteReceivedEvent.java
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.notify;

import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;

/**
 * 
 * 接收到一个邀请事件
 * 
 * @author solosky <solosky772@qq.com>
 *
 */
public class InviteReceivedEvent extends NotifyEvent
{

	private ChatDialogProxy dialogProxy;
	
	/**
	 * @param dialogProxy
	 */
	public InviteReceivedEvent(ChatDialogProxy dialogProxy) {
		super();
		this.dialogProxy = dialogProxy;
	}


	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.event.NotifyEvent#getEventType()
	 */
	@Override
	public NotifyEventType getEventType() {
		return NotifyEventType.INVITE_RECEIVED;
	}


	/**
	 * @return the dialogProxy
	 */
	public ChatDialogProxy getChatDialogProxy() {
		return dialogProxy;
	}
	
	

}
