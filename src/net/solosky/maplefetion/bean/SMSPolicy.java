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
 * File     : SMSPolicy.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-13
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.solosky.maplefetion.util.ParseException;


/**
 *
 * 用户设置的短信策略
 * 
 * 飞信用户可以设置不接受飞信发送的短信，也可以设置在几个小时之内不接受短信
 * 字符串形式形如：1.00:00:00	 0.0:0:0 36500.00:00:00，其含义是在多少时间内不接受短信
 * D.H:M:S
 * 
 *
 * @author solosky <solosky772@qq.com>
 */
public class SMSPolicy
{
	/**
	 * 时间格式对象
	 */
	private static final  SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("D.H:m:s") ;
	/**
	 * 拒绝接受短信的时间点，小于这个时间就不接受短信
	 */
	public Date refusedSMSDate;
	/**
	 * 构造函数
	 * @param sms	短信策略字串
	 * @throws ParseException
	 */
	public SMSPolicy(String sms) throws ParseException
	{
		this.parse(sms);
	}
	
	/**
	 * 默认构造函数
	 */
	public SMSPolicy()
	{
		this.refusedSMSDate = new Date();
	}
	
	/**
	 * 当前好友是否短信在线
	 */
	public boolean isSMSOnline()
	{
		return this.refusedSMSDate.getTime() <= System.currentTimeMillis();
	}
	
	/**
	 * 解析短信策略字串
	 * @param sms 短信策略字串
	 * @throws ParseException 
	 */
	public void parse(String sms) throws ParseException
	{
		try {
			synchronized (DATE_FORMAT) {
				Date d = DATE_FORMAT.parse(sms);
		        this.refusedSMSDate = new Date(d.getTime()+System.currentTimeMillis());
			}
        } catch (java.text.ParseException e) {
        	this.refusedSMSDate = new Date();
        	throw new ParseException("Cannot parse SMSPolicy:"+sms);
        }
	}
	
	/**
	 * 转化为字符串形式
	 */
	public String toString()
	{
		synchronized (DATE_FORMAT) {
			return DATE_FORMAT.format(new Date(this.refusedSMSDate.getTime()-System.currentTimeMillis()));
		}
	}
}
