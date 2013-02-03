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
 * File     : ClientState.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-3-31
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion;

/**
 *
 *  客户端状态枚举
 *  当状态改变的时候，比如网络连接断开，客户端状态就会改变，用户可以在NotifyEventListener中监听ClientState事件来获得客户端状态改变
 *
 * @author solosky <solosky772@qq.com>
 */
public enum ClientState {
	/**
	 * 在线状态
	 */
	ONLINE,
	
	/**
	 * 登录状态
	 */
	LOGGING,
	
	/**
	 * 登录出错
	 */
	LOGIN_ERROR,
	
	/**
	 * 退出状态，初始状态
	 */
	LOGOUT,
	
	/**
	 * 其他客户端登录
	 */
	OTHER_LOGIN,
	
	/**
	 * 服务器关闭了连接
	 */
	DISCONNECTED,
	
	/**
	 * 网络连接出错
	 */
	CONNECTION_ERROR,
	
	/**
	 * 客户端内部错误
	 */
	SYSTEM_ERROR
}
