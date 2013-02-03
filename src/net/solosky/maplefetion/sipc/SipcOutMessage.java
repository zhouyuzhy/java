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
 * Package  : net.solosky.net.maplefetion.sipc
 * File     : SipcOutMessage.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;



/**
*
* 可以发出去的消息
* 
* 子类有SIPRequest和SIPReceipt
*
* @author solosky <solosky772@qq.com> 
*/
public abstract class SipcOutMessage extends SipcMessage
{

	/**
	 * 默认的构造函数
	 */
	public SipcOutMessage()
	{
		
	}
	
	
	/**
	 * 设置请求者
	 * @param from		请求者
	 */
	public void setFrom(String from)
	{
		this.getHeader(SipcHeader.FROM).setValue(from);
	}
	
	/**
	 * 设置全局请求序号
	 * @param callid	全局请求序号
	 */
	public void setCallID(int callid)
	{
		this.getHeader(SipcHeader.CALLID).setValue(Integer.toString(callid));
	}
	
	/**
	 * 设置请求序号
	 * @param sequence	请求序号
	 */
	public void setSequence(String sequence)
	{
		this.getHeader(SipcHeader.SEQUENCE).setValue(sequence);
	}
	
	/**
	 * 设置请求长度
	 * @param length	请求长度
	 */
	public void setLength(int length)
	{
		this.getHeader(SipcHeader.LENGTH).setValue(Integer.toString(length));
	}
}
