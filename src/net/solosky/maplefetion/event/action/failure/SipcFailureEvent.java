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
 * Package  : net.solosky.maplefetion.event.action
 * File     : SuccessEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action.failure;

import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;

/**
 *
 * 飞信服务器返回的错误的未处理的回复信息
 * 如果出现这个事件，表明服务返回了错误信息，但目前客户端无法识别。
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class SipcFailureEvent extends FailureEvent
{

	/**
	 * 服务器原始的回复
	 */
	private SipcResponse response;
	
	/**
	 * @param type
	 */
	public SipcFailureEvent(FailureType type, SipcResponse response) 
	{
		super(type);
		this.response = response;  
	}

	/**
	 * 返回回复对象
	 * @return
	 */
	public SipcResponse getResponse() {
		return response;
	}

	/**
	 * 返回请求对象
	 * @return
	 */
	public SipcRequest getRequest() {
		return response.getRequest();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SipcFailureEvent [FailureType=" + getFailureType()+
				", response=" + response.getStatusCode()+"-"+response.getStatusMessage() + ", getEventType()="
				+ getEventType() 
				+ "]";
	}
	
	

}
