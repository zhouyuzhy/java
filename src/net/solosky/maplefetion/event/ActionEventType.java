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
 * File     : ActionEventType.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event;

/**
 *
 * 操作事件类型
 *
 * @author solosky <solosky772@qq.com>
 */
public enum ActionEventType {
	/**
	 * 操作成功
	 */
	SUCCESS,
	
	/**
	 * 操作失败
	 */
	FAILURE,
	
	/**
	 * 网络错误
	 */
	TRANSFER_ERROR,
	/**
	 * 操作超时
	 */
	TIMEOUT,
	
	/**
	 * 系统错误
	 */
	SYSTEM_ERROR,
}
