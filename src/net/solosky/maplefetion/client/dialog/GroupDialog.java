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
 * File     : GroupDialog.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.TimerTask;

import net.solosky.maplefetion.ClientState;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.client.response.DefaultResponseHandler;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.sipc.SipcOutMessage;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.sipc.SipcStatus;
import net.solosky.maplefetion.util.CallHelper;
import net.solosky.maplefetion.util.ResponseFuture;

import org.apache.log4j.Logger;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class GroupDialog extends Dialog
{

	/**
	 * 群对象
	 */
	private Group group;
	
	/**
	 * 群工具
	 */
	private CallHelper helper;
	
	/**
	 * 发送群在线请求任务
	 */
	private TimerTask keepLiveTask;
	
	/**
	 * 构造函数
     * @param client
     * @param group
     */
    public GroupDialog(FetionContext client, Group group)
    {
	    super(client);
	    this.group  = group;
	    this.helper = new CallHelper();
	    this.keepLiveTask = new GroupKeepLiveTask();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#doCloseDialog()
     */
    @Override
    protected void doCloseDialog() throws Exception
    {
		if(this.context.getState()==ClientState.ONLINE) {
			this.bye();
		}
        this.keepLiveTask.cancel();
    	this.context.getFetionTimer().clearCanceledTask();
    }
    
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#doOpenDialog()
     */
    @Override
    protected void doOpenDialog() throws Exception
    {
		this.invite();
		this.ack();
		this.setPresence();
		this.subscribeNotify();
		this.context.getFetionTimer().scheduleTask(this.keepLiveTask, 0, 3*60*1000);
    		
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#process(net.solosky.maplefetion.sipc.SipcOutMessage)
     */
    @Override
    public void process(SipcOutMessage out) 
    {
    	this.context.getDialogFactory().getServerDialog().process(out);
    }

    
    /**
     * 群会话开始
     * @throws TransferException 
     * @throws InterruptedException 
     * @throws RequestTimeoutException 
     * @throws IllegalResponseException 
     */
    private void invite() throws TransferException, IllegalResponseException, RequestTimeoutException, InterruptedException
    {
    	SipcRequest request = this.getMessageFactory().createInviteRequest(
    			this.group.getUri(),this.context.getTransferFactory().getDefaultTransferLocalPort());
    	this.helper.set(request);
    	ResponseFuture future = ResponseFuture.wrap(request);
    	this.process(request);
    	
    	int status = future.waitResponse().getStatusCode();
    	if(status==SipcStatus.TRYING) {
    		assertStatus(future.waitResponse().getStatusCode(), SipcStatus.ACTION_OK);
    	}else if(status==SipcStatus.ACTION_OK) {
    	}else {
    		throw new IllegalResponseException("Unexpected response status");
    	}
    }
    
    /**
     * 确认会话已经建立
     * @throws TransferException 
     */
    private void ack() throws TransferException
    {
    	//这个请求不需要回复
    	SipcRequest request = this.getMessageFactory().createAckRequest(this.group.getUri());
    	this.helper.set(request);
    	this.process(request);
    }
    
    /**
     * 设置在线状态
     * @throws TransferException
     * @throws RequestTimeoutException
     * @throws InterruptedException
     * @throws IllegalResponseException
     */
    private void setPresence() throws TransferException, RequestTimeoutException, InterruptedException, IllegalResponseException
    {
    	SipcRequest request = this.getMessageFactory().createSetGroupPresenceRequest(group.getUri(), Presence.ONLINE);
    	this.helper.set(request);
    	ResponseFuture future = ResponseFuture.wrap(request);
    	this.process(request);
    	SipcResponse response = future.waitResponse();
    	assertStatus(response.getStatusCode(), SipcStatus.ACTION_OK);
    }
    
    /**
     * 订阅通知
     * @throws TransferException 
     * @throws IllegalResponseException 
     * @throws InterruptedException 
     * @throws RequestTimeoutException 
     */
    private void subscribeNotify() throws TransferException, IllegalResponseException, RequestTimeoutException, InterruptedException
    {
    	SipcRequest request = this.getMessageFactory().createSubscribeGroupNotifyRequest(this.group.getUri());
    	this.helper.set(request);
    	ResponseFuture future = ResponseFuture.wrap(request);
    	this.process(request);
    	SipcResponse response = future.waitResponse();
    	assertStatus(response.getStatusCode(), SipcStatus.ACTION_OK);
    }
 
    /**
     * 离开对话
     * @throws TransferException 
     * @throws InterruptedException 
     * @throws RequestTimeoutException 
     * @throws IllegalResponseException 
     */
    private void bye() throws TransferException, RequestTimeoutException, InterruptedException, IllegalResponseException
    {
    	SipcRequest request = this.getMessageFactory().createLogoutRequest(this.group.getUri());
    	this.helper.set(request);
    	ResponseFuture future = ResponseFuture.wrap(request);
    	this.process(request);
    	SipcResponse response = future.waitResponse();
    	assertStatus(response.getStatusCode(), SipcStatus.ACTION_OK);
    }
    
	/**
     * @return the group
     */
    public Group getGroup()
    {
    	return group;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#getMessageFactory()
     */
    @Override
    public MessageFactory getMessageFactory()
    {
    	return this.context.getDialogFactory().getServerDialog().getMessageFactory();
    }
    
    /**
     * 
     * 内部类，实现了定时发送群在线请求
     *
     * @author solosky <solosky772@qq.com>
     */
    private class GroupKeepLiveTask extends TimerTask
    {
        @Override
        public void run()
        {
        	SipcRequest request = getMessageFactory().createGroupKeepLiveRequest(group.getUri());
        	request = helper.set(request);
        	ActionEventListener listener = new ActionEventListener()
			{
				public void fireEevent(ActionEvent event)
				{
					if(event.getEventType()!=ActionEventType.SUCCESS){
						Logger.getLogger(GroupDialog.class).warn("GroupDialog keepAlive failed.. Event="+event);
					}
				}
			};
        	request.setResponseHandler(new DefaultResponseHandler(listener));
            process(request);
        }
    }
    
    /**
     * 发送群消息
     * @param message		消息内容
     * @param listener		操作监听器
     * @throws TransferException
     */
    public void sendChatMessage(Message message, ActionEventListener listener)
    {
    	this.ensureOpened();
    	SipcRequest request = this.getMessageFactory().createSendGroupChatMessageRequest(this.group.getUri(), message.toString());
    	request = this.helper.set(request);
    	request.setResponseHandler(new DefaultResponseHandler(listener));
    	this.process(request);
    }
}
