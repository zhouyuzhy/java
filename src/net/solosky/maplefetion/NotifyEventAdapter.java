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
 * Package  : net.solosky.maplefetion
 * File     : NotifyEventAdapter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.notify.BuddyApplicationEvent;
import net.solosky.maplefetion.event.notify.BuddyConfirmedEvent;
import net.solosky.maplefetion.event.notify.BuddyMessageEvent;
import net.solosky.maplefetion.event.notify.BuddyPresenceEvent;
import net.solosky.maplefetion.event.notify.ClientStateEvent;
import net.solosky.maplefetion.event.notify.GroupMessageEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;
import net.solosky.maplefetion.event.notify.InviteReceivedEvent;
import net.solosky.maplefetion.event.notify.LoginStateEvent;
import net.solosky.maplefetion.event.notify.SystemMessageEvent;

/**
 *
 * 通知事件适配器，可以把通知事件适配为调用函数，方便处理
 * 用户可以继承这个类，并重载感兴趣的事件
 * 默认是什么也不做
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class NotifyEventAdapter implements NotifyEventListener
{

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.NotifyEventListener#fireEvent(net.solosky.maplefetion.event.NotifyEvent)
	 */
	@Override
	public void fireEvent(NotifyEvent event)
	{
		switch(event.getEventType()){
			case BUDDY_APPLICAION:
				BuddyApplicationEvent e1 = (BuddyApplicationEvent) event;
				this.buddyApplication(e1.getBuddy(), e1.getDesc());
				break;
				
			case BUDDY_MESSAGE:
				BuddyMessageEvent e2 = (BuddyMessageEvent) event;
				this.buddyMessageRecived(e2.getBuddy(), e2.getMessage(), e2.getDialogProxy());
				break;
				
			case BUDDY_CONFIRMED:
				BuddyConfirmedEvent e3 = (BuddyConfirmedEvent) event;
				this.buddyConfirmed(e3.getBuddy(), e3.isAgreed());
				break;
				
			case BUDDY_PRESENCE:
				BuddyPresenceEvent e4 = (BuddyPresenceEvent) event;
				this.buddyPresenceChanged(e4.getBuddy());
				break;
				
			case CLIENT_STATE:
				ClientStateEvent e5 = (ClientStateEvent) event;
				this.clientStateChanged(e5.getClientState());
				break;
			
			case GROUP_MESSAGE:
				GroupMessageEvent e6 = (GroupMessageEvent) event;
				this.groupMessageRecived(e6.getGroup(),e6.getMember(), e6.getMessage(), e6.getGroupDialog());
				break;
			
			case LOGIN_STATE:
				LoginStateEvent e7 = (LoginStateEvent) event;
				this.loginStateChanged(e7.getLoginState());
				break;
				
			case SYSTEM_MESSAGE:
				SystemMessageEvent e8 = (SystemMessageEvent) event;
				this.systemMessageRecived(e8.getSystemMessage());
				break;
				
			case INVITE_RECEIVED:
				InviteReceivedEvent e9 = (InviteReceivedEvent) event;
				this.inviteReceived(e9.getChatDialogProxy());
				break;
				
			case IMAGE_VERIFY:
				ImageVerifyEvent e10 = (ImageVerifyEvent) event;
				this.imageVerify(e10.getVerifyImage(), e10.getVerifyReason(), e10.getVerifyTips(), e10);
				
				default:;
				
				
		}
	}
	
	/**
	 * 处理验证码
     * @param verifyImage		验证图片
     * @param verifyReason		验证原因
     * @param verifyTips		验证提示
     */
    protected void imageVerify(VerifyImage verifyImage, String verifyReason,
            String verifyTips, ImageVerifyEvent event)
    {
	    
    }

	/**
	 * 收到用户消息
	 * @param from 		来自好友
	 * @param message	用户消息字符串
	 */
	protected void buddyMessageRecived(Buddy from, Message message, ChatDialogProxy dialog)
	{
	}
	
	/**
	 * 接收到了群消息
	 * @param group		来自群
	 * @param from		来自群成员
	 * @param message	消息内容
	 * @param dialog	群对话框
	 */
	protected void groupMessageRecived(Group group, Member from, Message message, GroupDialog dialog)
	{
	}
	
	/**
	 * 收到系统消息
	 * @param m			系统消息字符串
	 */
	protected void systemMessageRecived(String m)
	{
	}
	
	/**
	 * 收到添加好友请求
	 * @param b			待添加的好友
	 * @param desc		请求者
	 */
	protected void buddyApplication(Buddy buddy, String desc)
	{
	}
	
	/**
	 * 回复添加对方为好友请求
	 * @param buddy		好友对象
	 * @param isAgreed 	对方是否同意添加
	 */
	protected void buddyConfirmed(Buddy buddy, boolean isAgreed)
	{
	}
	
	/**
	 *  用户状态改变了
	 * @param 状态改变的好友
	 */
	protected void buddyPresenceChanged(Buddy buddy)
	{
	}
	
	
	/**
	 * 客户端状态发生了改变
	 * @param state
	 */
	protected void clientStateChanged(ClientState state)
	{
	}
	
	/**
	 * 登陆状态发生了改变
	 * @param state
	 */
	protected void loginStateChanged(LoginState state)
	{
	}
	
	/**
	 * 收到了一个邀请
	 */
	protected void inviteReceived(ChatDialogProxy dialog)
	{
	}

}
