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
 * Package  : net.solosky.maplefetion.event.action.success
 * File     : SendChatMessageSuccessEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action.success;

import net.solosky.maplefetion.event.action.SuccessEvent;
import net.solosky.maplefetion.sipc.SipcStatus;

/**
 *
 * 发送消息成功
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class SendChatMessageSuccessEvent extends SuccessEvent
{
	/**
	 * 服务器发回消息的返回值
	 */
	private int sipcStatus;

	/**
	 * @param sipcStatus
	 */
	public SendChatMessageSuccessEvent(int sipcStatus)
	{
		super();
		this.sipcStatus = sipcStatus;
	}
	
	
	/**
	 * 是否发送至手机
	 * @return
	 */
	public boolean isSendToMobile()
	{
		return this.sipcStatus == SipcStatus.SEND_SMS_OK;
	}
	
	/**
	 * 是否发送至客户端
	 * @return
	 */
	public boolean isSendToClient()
	{
		return this.sipcStatus == SipcStatus.ACTION_OK;
	}
}
