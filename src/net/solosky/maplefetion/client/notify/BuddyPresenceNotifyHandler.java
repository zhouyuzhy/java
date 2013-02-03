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
 * File     : PresenceChangedSIPNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-24
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.client.dialog.BasicChatDialog;
import net.solosky.maplefetion.client.dialog.ChatDialog;
import net.solosky.maplefetion.event.notify.BuddyPresenceEvent;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 好友状态改变
 *
 * @author solosky <solosky772@qq.com> 
 */
public class BuddyPresenceNotifyHandler extends AbstractNotifyHandler
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.protocol.ISIPNotifyHandler#handle(net.solosky.maplefetion.sip.SIPNotify)
     */
    @Override
    public void handle(SipcNotify notify) throws FetionException
    {
	    Element root = XMLHelper.build(notify.getBody().toSendString());
	    List list = XMLHelper.findAll(root, "/events/event/contacts/*c");
	    if(list==null)	return;
	    Iterator it = list.iterator();
	    while(it.hasNext()) {
	    	Element contact = (Element) it.next();
	 	    Element personal = contact.getChild("p");
	 	    Element presence = contact.getChild("pr");
    	    Integer userId = Integer.parseInt(contact.getAttributeValue("id"));
    	    
    	    //查找这个好友
    	    Buddy buddy = context.getFetionStore().getBuddyByUserId(userId);
    	    //这里可能是用户自己
    	    if(buddy==null && context.getFetionUser().getUserId()==userId) {
    	    	buddy = context.getFetionUser();			
    	    }    	    
    	    
    	    //判断用户是不是飞信好友
    	    if(buddy!=null) {
				//好友信息改变
        	    if(personal!=null) {
        	    	String nickname = personal.getAttributeValue("n");
        	    	String impresa  = personal.getAttributeValue("i");
        	    	if(nickname!=null && nickname.length()>0) buddy.setNickName(nickname);
        	    	if(impresa!=null && impresa.length()>0)   buddy.setImpresa(impresa);
        	    	
        	    	String m = personal.getAttributeValue("m");
        	    	if(m!=null && m.length()>0)
        	    		buddy.setMobile(Long.parseLong(m));
    				
        	    	String sms = personal.getAttributeValue("sms");
        	    	if(sms!=null) {
        	    		buddy.getSMSPolicy().parse(sms);
        	    	}
        	    }

				//状态改变
        	    if(presence!=null) {
            	    int oldpresense = buddy.getPresence().getValue(); 
            	    int curpresense = Integer.parseInt(presence.getAttributeValue("b"));  
            	    BeanHelper.toBean(Presence.class, buddy.getPresence(), presence);
            	    if(oldpresense!=curpresense) {
            	    	//注意，如果好友上线了，并且当前打开了手机聊天对话框，需要关闭这个手机聊天对话框
            	    	if(curpresense == Presence.AWAY   || curpresense == Presence.BUSY ||
            	    	   curpresense == Presence.ONLINE || curpresense == Presence.ROBOT ) {
            	    		ChatDialog chatDialog = this.context.getDialogFactory().findChatDialog(buddy);
            	    		if(chatDialog!=null && chatDialog instanceof BasicChatDialog) {
            	    			chatDialog.closeDialog();
            	    		}
            	    	}
            	    	
            	    	//通知监听器，好友状态已经改变
            	    	this.tryFireNotifyEvent(new BuddyPresenceEvent(buddy));
            	    }
        	    }

        	    //刷新数据
        	    context.getFetionStore().flushBuddy(buddy);
        	    logger.debug("PresenceChanged:"+buddy.toString()+" - "+buddy.getPresence());
        	    //TODO ..这里只处理了好友状态改变，本来还应该处理其他信息改变，如好友个性签名和昵称的改变，以后添加。。
    	    }else{
    	    	logger.warn("Unknown Buddy in PresenceChanged notify:"+userId);
    	    }
	    }
    }
}
