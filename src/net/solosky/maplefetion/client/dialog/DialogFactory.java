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
 * File     : DialogFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.TransferFactory;
import net.solosky.maplefetion.sipc.SipcNotify;

/**
 * 
 * 对话工厂，建立三种类型的对话
 * 
 * @author solosky <solosky772@qq.com>
 */
public class DialogFactory
{
	/**
	 * 客户端对象
	 */
	private FetionContext context;

	/**
	 * 服务器对话
	 */
	private ServerDialog serverDialog;

	/**
	 * 聊天对话列表
	 */
	private ArrayList<ChatDialog> chatDialogList;

	/**
	 * 群对话框列表
	 */
	private ArrayList<GroupDialog> groupDialogList;
	
	/**
	 * 定时空闲对话框检查任务
	 */
	private TimerTask idleDialogCheckTask;
	

	/**
	 * 默认构造函数
	 * 
	 * @param client
	 */
	public DialogFactory(FetionContext client)
	{
		this.context = client;
		this.chatDialogList = new ArrayList<ChatDialog>();
		this.groupDialogList = new ArrayList<GroupDialog>();
		this.idleDialogCheckTask = new IdleDialogCheckTask();

		this.context.getFetionTimer().scheduleTask(this.idleDialogCheckTask, 0 ,
		                FetionConfig.getInteger("fetion.dialog.check-idle-interval") * 1000);
	}

	/**
	 * 创建服务器对话，只能创建一次
	 * 
	 * @param user
	 *            登录用户对象
	 */
	public synchronized ServerDialog createServerDialog()
	{
		if(this.serverDialog==null) {
			this.serverDialog = new ServerDialog(context);
			return this.serverDialog;
		}else {
			throw new IllegalStateException("ServerDialog arealy created...");
		}
	}

	/**
	 * 创建聊天对话
	 * 
	 * @param buddy
	 *            好友对象
	 * @return
	 */
	public synchronized ChatDialog createChatDialog(Buddy buddy)
	        throws DialogException
	{
		Relation relation = buddy.getRelation();
		if( relation==Relation.BANNED ||
			relation==Relation.DECLINED ||
			relation==Relation.UNCONFIRMED) {
			throw new DialogException("Buddy relation is +"+buddy.getRelation()+", you couldn't send chat message to this buddy.");
		}
		ChatDialog dialog = this.findChatDialog(buddy);
		if (dialog != null && dialog.getState()!=DialogState.CLOSED && dialog.getState()!=DialogState.FAILED)
			return dialog;

		int presence = buddy.getPresence().getValue();
		// 如果用户手机在线 或者电脑离线，将建立手机聊天对话框
		if (presence == Presence.OFFLINE) {
			dialog = new BasicChatDialog(this.context, buddy);
		} else if (presence == Presence.ONLINE || presence == Presence.AWAY ||
		            presence == Presence.BUSY   || presence == Presence.ROBOT ) { // 如果用户电脑在线，建立在线聊天对话框
			TransferFactory factory = this.context.getTransferFactory();
			if (factory.isMutiConnectionSupported()) {
				dialog = new LiveV2ChatDialog(buddy, this.context);
			} else {
				dialog = new LiveV1ChatDialog(buddy, this.context);
			}
		} else {
			throw new DialogException("Illegal buddy presence - presence="
			        + Integer.toString(presence));
		}

		// 添加对话框至列表并返回
		this.chatDialogList.add(dialog);
		return dialog;
	}

	/**
	 * 以一个邀请通知创建会话
	 * 
	 * @param inviteNotify
	 *            邀请的通知
	 * @return
	 */
	public synchronized ChatDialog createChatDialog(SipcNotify inviteNotify) throws DialogException
	{
		// 当收到会话邀请时，发起邀请的好友一定是在线，手机在线的好友是不会发起会话邀请的，所以这里无需判断好友状态
		ChatDialog dialog = null;

		if (this.context.getTransferFactory().isMutiConnectionSupported()) {
			dialog = new LiveV2ChatDialog(inviteNotify, context);
		} else {
			dialog = new LiveV1ChatDialog(inviteNotify, context);
		}
		this.chatDialogList.add(dialog);

		return dialog;
	}

	/**
	 * 创建群组对话
	 * 
	 * @param group
	 *            群对象
	 * @return
	 */
	public synchronized GroupDialog createGroupDialog(Group group)
	{
		GroupDialog dialog = new GroupDialog(this.context, group);
		this.groupDialogList.add(dialog);
		return dialog;
	}

	/**
	 * 查找聊天对话框 只是查找对话框的主要参与者
	 * 
	 * @param buddy
	 *            飞信对象
	 * @return
	 */
	public synchronized ChatDialog findChatDialog(Buddy buddy)
	{
		Iterator<ChatDialog> it = this.chatDialogList.iterator();
		ChatDialog dialog = null;
		while (it.hasNext()) {
			dialog = it.next();
			if (dialog.getMainBuddy().equals(buddy)) {
				return dialog;
			}
		}
		return null;
	}
	
