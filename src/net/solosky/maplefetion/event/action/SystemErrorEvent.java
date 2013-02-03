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
 * Package  : net.solosky.maplefetion.event.action
 * File     : SystemErrorEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action;

import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;

/**
 *
 * 系统错误事件
 *
 * @author solosky <solosky772@qq.com>
 */
public class SystemErrorEvent extends ActionEvent
{
	/**
	 * 回复异常
	 */
	private Throwable cause;
	

	/**
	 * 
	 * @param cause
	 */
	public SystemErrorEvent(Throwable cause)
	{
		this.cause    = cause;
	}
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.event.ActionEvent#getType()
     */
    @Override
    public ActionEventType getEventType()
    {
    	return ActionEventType.SYSTEM_ERROR;
    }
	/**
	 * @return the cause
	 */
	public Throwable getCause()
	{
		return cause;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "SystemErrorEvent [cause=" + cause + ", EventType="
				+ getEventType() + "]";
	}
    
}
