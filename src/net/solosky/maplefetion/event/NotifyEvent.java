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
 * File     : NotifyEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event;


/**
 *
 * 通知事件，通常是飞信客户端内部产生的事件
 * 来通知调用者一些事件，比如好友消息，好友申请等
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class NotifyEvent extends FetionEvent
{
	
	/**
	 * 默认构造函数
	 */
	public NotifyEvent()
	{
	}

	/**
	 * 返回通知事件类型
	 * @return
	 */
	public abstract NotifyEventType getEventType();
}
