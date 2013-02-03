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
 * Package  : net.solosky.maplefetion.action.failure
 * File     : RequestFailtureEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action.failure;

import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;

/**
 *
 *
 * 请求失败事件，通常含有服务器返回的失败的原因
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class RequestFailureEvent extends FailureEvent{

	/**
	 * 请求失败的原因
	 */
	private String reason;
	
	/**
	 * 原因详细信息的URL
	 */
	private String reffer;
	/**
	 * @param type
	 */
	public RequestFailureEvent(FailureType type, String reason, String reffer) {
		super(type);
		this.reason = reason;
		this.reffer = reffer;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @return the reffer
	 */
	public String getReffer() {
		return reffer;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RequestFailureEvent [FailureType=" + getFailureType()
				+ ", reason=" + reason + ", reffer=" + reffer + "]";
	}
	
	
	
	
}
