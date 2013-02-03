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
 * Project  : MapleSMS
 * Package  : net.solosky.maplesms.gateway
 * File     : FetionGateway.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot;

import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.NotifyEventAdapter;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventListener;

/**
 *
 *消息网关
 *
 * @author solosky <solosky772@qq.com>
 */
public class FetionGateway extends NotifyEventAdapter implements Gateway
{

	private FetionClient client;
	private SMSListener listener;
	
	public FetionGateway(long mobile, String pass)
	{
		this.client = new FetionClient(Long.toString(mobile), pass, this);
	}
	/* (non-Javadoc)
     * @see net.solosky.maplesms.gateway.Gateway#login()
     */
    @Override
    public void login()
    {
    	this.client.syncLogin();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.gateway.Gateway#logout()
     */
    @Override
    public void logout()
    {
    	this.client.logout();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.gateway.Gateway#sendSMS(java.lang.String, java.lang.String)
     */
    @Override
    public void sendSMS(String uri, final String msg)
    {
    	try{
        	Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
        	ChatDialogProxy dialog = this.client.getChatDialogProxyFactory().create(buddy);
    		dialog.sendChatMessage(new Message(msg), new DefaultActionListener("发送消息[ "+msg+" ]给"+buddy.getDisplayName()));
        } catch (FetionException e) {
        	println("建立对话框时出错"+e.getMessage());
        }
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.gateway.Gateway#setSMSListener(net.solosky.maplesms.gateway.SMSListener)
     */
    @Override
    public void setSMSListener(SMSListener listener)
    {
    	this.listener = listener;
    }
    
    public void println(String msg)
    {
    	System.out.println("[FetionGateway] "+msg);
    }
    
    public class DefaultActionListener implements ActionEventListener{

    	private String title;
    	
    	public DefaultActionListener(String title) {
    		this.title = title;
    	}
		/* (non-Javadoc)
		 * @see net.solosky.maplefetion.client.dialog.ActionEventListener#fireEevent(net.solosky.maplefetion.event.ActionEvent)
		 */
		@Override
		public void fireEevent(ActionEvent event)
		{
			if(event.getEventType()==ActionEventType.SUCCESS){
				println(title+" 【成功】");
			}else{
				println(title+" 【失败】");
			}
		}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplesms.gateway.Gateway#getName()
     */
    @Override
    public String getName()
    {
    	return "[Fetion user="+this.client.getFetionUser().getDisplayName()+", uri="+this.client.getFetionUser().getUri()+"]";
    }
	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.NotifyEventAdapter#buddyMessageRecived(net.solosky.maplefetion.bean.Buddy, net.solosky.maplefetion.bean.Message, net.solosky.maplefetion.client.dialog.ChatDialogProxy)
	 */
	@Override
	protected void buddyMessageRecived(Buddy from, Message message, ChatDialogProxy dialog)
	{
		this.listener.smsRecived(from.getUri(), message.getText(), this);
	}
    
    
}
