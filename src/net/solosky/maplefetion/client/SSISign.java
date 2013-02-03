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
 * Package  : net.solosky.maplefetion.client
 * File     : SSISign.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-9
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.util.LocaleSetting;

/**
 *
 *SSI登陆接口
 *
 * @author solosky <solosky772@qq.com>
 */
public interface SSISign
{
	/**
	 * 尝试登录
	 * param user 用户对象
	 * @return		状态值
	 */
	public LoginState signIn(User user);
	
	/**
	 * 以验证码登录
	 * @param user	用户对象
	 * @param img	验证码
	 * @return	状态值
	 */
	public LoginState signIn(User user, VerifyImage img);
	
	/**
	 * SSI登出，这个方法可以忽略掉
	 * @param user
	 * @return
	 * @throws TransferException
	 */
	public LoginState signOut(User user);
	
	
	/**
	 * 设置区域化的配置
	 * @param localeSetting
	 */
	public void setLocaleSetting(LocaleSetting localeSetting);
	
	/**
	 * 设置飞信上下文
	 * @param context
	 */
	public void setFetionContext(FetionContext context);
	

}