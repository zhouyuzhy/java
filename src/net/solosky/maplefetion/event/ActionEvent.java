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
 * File     : ActionEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event;


/**
 *
 * 操作事件
 * 通常是用户发起一个操作，然后操作完成或者失败后会触发操作事件
 * 用户应该设置一个操作事件监听器来监听操作结果
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class ActionEvent extends FetionEvent
{

	/**
	 * 默认的构造函数
	 */
	public ActionEvent()
	{
	}
	
	
	/**
	 * 返回操作事件的类型
	 * @return
	 */
	public abstract ActionEventType getEventType();
}