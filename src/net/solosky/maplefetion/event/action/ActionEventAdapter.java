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
 * File     : ActionEventAdapter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-17
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.action;

import net.solosky.maplefetion.event.ActionEvent;

/**
 *
 * 操作事件适配器
 * 可以把事件适配为调用方法
 * 默认什么也不做，子类可以重载感兴趣的方法
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class ActionEventAdapter implements ActionEventListener
{

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.event.action.ActionEventListener#fireEevent(net.solosky.maplefetion.event.ActionEvent)
	 */
	@Override
	public void fireEevent(ActionEvent event)
	{
		switch(event.getEventType())
		{
			case SUCCESS:
				doSuccess((SuccessEvent) event);
				break;
				
			case FAILURE:
				doFailure((FailureEvent) event);
				break;
				
			case SYSTEM_ERROR:
				doSystemError((SystemErrorEvent) event);
				break;
				
			case TIMEOUT:
				doTimeout((TimeoutEvent) event);
				break;
				
			case TRANSFER_ERROR:
				doTransferError((TransferErrorEvent) event);
				break;
				
			default:
				break;
				
		}
	}
	
	/**
	 * 操作成功
	 * @param event
	 */
	protected void doSuccess(SuccessEvent event)
	{
	}
	
	/**
	 * 操作失败
	 * @param event
	 */
	protected void doFailure(FailureEvent event)
	{
	}
	
	/**
	 * 网络错误
	 * @param event
	 */
	protected void doTransferError(TransferErrorEvent event)
	{
	}
	
	/**
	 * 系统内部错误
	 * @param event
	 */
	protected void doSystemError(SystemErrorEvent event)
	{
	}
	
	/**
	 * 超时
	 * @param event
	 */
	protected void doTimeout(TimeoutEvent event)
	{
	}
	
}
