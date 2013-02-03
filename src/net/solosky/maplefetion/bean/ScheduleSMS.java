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
 * Package  : net.solosky.maplefetion.bean
 * File     : ScheduleSMS.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-25
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

import java.util.Collection;
import java.util.Date;

/**
 *
 * 定时短信对象
 * 
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class ScheduleSMS
{
	/**
	 * 定时短信ID
	 */
	private int id;
	
	/**
	 * 发送时间
	 */
	private Date sendDate;
	
	/**
	 * 消息内容
	 */
	private Message message;
	
	/**
	 * 接受者列表
	 */
	private Collection<Buddy> receiverList;
	
	/**
	 * 默认的构造函数
	 */
	public ScheduleSMS()
	{
		this.id = -1;
		this.message = null;
		this.sendDate = null;
		this.receiverList = null;
	}


	/**
	 * 构造函数
	 * @param id			定时短信ＩＤ 
	 * @param message		定时短信消息
	 * @param sendDate		发送时间	
	 * @param reciverList	接受者列表
	 */
	public ScheduleSMS(int id, Message message, Date sendDate,
			Collection<Buddy> reciverList)
	{
		super();
		this.id = id;
		this.message = message;
		this.sendDate = sendDate;
		this.receiverList = reciverList;
	}


	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @return the sendDate
	 */
	public Date getSendDate()
	{
		return sendDate;
	}

	/**
	 * @return the message
	 */
	public Message getMessage()
	{
		return message;
	}

	/**
	 * @return the reciverList
	 */
	public Collection<Buddy> getReciverList()
	{
		return receiverList;
	}

	/**
     * @return the receiverList
     */
    public Collection<Buddy> getReceiverList()
    {
    	return receiverList;
    }


	/**
     * @param receiverList the receiverList to set
     */
    public void setReceiverList(Collection<Buddy> receiverList)
    {
    	this.receiverList = receiverList;
    }


	/**
     * @param id the id to set
     */
    public void setId(int id)
    {
    	this.id = id;
    }


	/**
     * @param sendDate the sendDate to set
     */
    public void setSendDate(Date sendDate)
    {
    	this.sendDate = sendDate;
    }


	/**
     * @param message the message to set
     */
    public void setMessage(Message message)
    {
    	this.message = message;
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ScheduleSMS [id=" + id + ", message=" + message + ", sendDate="
				+ sendDate + ", receiverList=" + receiverList + "]";
	}
}
