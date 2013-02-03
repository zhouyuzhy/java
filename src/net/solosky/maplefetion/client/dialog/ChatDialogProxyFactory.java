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
 * Package  : net.solosky.maplefetion.client.dialog
 * File     : ChatProxyDialogFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-8
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.Hashtable;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.Buddy;

/**
 *
 * 聊天代理对话工厂
 * 
 * 管理金额创建代理工厂
 *
 * @author solosky <solosky772@qq.com>
 */
public class ChatDialogProxyFactory
{
	/**
	 * 飞信上下文
	 */
	private FetionContext context;
	
	/**
	 * 聊天对话代理缓存
	 */
	private Hashtable<Buddy, ChatDialogProxy> proxyTable;
	
	/**
	 * 构造函数
	 */
	public ChatDialogProxyFactory(FetionContext context)
	{
		this.context    = context;
		this.proxyTable = new Hashtable<Buddy, ChatDialogProxy>();
	}
	
	/**
	 * 建立聊天对话代理
	 * @param buddy		开始对话的好友
	 * @return		聊天对话代理
	 * @throws DialogException	建立失败抛出
	 */
	public synchronized ChatDialogProxy create(Buddy buddy) throws DialogException
	{
		if(this.proxyTable.containsKey(buddy)) {
			return this.proxyTable.get(buddy);
		}else {
			ChatDialogProxy proxy = new ChatDialogProxy(buddy, context);
			this.proxyTable.put(buddy, proxy);
			return proxy;
		}
	}
	
	/**
	 * 关闭一个对话代理对象
	 * @param proxy
	 */
	public synchronized void close(ChatDialogProxy proxy)
	{
		if(proxy.getState()!=DialogState.CLOSED) {
			proxy.closeDialog();
		}
		if(this.proxyTable.containsKey(proxy.getMainBuddy())) {
			this.proxyTable.remove(proxy.getMainBuddy());
		}
	}
}
