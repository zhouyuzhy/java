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
 * File     : FailureEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action;

import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;

/**
 *
 * 操作失败事件
 * 通常是服务器返回了其他结果
 *
 * @author solosky <solosky772@qq.com>
 */
public class FailureEvent extends ActionEvent
{
	/**
	 * 错误原因
	 */
	private FailureType failureType;
	
	/**
	 * 以一个回复构造失败事件
	 * @param response
	 */
	public FailureEvent(FailureType type)
	{
		this.failureType = type;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.event.ActionEvent#getType()
     */
    @Override
    public ActionEventType getEventType()
    {
	  return ActionEventType.FAILURE;
    }

	/**
	 * @return the failureType
	 */
	public FailureType getFailureType()
	{
		return failureType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "FailureEvent [failureType=" + failureType + ", EventType="
				+ getEventType() + "]";
	}
	
	
}
