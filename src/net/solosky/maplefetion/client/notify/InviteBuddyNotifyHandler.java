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
 * File     : InvateBuddyNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-26
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.client.dialog.BasicChatDialog;
import net.solosky.maplefetion.client.dialog.ChatDialog;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.event.notify.InviteReceivedEvent;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcReceipt;
import net.solosky.maplefetion.store.FetionStore;

/**
 *
 *	被邀请加入会会话处理器
 *
 * @author solosky <solosky772@qq.com> 
 */
public class InviteBuddyNotifyHandler extends AbstractNotifyHandler
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.protocol.ISIPNotifyHandler#handle(net.solosky.maplefetion.sip.SIPNotify)
     */
    @Override
    public void handle(final SipcNotify notify) throws FetionException
    {
    	logger.debug("Recived a invite - from = "+notify.getFrom());
		//发送已收到消息
		SipcReceipt receipt = null;
		if(this.context.getTransferFactory().isMutiConnectionSupported()) {
    		receipt = this.dialog.getMessageFactory()
    					.createDefaultReceipt(notify.getFrom() ,
    							Integer.toString(notify.getCallID()),
    							notify.getSequence());
		}else {
			receipt = this.dialog.getMessageFactory()
            			.createHttpInviteReceipt(notify.getFrom() ,
            					Integer.toString(notify.getCallID()),
            					notify.getSequence(),
            					this.context.getTransferFactory().getDefaultTransferLocalPort());
			
		}
		this.dialog.process(receipt);
		
		//检查是否发起会话的好友是否存在，如果不存在建立一个新飞信好友对象，并设置关系为陌生人关系
		FetionStore store = this.context.getFetionStore();
		Buddy buddy = null;
		synchronized(store){
			buddy = store.getBuddyByUri(notify.getFrom());
			if(buddy==null){
				buddy = new Buddy();
				buddy.setUri(notify.getFrom());
				store.addBuddy(buddy);
			}
		}
		
		//检查之前的会话中，是否有BasicChatDialog 如果有，关闭
		ChatDialog basicDialog = context.getDialogFactory().findChatDialog(buddy);
		if(basicDialog instanceof BasicChatDialog){
			basicDialog.closeDialog();	//关闭并从当前对话列表中移除
		}
		
		//和邀请的好友建立会话
    	final ChatDialog dialog = context.getDialogFactory().createChatDialog(notify);
    	Runnable r = new Runnable(){
			public void run(){
		    	//在另外一个线程里打开这个对话框
				try {
	                dialog.openDialog();
	              	logger.debug("Created and opened ChatDialog success - Dialog="+dialog);
                } catch (FetionException e) {
                	logger.warn("create ChatDialog by invite notify failed.", e);
                	//发生错误就关闭这个对话框
	                dialog.closeDialog();
                }
			}
		};
		this.context.getFetionExecutor().submitTask(r);
		
		//通知监听器收到了一个邀请
		ChatDialogProxy cd = this.context.getChatDialogProxyFactoy().create(buddy);
		this.tryFireNotifyEvent(new InviteReceivedEvent(cd));
    }

}
