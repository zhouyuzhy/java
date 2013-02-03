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
 * Package  : net.solosky.net.maplefetion.client.dialog
 * File     : ChatDialog.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FutureActionEventListener;
import net.solosky.maplefetion.net.TransferException;

/**
 * 
 * 聊天对话框 一个聊天对话框一定有一个主要参与者，如果支持多方会谈，可能有多个参与者，不过需要实现MutipartyDialog接口
 * 用户发送消息可以通过客户端对象发送，也可以通过聊天对话框发送
 * 
 * @author solosky <solosky772@qq.com>
 */
public abstract class ChatDialog extends Dialog
{

	/**
	 * 主要参与者
	 */
	protected Buddy mainBuddy;

	/**
	 * 上一次收发消息时间 如果空闲时间超过指定的时间就关闭这个对话框
	 */
	protected int activeTime;
	
	/**
	 * 主要参与者
	 * 
	 * @param mainBuddy
	 *            主要参与者
	 * @param client
	 *            飞信对象
	 */
	public ChatDialog(Buddy mainBuddy, FetionContext client)
	{
		super(client);
		this.mainBuddy = mainBuddy;
		this.activeTime = (int) System.currentTimeMillis()/1000;
	}
	
	/**
	 * 仅以客户端对象构造对话框
	 * @param client
	 */
	public ChatDialog(FetionContext client)
	{
		super(client);
		this.activeTime = (int) System.currentTimeMillis()/1000;
	}
	
	/**
	 * 返回主要参与者
	 * 
	 * @return 主要参与者
	 */
	public Buddy getMainBuddy()
	{
		return this.mainBuddy;
	}

	/**
	 * 是否支持多方会话，子类实现
	 * @return
	 */
	public abstract boolean isMutipartySupported();
	
	/**
	 * 给好友发送在线消息
	 * @param message		消息内容
	 * @param listener		操作监听器
	 * @throws TransferException
	 */
	public abstract void sendChatMessage(Message message, ActionEventListener listener);
	
	
	/**
	 * 给好友发送在线消息
	 * @param message		消息内容
	 * @return				操作等待对象
	 * @throws TransferException
	 */
	public ActionEventFuture sendChatMessage(Message message)
	{
		ActionEventFuture future = new ActionEventFuture();
		this.sendChatMessage(message, new FutureActionEventListener(future));
		return future;
	}

	/**
	 * 发送短信消息
	 * 
	 * @param message
	 *            消息正文
	 * @return 操作等待对象
	 */
	public ActionEventFuture sendSMSMessage(Message message)
	{
		ActionEventFuture future = new ActionEventFuture();
		this.sendSMSMessage(message, new FutureActionEventListener(future));
		return future;
	}

	/**
	 * 发送短信消息
	 * 
	 * @param message
	 *            消息正文
	 * @param listener
	 *            操作监听器
	 */
	public void sendSMSMessage(Message message, ActionEventListener listener)
	{
		this.context.getDialogFactory().getServerDialog().sendSMSMessage(
		        this.mainBuddy, message, listener);
		this.updateActiveTime();
	}

	/**
	 * 返回上一次活动时间
     * @return the activeTime
     */
    public long getActiveTime()
    {
    	return activeTime;
    }

	/**
     * @param activeTime the activeTime to set
     */
    public void updateActiveTime()
    {
    	this.activeTime = (int) (System.currentTimeMillis()/1000);
    }
}
