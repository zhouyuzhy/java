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
 * Package  : net.solosky.maplefetion.event
 * File     : NotifyEventType.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event;

/**
 *
 * 通知事件类型
 *
 * @author solosky <solosky772@qq.com>
 */
public enum NotifyEventType {
	/**
	 * 登录状态发生变化
	 */
	LOGIN_STATE,
	
	/**
	 * 客户端状态发生变化
	 */
	CLIENT_STATE,
	
	/**
	 * 好友消息
	 */
	BUDDY_MESSAGE,
	
	/**
	 * 群消息
	 */
	GROUP_MESSAGE,
	
	/**
	 * 系统消息
	 */
	SYSTEM_MESSAGE,
	
	/**
	 * 添加好友请求
	 */
	BUDDY_APPLICAION,
	
	/**
	 * 对方回复了添加好友的请求
	 */
	BUDDY_CONFIRMED,
	
	/**
	 * 对方状态发生改变
	 */
	BUDDY_PRESENCE,	
	
	/**
	 * 接收到了一个邀请
	 */
	INVITE_RECEIVED,
	
	/**
	 * 操作需要验证
	 */
	IMAGE_VERIFY
	
}
