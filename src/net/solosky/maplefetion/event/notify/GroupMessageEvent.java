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
 * File     : GroupMessageEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.notify;

import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;

/**
 *
 * 群消息
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class GroupMessageEvent extends NotifyEvent
{

	/**
	 * 发送消息的群
	 */
	private Group group;
	
	/**
	 * 发送消息的成员
	 */
	private Member member;
	
	/**
	 * 消息内容
	 */
	private Message message;
	
	/**
	 * 群对话
	 */
	private GroupDialog groupDialog;
	
	
	/**
	 * @param group
	 * @param member
	 * @param message
	 * @param groupDialog
	 */
	public GroupMessageEvent(Group group, Member member,
			Message message, GroupDialog groupDialog)
	{
		this.group = group;
		this.member = member;
		this.message = message;
		this.groupDialog = groupDialog;
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.event.NotifyEvent#getEventType()
	 */
	@Override
	public NotifyEventType getEventType()
	{
		return NotifyEventType.GROUP_MESSAGE;
	}

	/**
	 * @return the group
	 */
	public Group getGroup()
	{
		return group;
	}

	/**
	 * @return the member
	 */
	public Member getMember()
	{
		return member;
	}

	/**
	 * @return the message
	 */
	public Message getMessage()
	{
		return message;
	}

	/**
	 * @return the groupDialog
	 */
	public GroupDialog getGroupDialog()
	{
		return groupDialog;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "GroupMessageEvent [group=" + group + ", groupDialog="
				+ groupDialog + ", member=" + member + ", message=" + message
				+ ", EventType=" + getEventType() + "]";
	}
	
	

}
