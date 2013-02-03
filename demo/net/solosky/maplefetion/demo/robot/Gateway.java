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
 * Project  : MapleSMS
 * Package  : net.solosky.maplesms
 * File     : Gateway.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot;

/**
 *
 * 消息网关
 * 所有实现这个接口的程序都可以注册到机器人程序中
 *
 * @author solosky <solosky772@qq.com>
 */
public interface Gateway
{
	/**
	 * 发送短消息
	 * @param uri
	 * @param msg
	 */
	public void sendSMS(String uri, String msg);
	
	/**
	 * 设置消息监听器
	 * @param listener
	 */
	public void setSMSListener(SMSListener listener);
	
	/**
	 * 登录
	 */
	public void login();
	
	/**
	 * 退出
	 */
	public void logout();
	
	/**
	 * 返回可以显示的网关名称
	 * @return
	 */
	public String getName();
}
