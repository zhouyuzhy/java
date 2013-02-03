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
 * File     : SipcBody.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;

/**
*
* SIP请求或者回复正文
* @author solosky <solosky772@qq.com> 
*/
public class SipcBody
{
	/**
	 * 请求正文
	 */
	private String body;
	
	/**
	 * 构造函数
	 * @param body		请求正文
	 */
	public SipcBody(String body)
	{
		this.body = body;
	}
	/**
	 * 转化为可以发送的字符串序列
	 * @return		可发送的字符串序列
	 */
	public String toSendString()
	{
		return this.body!=null ? this.body : "" ;
	}
	
	/**
	 * 正文长度
	 * @return
	 */
	public int getLength()
	{
		return this.body!=null? this.body.length() : 0 ;
	}
}