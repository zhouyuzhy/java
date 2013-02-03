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
 * File     : CreateScheduleSMSResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-26
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 创建定时短信的回复处理器
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class CreateScheduleSMSResponseHandler extends  AbstractResponseHandler
{
	/**
	 * 创建的定时短信对象
	 */
	private ScheduleSMS scheduleSMS;

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public CreateScheduleSMSResponseHandler(FetionContext context,
			Dialog dialog, ActionEventListener listener, ScheduleSMS scheduleSMS)
	{
		super(context, dialog, listener);
		this.scheduleSMS = scheduleSMS;
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doActionOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doActionOK(SipcResponse response)
			throws FetionException
	{
		Element root = XMLHelper.build(response.getBody().toSendString());
		Element scel = XMLHelper.find(root, "/results/schedule-sms");
		
		if(scel!=null){
			int scId = Integer.parseInt(scel.getAttributeValue("id"));
			BeanHelper.setValue(this.scheduleSMS, "id", scId);
			this.context.getFetionStore().addScheduleSMS(this.scheduleSMS);
		}
		
		Element sclist = XMLHelper.find(root, "/results/schedule-sms-list");
		if(sclist!=null){
			int ver = Integer.parseInt(sclist.getAttributeValue("version"));
			this.context.getFetionStore().getStoreVersion().setScheduleSMSVersion(ver);
		}
		
		return super.doActionOK(response);
	}

	@Override
	protected ActionEvent doBusyHere(SipcResponse response) throws FetionException {
		Element root = XMLHelper.build(response.getBody().toSendString());
		Element error = XMLHelper.find(root, "/results/error");
		String reason = error.getAttributeValue("reason");
		if(reason!=null){
			if(reason.equals("sms-send-time-range-out")){
				return new FailureEvent(FailureType.INVALID_SEND_DATE);
			}else{
				logger.warn("Unkown createScheduleSMS error reason:"+reason);
				return super.doBusyHere(response);
			}
		}else{
			return super.doBusyHere(response);
		}
	}
	
	
}
