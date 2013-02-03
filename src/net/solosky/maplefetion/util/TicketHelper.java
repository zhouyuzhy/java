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
 * Package  : net.solosky.maplefetion.util
 * File     : TicketHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-28
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.solosky.maplefetion.net.Port;

/**
 *
 * 进入聊天对话框的信息
 * 如 CS address="221.176.31.108:8080;221.176.31.108:443",credential="244180958.1770195007"
 * 
 * @author solosky <solosky772@qq.com>
 */
public class TicketHelper
{
	/**
	 * 连接端口列表
	 */
	private ArrayList<Port> portList;
	
	/**
	 * 验证字符
	 */
	private String credential;
	/**
	 * 
	 * @param ticket
	 * @throws ParseException 
	 */
	public TicketHelper(String ticket) throws ParseException
	{
		this.portList = new ArrayList<Port>();
		this.credential = null;
		this.parseTicket(ticket);
	}
	
	/**
	 * 解析Ticket
	 * @throws ParseException 
	 */
	public void parseTicket(String ticket) throws ParseException
	{
		//CS address="221.176.31.108:8080;221.176.31.108:443",credential="244180958.1770195007"
		//使用正则匹配
		Pattern pt = Pattern.compile("CS address=\"(.*?)\",credential=\"(.*?)\"");
		Matcher mc = pt.matcher(ticket);
		if(mc.matches()) {
			//解析地址和端口
			String addr = mc.group(1);
			String[] addrs = addr.split(";");
			for(int i=0; i<addrs.length; i++) {
				try {
	                this.portList.add(new Port(addrs[i]));
                } catch (UnknownHostException e) {
	                throw new ParseException("Unkown host - Port="+addrs[i]);
                }
			}
			//解析凭证
			this.credential = mc.group(2);
		}else {
			throw new ParseException("Parse ticket failed - ticket="+ticket);
		}
	}
	/**
     * @return the portList
     */
    public ArrayList<Port> getPortList()
    {
    	return portList;
    }
	/**
     * @return the credential
     */
    public String getCredential()
    {
    	return credential;
    }
	
	
}
