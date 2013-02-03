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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.protocol.notify
 * File     : BuddyMessageRecivedNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-25
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import java.io.IOException;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.client.response.GetContactInfoResponseHandler;
import net.solosky.maplefetion.event.notify.BuddyMessageEvent;
import net.solosky.maplefetion.event.notify.GroupMessageEvent;
import net.solosky.maplefetion.event.notify.SystemMessageEvent;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcReceipt;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.UriHelper;

/**
 *
 *	收到服务消息回复
 *
 * @author solosky <solosky772@qq.com> 
 */
public class MessageNotifyHandler extends AbstractNotifyHandler
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.protocol.ISIPNotifyHandler#handle(net.solosky.maplefetion.sip.SIPNotify)
     */
    @Override
    public void handle(SipcNotify notify) throws FetionException
    {
    	SipcHeader event = notify.getHeader(SipcHeader.EVENT);
    	String from = notify.getFrom();
    	if(event!=null && event.getValue().equals("system-message")) {
    		this.systemMessageReceived(notify);
    	}else if(UriHelper.isGroup(from)) {
    		this.groupMessageReceived(notify);
    	}else {
    		this.buddyMessageRecived(notify);
    	}
    }
    
    /**
     * 好友消息
     * @throws IOException 
     */
    private void buddyMessageRecived(SipcNotify notify) throws FetionException
    {
    	//发送信息收到回复
	    SipcReceipt receipt = this.dialog.getMessageFactory()
	    .createDefaultReceipt(notify.getFrom(), Integer.toString(notify.getCallID()), notify.getSequence());
	    this.dialog.process(receipt);
	    
    	//查找消息是哪个好友发送的
        FetionStore store = this.context.getFetionStore();
	    Buddy from   = store.getBuddyByUri(notify.getFrom());
	    String body  = notify.getBody()!=null?notify.getBody().toSendString():"";	//防止产生NULL错误
	    Message msg  = this.parseMessage(notify);
	    
	    //如果好友没有找到，可能是陌生人发送的信息，
	    if(from==null) {
	    	//这里新建一个好友对象，并设置关系为陌生人
	    	from = UriHelper.createBuddy(notify.getFrom());
	    	BeanHelper.setValue(from, "relation", Relation.STRANGER);
	    	//添加至列表中
	    	this.context.getFetionStore().addBuddy(from);
	    	
	    	//如果是飞信好友，还需要获取这个陌生人的信息
	    	SipcRequest request = this.dialog.getMessageFactory().createGetContactInfoRequest(notify.getFrom());
	    	request.setResponseHandler(new GetContactInfoResponseHandler(context, dialog, from ,null));
	    	this.dialog.process(request);
	    }
	   
	    //查找这个好友的聊天代理对话
	    ChatDialogProxy chatDialogProxy = this.context.getChatDialogProxyFactoy().create(from);
	    
	    //通知消息监听器
	    if(chatDialogProxy!=null && this.context.getNotifyEventListener()!=null) {
	    	this.tryFireNotifyEvent(new BuddyMessageEvent(from, chatDialogProxy, msg));
	    }
	    
	    logger.debug("RecivedMessage:[from="+notify.getFrom()+", message="+body+"]");
    }
    
    /**
     * 系统消息
     */
    private void systemMessageReceived(SipcNotify notify)
    {
    	logger.debug("Recived a system message:"+notify.getBody().toSendString());
    	this.tryFireNotifyEvent(new SystemMessageEvent(notify.getBody().toSendString()));
    }
    
    /**
     * 从一个消息通知中解析出消息
     * @param notify
     * @return
     */
    private Message parseMessage(SipcNotify notify)
    {
    	 String body  = notify.getBody()!=null?notify.getBody().toSendString():"";	//防止产生NULL错误
 	    Message msg  = null;
 	    SipcHeader contentHeader = notify.getHeader(SipcHeader.CONTENT_TYPE);
 	    if(contentHeader!=null) {
 	    	String value = contentHeader.getValue();
 	    	if(Message.TYPE_PLAIN.equals(value)){
 	    		msg = new Message(body, Message.TYPE_PLAIN);
 	    	}else if(Message.TYPE_HTML.equals(value)){
 	    		msg = new Message(body, Message.TYPE_HTML);
 	    	}else{
 	    		msg = new Message(body, Message.TYPE_PLAIN);
 	    	}
 	    }else {
 	    	msg = new Message(body, Message.TYPE_PLAIN);	//默认为普通文本
 	    }
 	    
 	    return msg;
    }
    
    /**
     * 群消息
     */
    private void groupMessageReceived(SipcNotify notify) throws FetionException
    {
    	//发送信息收到回复
	    SipcReceipt receipt = this.dialog.getMessageFactory()
	    .createDefaultReceipt(notify.getFrom(), Integer.toString(notify.getCallID()), notify.getSequence());
	    this.dialog.process(receipt);
	    
	    Group  group  = this.context.getFetionStore().getGroup(notify.getFrom());
	    
	    Member member = this.context.getFetionStore().getGroupMember(group, notify.getHeader("SO").getValue());
	    String body   = notify.getBody()!=null?notify.getBody().toSendString():"";	//防止产生NULL错误
	    GroupDialog groupDialog = this.context.getDialogFactory().findGroupDialog(group);
	    
	    if(group!=null && member!=null && groupDialog!=null&&this.context.getNotifyEventListener()!=null) {
	    	this.tryFireNotifyEvent(new GroupMessageEvent(group, member, this.parseMessage(notify), groupDialog));
	    	
	    	logger.debug("Received a group message:[ Group="+group.getName()+", from="+member.getDisplayName()+", msg="+body );
	    }
    }
}
