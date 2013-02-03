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
 * File     : SendChatMessageResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import org.jdom.Element;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.event.action.success.SendChatMessageSuccessEvent;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.util.XMLHelper;

/**
 *
 * 发送消息的结果的回调函数
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class SendChatMessageResponseHandler extends AbstractResponseHandler
{

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public SendChatMessageResponseHandler(FetionContext context, Dialog dialog,
			ActionEventListener listener)
	{
		super(context, dialog, listener);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doActionOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doActionOK(SipcResponse response)
			throws FetionException
	{
		return new SendChatMessageSuccessEvent(response.getStatusCode());
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doSendSMSOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doSendSMSOK(SipcResponse response)
			throws FetionException
	{
		return new SendChatMessageSuccessEvent(response.getStatusCode());
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doNotFound(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doNotFound(SipcResponse response)
			throws FetionException
	{
		return new FailureEvent(FailureType.USER_NOT_FOUND);
	}

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doForbidden(net.solosky.maplefetion.sipc.SipcResponse)
     */
    @Override
    protected ActionEvent doForbidden(SipcResponse response)
            throws FetionException
    {
    	Element root =  XMLHelper.build(response.getBody().toSendString());
    	Element error = XMLHelper.find(root, "/results/error");
    	if(error!=null) {
        	String reason = error.getAttributeValue("reason");
        	if("receiver is in the BlackList of sender".equals(reason)) {
        		return new FailureEvent(FailureType.BUDDY_BLOCKED);
        	}else {
        		logger.warn("Uknown SendChatMessage fail reason:"+reason);
        		return super.doForbidden(response);
        	}
    	}else {
    		return super.doForbidden(response);	
    	}
    }
}
