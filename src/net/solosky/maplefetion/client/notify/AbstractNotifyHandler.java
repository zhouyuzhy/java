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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.protocol.notify
 * File     : AbstractSIPNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-24
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.NotifyEventListener;
import net.solosky.maplefetion.client.NotifyHandler;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.NotifyEvent;

import org.apache.log4j.Logger;

/**
 *
 * 所有服务器发回的异步通知处理器的基类
 *
 * @author solosky <solosky772@qq.com> 
 */
public abstract class AbstractNotifyHandler implements NotifyHandler
{
	/**
	 * 飞信运行环境
	 */
	protected FetionContext context; 
	
	/**
	 * 飞信传输服务
	 */
	protected Dialog dialog;
	
	/**
	 * Logger
	 */
	protected Logger logger = Logger.getLogger(AbstractNotifyHandler.class);
	
	/**
	 * 设置飞信运行环境
	 * 这个函数仅在第一实例化对象时候调用，注入飞信运行环境
	 * @param context	客户端对象
	 */
	public void setContext(FetionContext context)
	{
		this.context = context;
	}
	
	/**
     * 设置对话对象
	 * @param dailog			当前对话对象
	 */
	public void setDailog(Dialog dialog)
	{
		this.dialog = dialog;
	}
	
	/**
	 * 尝试触发通知事件
	 * 捕获所有触发事件的异常，防止异常传递到最顶层而使系统错误
	 * @param event
	 */
	protected void tryFireNotifyEvent(NotifyEvent event)
	{
		logger.debug("FireNotifyEvent:"+event);
		NotifyEventListener listener = this.context.getNotifyEventListener();
		if(listener!=null){
			try {
				listener.fireEvent(event);
			} catch (Throwable e) {
				logger.warn("Fire NotifyEvent error.", e);
			}
		}
	}
}
