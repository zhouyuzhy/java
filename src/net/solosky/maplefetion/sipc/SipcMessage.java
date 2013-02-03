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
 * File     : SipcMessage.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.util.ConvertHelper;

/**
*
* SIP消息
* 这是一个抽象类，SIP发送和回复都是其子类
* 所有的请求和回复都是异步的
*
* @author solosky <solosky772@qq.com> 
*/
public abstract class SipcMessage
{
	
	/**
	 * SIP版本字符串
	 */
	public static String SIP_VERSION = "SIP-C/4.0";
	
	/**
	 * 消息头列表
	 */
	protected ArrayList<SipcHeader> headers;
	
	/**
	 * 消息正文
	 */
	protected SipcBody body;
	
	
	/**
	 * 默认构造函数
	 */
	public SipcMessage()
	{	
		headers = new ArrayList<SipcHeader>();
	}
	
	/**
	 * 返回一个消息头
	 * @param name	消息头值
	 * @return		消息头，如果有多个返回第一个符合的消息头
	 */
	public SipcHeader getHeader(String name)
	{
		Iterator<SipcHeader> it = headers.iterator();
		while(it.hasNext()) {
			SipcHeader header = it.next();
			if(header.getName()!=null&&header.getName().equals(name))
				return header;
		}
		return null;
	}
	
	/**
	 * 返回一个消息头
	 * @param name	消息头值
	 * @return		消息头，如果有多个返回第一个符合的消息头
	 */
	public List<SipcHeader> getHeaders(String name)
	{
		Iterator<SipcHeader> it = headers.iterator();
		ArrayList<SipcHeader> list = new ArrayList<SipcHeader>();
		while(it.hasNext()) {
			SipcHeader header = it.next();
			if(header.getName()!=null&&header.getName().equals(name)) {
				list.add(header);
			}
		}
		return list;
	}	
	
	/**
	 * 检查是否有给定名字的消息头
	 * @param name	消息头名
	 * @return		存在返回true不存在返回false
	 */
	public boolean hasHeader(String name)
	{
		Iterator<SipcHeader> it = headers.iterator();
		while(it.hasNext()) {
			SipcHeader header = it.next();
			if(header.getName().equals(name))
				return true;
		}
		return false;
	}
	
	/**
	 * 添加头部
	 * @param header	请求头
	 */
	public void addHeader(SipcHeader header)
	{
		this.headers.add(header);
	}
	
	/**
	 * 添加头部
	 * @param name		请求头名
	 * @param value		请求头值
	 */
	public void addHeader(String name, String value)
	{
		this.addHeader(new SipcHeader(name, value));
	}
	
	
	/**
	 * 移除一个请求头
	 * @param name
	 */
	public void removeHeader(String name)
	{
		SipcHeader header = this.getHeader(name);
		if(header!=null) {
			this.headers.remove(header);
		}
	}
	
	
	/**
	 * 返回所有的消息头集合
	 * @return		消息头集合
	 */
	public ArrayList<SipcHeader> getHeaders()
	{
		return headers;
	}
	
	/**
	 * 设置消息正文
	 * @param body	消息正文
	 */
	public void setBody(SipcBody body)
	{
		this.body = body;
	}
	
	/**
	 * 返回消息正文
	 * @return body		消息正文
	 */
	public SipcBody getBody()
	{
		return body;
	}
	
	/**
	 * 转化为可以发送的字符串序列
	 * @return			可发送的字符串序列
	 */
	public String toSendString()
	{
		StringBuffer buffer = new StringBuffer();
		
		//HeadLine
		buffer.append(this.toHeadLine());
		buffer.append("\r\n");
		
		//headers
		Iterator<SipcHeader> it = this.getHeaders().iterator();
		while(it.hasNext()) {
			buffer.append(it.next().toSendString());
			buffer.append("\r\n");
		}
		
		if(this.body!=null && !this.hasHeader(SipcHeader.LENGTH)) {
			int len =ConvertHelper.string2Byte(body.toSendString()).length;
			if(len>0)
				buffer.append("L: "+len+"\r\n");
		}
		
		buffer.append("\r\n");
		
		
		//body
		if(this.body!=null)
			buffer.append(body.toSendString());

		
		return buffer.toString();

	}
	
	/**
	 * 返回头部信息
	 */
	protected abstract String toHeadLine();
	
}