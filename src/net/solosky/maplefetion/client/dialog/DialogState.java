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
 * File     : DialogState.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-3-31
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

/**
 * 
 * 对话状态枚举
 *
 * @author solosky <solosky772@qq.com>
 */
public enum DialogState {
	
	/**
	 * 刚刚创建好对话
	 */
	CREATED,
	
	/**
	 * 对话正在打开
	 */
	OPENNING,
	
	/**
	 * 打开对话失败
	 */
	FAILED,	
	
	/**
	 * 对话框已经打开
	 */
	OPENED,	
	
	/**
	 * 对话框已经关闭
	 */
	CLOSED
}
