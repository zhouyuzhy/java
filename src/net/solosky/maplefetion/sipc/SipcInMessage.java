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
 * File     : SipcInMessage.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;

/**
*
* 可以接受的消息
* 
* 子类有SipcResponse和SipcNotify
*
* @author solosky <solosky772@qq.com> 
*/
public abstract class SipcInMessage extends SipcMessage
{	
	/**
	 * 返回请求者
	 * @return		请求者
	 */
	public String getFrom()
	{
		return this.getHeader(SipcHeader.FROM).getValue();
	}
	
	/**
	 * 返回全局请求编号
	 * @return		全局请求编号
	 */
	public int getCallID()
	{
		SipcHeader header = this.getHeader(SipcHeader.CALLID);
		return header==null?-1:Integer.parseInt(header.getValue());
	}
	
	/**
	 * 返回请求序号
	 * @return		请求序号
	 */
	public String getSequence() 
	{
		SipcHeader header = this.getHeader(SipcHeader.SEQUENCE);
		return header==null?null:header.getValue();
	}
	
	/**
	 * 返回正文长度
	 * @return		正文长度，如果没有消息头返回-1
	 */
	public int getContentLength()
	{
		//如果没有长度信息，默认为0。
		//长度有可能是这样的内容：64240;p=0
		//前面的数字是这个包的长度，后面的p数字是这个包的起始位置
		SipcHeader header = this.getHeader(SipcHeader.LENGTH);
		if(header!=null && header.getValue()!=null){
			String value = header.getValue();
			if(value.length()<1){
				return -1;
			}else if(value.indexOf(';')!=-1){
				return Integer.parseInt(value.substring(0, value.indexOf(';')));
			}else{
				return Integer.parseInt(value);
			}
		}else{
			return -1;
		}
	}
}