	/**
	 * 查找对话，如果对话不存在，建立一个新的对话
	 * @param buddy		对话的好友
	 * @return			聊天对话
	 * @throws DialogException
	 */
	public synchronized ChatDialog findOrCreateChatDialog(Buddy buddy) throws DialogException
	{
		ChatDialog dialog  = this.findChatDialog(buddy);
		if(dialog==null)
			dialog = this.createChatDialog(buddy);
		
		return dialog;
	}
	
	/**
	 * 返回一个聊天对话框，首先查找当前活动的聊天对话，如果找到并且没有关闭就返回这个对话
	 * 如果不存在或者会话已经关闭就新建立一个对话并返回
	 * @param buddy		好友对象
	 * @return			聊天对话
	 * @throws DialogException
	 */
	public synchronized ChatDialog getChatDialog(Buddy buddy) throws DialogException
	{
		ChatDialog dialog = this.findChatDialog(buddy);
		if(dialog!=null && dialog.getState()!=DialogState.CLOSED) {
			return dialog;
		}else {
			return this.createChatDialog(buddy);
		}
		
	}

	/**
	 * 查找群对话框
	 * 
	 * @param group
	 *            群对象
	 * @return
	 */
	public synchronized GroupDialog findGroupDialog(Group group)
	{
		Iterator<GroupDialog> it = this.groupDialogList.iterator();
		GroupDialog dialog = null;
		while (it.hasNext()) {
			dialog = it.next();
			if (dialog.getGroup().equals(group)) {
				break;
			}
		}

		return dialog;
	}

	/**
	 * 返回服务器对话框
	 * 
	 * @return
	 */
	public ServerDialog getServerDialog()
	{
		return this.serverDialog;
	}
	
	/**
	 * 移除对话,不会关闭对话
	 */
	public synchronized void removeDialog(Dialog dialog)
	{
		if (dialog instanceof ChatDialog) {
			this.chatDialogList.remove(dialog);
		}else if(dialog instanceof GroupDialog){
			this.groupDialogList.remove(dialog);
		}
	}
	
	/**
	 * 关闭所有的对话框
	 * @throws TransferException
	 * @throws DialogException
	 */
	public synchronized void closeAllDialog() throws TransferException, DialogException
	{
		ArrayList<GroupDialog> tmpGroupList = new ArrayList<GroupDialog>();
		tmpGroupList.addAll(this.groupDialogList);
		Iterator<GroupDialog> git = tmpGroupList.iterator();
		while(git.hasNext()) {
			git.next().closeDialog();
		}
		
		ArrayList<ChatDialog> tmpChatList   = new ArrayList<ChatDialog>(); 
		tmpChatList.addAll(this.chatDialogList);
		Iterator<ChatDialog> cit = tmpChatList.iterator();
		while(cit.hasNext()) {
			cit.next().closeDialog();
		}
		
		if(this.serverDialog!=null) {
			this.serverDialog.closeDialog();
			this.serverDialog = null;
		}
	}
	
	
	/**
	 * 关闭对话框工厂
	 * 停止定时任务
	 */
	public void closeFactory()
	{
		this.idleDialogCheckTask.cancel();
		this.context.getFetionTimer().clearCanceledTask();
	}
	
	
	/**
	 * 为了减少资源占用率，如果用户没有手动关闭对话框，就需要一个计划任务定时检查空闲的对话框， 
	 * 如果对话框在指定的时间没有收到消息，就关闭这个对话框
	 */
	public synchronized void checkIdleDialog()
	{
		int maxIdleTime = FetionConfig
        .getInteger("fetion.dialog.max-idle-time"); // 最大空闲时间,单位秒，用户可以设置
        Iterator<ChatDialog> it = chatDialogList.iterator();
        while (it.hasNext()) {
        	ChatDialog dialog = it.next();
        	if (dialog.getState()==DialogState.CLOSED) {
        		it.remove();
        	}else if (dialog.getActiveTime() + maxIdleTime < (int) (System.currentTimeMillis() / 1000)) {
        		//异步关闭，如果在这里关闭，因为有了一把DialogFactory的锁，在关闭对话框的过程中会去获得
        		//相应对话框的锁，这就可能出现死锁，因此这里把关闭操作放在另外一个线程里，就不会出现死锁
        		dialog.closeDialog(null);		
                it.remove();
        	} else {
        	}
        }
	}

	/**
	 * 定时检查空闲对话框任务，只是对checkIdleDialog()的一个封装
	 * 
	 * @author solosky <solosky772@qq.com>
	 */
	private class IdleDialogCheckTask extends TimerTask
	{
		@Override
		public void run()
		{
			checkIdleDialog();
		}

	}
}
