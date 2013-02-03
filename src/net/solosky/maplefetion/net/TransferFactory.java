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
 * Package  : net.solosky.net.maplefetion.net
 * File     : TransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import net.solosky.maplefetion.FetionContext;


/**
*
*  传输连接工厂接口
*
* @author solosky <solosky772@qq.com> 
*/
public interface TransferFactory
{
	
	/**
	 * 建立传输对象
	 * @param Port		连接信息
	 * @return
	 * @throws Exception
	 */
	public Transfer createTransfer(Port port) throws TransferException;
	
	/**
	 * 创建默认的传输对象
	 * @return
	 * @throws TransferException
	 */
	public Transfer createDefaultTransfer() throws TransferException;
	
	/**
	 * 返回默认传输对象的本地端口
	 * @return
	 * @throws TransferException
	 */
	public Port getDefaultTransferLocalPort();
	
	/**
	 * 设置飞信上下文
	 * @param context
	 */
	public void setFetionContext(FetionContext context);
	
	/**
	 * 建立这个连接工厂
	 */
	public void openFactory();
	
	/**
	 * 关闭这个连接工厂
	 */
	public void closeFactory();
	
	/**
	 * 是否支持多个独立的链接
	 * 如果支持多个独立的链接，客户端会采用LiveV2ChatDialog和在线好友建立会话， 如TCP,Mina等
	 * 如果不支持多个独立的链接，客户端就会采用LiveV1ChatDialog和在线好友建立会话	如Http传输方式
	 * @return
	 */
	public boolean isMutiConnectionSupported();
}