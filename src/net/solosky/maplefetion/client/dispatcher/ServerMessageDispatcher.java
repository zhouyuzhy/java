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
 * Package  : net.solosky.maplefetion.client.dispatcher
 * File     : ServerNotifyDispatcher.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-27
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dispatcher;

import net.solosky.maplefetion.ExceptionHandler;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcMethod;
import net.solosky.maplefetion.sipc.SipcNotify;

import org.apache.log4j.Logger;

/**
 *
 * 这个通知处理器处理飞信服务器发回的通知
 *
 * @author solosky <solosky772@qq.com>
 */
public class ServerMessageDispatcher extends AbstractMessageDispatcher
{

	private static Logger logger = Logger.getLogger(ServerMessageDispatcher.class);
	
	/**
     * @param client
     * @param dialog
     * @param exceptionHandler
     */
    public ServerMessageDispatcher(FetionContext client, Dialog dialog, ExceptionHandler exceptionHandler)
    {
	    super(client, dialog, exceptionHandler);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dispatcher.AbstractNotifyDispatcher#findNotifyHandlerClass(net.solosky.maplefetion.sipc.SipcNotify)
     */
    @Override
    protected String findNotifyHandlerClass(SipcNotify notify)
    {
    	if(notify==null)	return null;
		String method = notify.getMethod();
		if(method==null) {
			logger.warn("Unknown Notify method:["+notify+"]");
		}
		String clazz = null;
		if (method.equals(SipcMethod.BENOTIFY)) {
			SipcHeader eventHeader = notify.getHeader(SipcHeader.EVENT);
			if(eventHeader==null || eventHeader.getValue()==null) {
				logger.warn("Unknown Notify event:["+notify+"]");
				return null;
			}
			String event = notify.getHeader(SipcHeader.EVENT).getValue();
			if (event.equals("PresenceV4")) {
				clazz = "BuddyPresenceNotifyHandler";					//好友在线状态改变
			} else if(event.equals("PGPresence")) {
				clazz = "GroupPresenceNotifyHandler";					//群成员状态改变
			} else if(event.equals("PGGroup")) {
				clazz = "GroupNotifyHandler";							//群状态改变
			}else if (event.equals("contact")) {
				clazz = "ContactNotifyHandler";						//联系人消息，这下面还有子请求，如添加好友请求，延迟好友资料返回
			}else if(event.equals("Conversation")){
				clazz ="ConversationNotifyHandler";					//对话消息，表示好友已经进入了对话里面
			}else if(event.equals("registration")){
				clazz ="RegistrationNotifyHandler";					//注册消息，这里只有一个，用户在其他地方登陆
			}else if(event.equals("compact")) {
																		//列表改变事件 TODO ..
			}else if(event.equals("permission")) {
				clazz = "PermissionNotifyHandler";						//权限改变事件
			}else if(event.equals("SystemNotifyV4")){
				clazz = "SystemNotifyHandler";							//系统通知,啥东西。。 TODO ..
			}else if(event.equals("SyncUserInfoV4")){
				clazz = "SyncUserInfoNotifyHandler";		
			}else{}
		}else if(method.equals(SipcMethod.MESSAGE)) {
			clazz = "MessageNotifyHandler";							//消息，这里包括系统消息和好友消息
		}else if(method.equals(SipcMethod.INVATE)) {
			clazz = "InviteBuddyNotifyHandler";						//被邀请进入对话
		}else if(method.equals(SipcMethod.INFO)) {
			clazz = "FetionShowNotifyHandler";							//飞信秀，可能会有其他的
		}else if(method.equals(SipcMethod.ACK)) {
			clazz = "AckNotifyHandler";
		}else if(method.equals(SipcMethod.OPTION)) {
			clazz = "OptionNotifyHandler";
		}else if(method.equals(SipcMethod.BYE)){
			clazz = "ByeNotifyHandler";
		}
		// TODO ..其他通知。。。
		
		return clazz!=null ? "net.solosky.maplefetion.client.notify."+clazz : null;
    }
}
