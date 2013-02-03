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
 * Package  : net.solosky.maplefetion
 * File     : FetionContext.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-24
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion;

import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.client.dialog.ChatDialogProxyFactory;
import net.solosky.maplefetion.client.dialog.DialogFactory;
import net.solosky.maplefetion.net.TransferFactory;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.FetionExecutor;
import net.solosky.maplefetion.util.FetionTimer;
import net.solosky.maplefetion.util.LocaleSetting;

/**
 *
 * 飞信运行上下文 相当于一个导航树，便于子模块可以获得飞信运行环境时的资源
 *
 * @author solosky <solosky772@qq.com>
 */
public interface FetionContext
{

	/**
	 * 返回对话框工厂
	 * @return
	 */
	public DialogFactory getDialogFactory();
	
	/**
	 * 返回聊天对话代理工厂
	 * @return
	 */
	public ChatDialogProxyFactory getChatDialogProxyFactoy();

	/**
	 * 返回传输工厂
	 * @return
	 */
	public TransferFactory getTransferFactory();

	/**
	 * 返回单线程执行器
	 * @return
	 */
	public FetionExecutor getFetionExecutor();

	/**
	 * 返回全局的定时器
	 * @return
	 */
	public FetionTimer getFetionTimer();

	/**
	 * 返回飞信用户
	 * @return
	 */
	public User getFetionUser();

	/**
	 * 返回存储对象
	 */
	public FetionStore getFetionStore();

	/**
	 * 返回通知监听器
	 * @return the notifyListener
	 */
	public NotifyEventListener getNotifyEventListener();

	/**
	 * 设置客户端状态
	 */
	public void updateState(ClientState state);

	/**
	 * 返回客户端状态
	 */
	public ClientState getState();

	/**
	 * 返回区域化配置
	 */
	public LocaleSetting getLocaleSetting();
	
	/**
	 * 处理不可恢复的异常的回调方法
	 * 通常这个方法是为Client处理不可恢复的异常准备的
	 * @param exception
	 */
	public void handleException(FetionException exception);

}