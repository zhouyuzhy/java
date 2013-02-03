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
 * File     : LiveV2ChatNotifyDispatcher.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-31
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dispatcher;

import net.solosky.maplefetion.ExceptionHandler;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcMethod;
import net.solosky.maplefetion.sipc.SipcNotify;

/**
 * 
 * 第二版本对话框通知分发器
 * 
 * @author solosky <solosky772@qq.com>
 */
public class LiveV2MessageDispatcher extends AbstractMessageDispatcher
{

	/**
	 * @param client
	 * @param dialog
	 * @param exceptionHandler
	 */
	public LiveV2MessageDispatcher(FetionContext client, Dialog dialog, ExceptionHandler exceptionHandler)
	{
		super(client, dialog, exceptionHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.solosky.maplefetion.client.dispatcher.AbstractNotifyDispatcher#
	 * findNotifyHandlerClass(net.solosky.maplefetion.sipc.SipcNotify)
	 */
	@Override
	protected String findNotifyHandlerClass(SipcNotify notify)
	{
		String method = notify.getMethod();
		String clazz = null;
		if (method.equals(SipcMethod.MESSAGE)) {
			clazz = "MessageNotifyHandler";
		} else if (method.equals(SipcMethod.BENOTIFY)) {
			//检查事件名字
			SipcHeader eventHeader = notify.getHeader(SipcHeader.EVENT);
			if (eventHeader == null || eventHeader.getValue() == null) {
				logger.warn("Unknown Notify event:[" + notify + "]");
				return null;
			}
			String event = notify.getHeader(SipcHeader.EVENT).getValue();
			
			//根据不同的事件名字设置不同的处理器Conversation
			if (event.equals("Conversation")) {
				clazz = "ConversationNotifyHandler";
			}else {
				//TODO 
			}
		} else if(method.equals(SipcMethod.OPTION)){
			clazz = "OptionNotifyHandler";
		}else {
			//TODO ..
		}

		return clazz != null ? "net.solosky.maplefetion.client.notify." + clazz : null;
	}

}
