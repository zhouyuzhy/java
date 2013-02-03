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
 * Package  : net.solosky.maplefetion.client.response
 * File     : DefaultResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.action.ActionEventListener;

/**
 * 
 * 默认回复处理
 * 
 * @author solosky <solosky772@qq.com>
 */
public class DefaultResponseHandler extends AbstractResponseHandler
{

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public DefaultResponseHandler(FetionContext context, Dialog dialog,
			ActionEventListener listener)
	{
		super(context, dialog, listener);
	}
	
	public DefaultResponseHandler(ActionEventListener listener)
	{
		super(null, null, listener);
	}
}
