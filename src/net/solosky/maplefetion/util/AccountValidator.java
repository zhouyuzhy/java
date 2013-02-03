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
 * File     : AccountValidator.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-19
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 验证用户输入的内容是否是合法的飞信用户账号
 * 
 * 飞信提供了三种账号登陆：手机号，飞信号，以及在2010中提供的邮箱登陆
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class AccountValidator
{
	/**
	 * 需要验证的账号
	 */
	private String account;
	
	/**
	 * 以一个账号字符串构造
	 * @param account
	 */
	public AccountValidator(String account)
	{
		this.account = account;
	}
	
	/**
	 * 验证是否是合法的手机号
	 * @return
	 */
	public boolean isValidMobile()
	{
		try {
			long mobile = Long.parseLong(this.account);
			return validateMobile(mobile);
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	/**
	 * 验证是否是合法的飞信号
	 * @return
	 */
	public boolean isValidFetionId()
	{
		try {
			int fetionId = Integer.parseInt(this.account);
			return validateFetionId(fetionId);
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * 验证是否是有效的手机号码
	 * @return
	 */
	public boolean isValidEmail()
	{
		return validateEmail(this.account);
	}
	
	/**
	 * 返回手机号码
	 * @return
	 */
	public long getMobile()
	{
		try {
			return Long.parseLong(this.account);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 返回飞信号码
	 * @return
	 */
	public int getFetionId()
	{
		try {
			return Integer.parseInt(this.account);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	/**
	 * 返回Email
	 * @return
	 */
	public String getEmail()
	{
		return this.account;
	}
	
	
	/**
	 * 判断手机号是否合法
	 * 目前仅判断了是否是以下号码段1340-1348，135-139，150-152, 187-188, 852(HK), 147, 182
	 * @param mobile	手机号码
	 * @return	是否合法
	 */
	public static boolean validateMobile(long mobile)
	{
		if(mobile>=13400000000L && mobile<=13489999999L){	//1340-1348
			return true;
		}else if(mobile>=13500000000L && mobile<=13999999999L){	//135-139
			return true;
		}else if(mobile>=14700000000L && mobile<=14799999999L){	//147
			return true;
		}else if(mobile>=15000000000L && mobile<=15299999999L){	//150-152
			return true;
		}else if(mobile>=15700000000L && mobile<=15999999999L){	//157-159
			return true;
		}else if(mobile>=18200000000L && mobile<=18299999999L){	//182
			return true;
		}else if(mobile>=18300000000L && mobile<=18399999999L){	//182
			return true;
		}else if(mobile>=18700000000L && mobile<=18899999999L){	//187-188
			return true;
		}else if(mobile>=85200000000L && mobile<=85299999999L){	//852 HK
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * 是否是飞信号码
	 * 这个判断很简单，就是如果给的号码在100000000-999999999就是有效的飞信号码
	 * @param fetionId
	 * @return
	 */
	public static boolean validateFetionId(int fetionId)
	{
		if(fetionId>100000000 && fetionId<999999999){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 是否是有效的Email
	 */
	public static boolean validateEmail(String email)
	{
		Pattern pt = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher mc = pt.matcher(email);
		return mc.matches();
	}
}
