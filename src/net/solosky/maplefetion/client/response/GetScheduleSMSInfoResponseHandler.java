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
 * File     : GetScheduleSMSInfoResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-25
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.UriHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 获取定时短信的详细信息处理器
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class GetScheduleSMSInfoResponseHandler extends AbstractResponseHandler
{

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public GetScheduleSMSInfoResponseHandler(FetionContext context,
			Dialog dialog, ActionEventListener listener)
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
		Element root = XMLHelper.build(response.getBody().toSendString());
		List sclist = XMLHelper.findAll(root, "/results/schedule-sms-list/*schedule-sms");
		Iterator it = sclist.iterator();
		FetionStore store = this.context.getFetionStore();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d H:m:s");
		df.setTimeZone(TimeZone.getTimeZone("GMT 0"));
		while(it.hasNext()){
			Element e = (Element) it.next();
			int scId = Integer.parseInt(e.getAttributeValue("id"));
			ScheduleSMS sc = store.getScheduleSMS(scId);
			if(sc!=null){
				try {
					BeanHelper.setValue(sc, "sendDate", df.parse(e.getAttributeValue("send-time")));	//发送时间
				} catch (ParseException e1) {
					throw new net.solosky.maplefetion.util.ParseException("Parse scheduleSMS send-time failed. "+e.getAttributeValue("sendTime"));
				}
				
				BeanHelper.setValue(sc, "message", new Message(e.getChild("message").getText()));	//消息内容
				
				ArrayList<Buddy> recieverList = new ArrayList<Buddy>();
				List recvList = XMLHelper.findAll(e, "/schedule-sms/receivers/*receiver");		//接收者
				Iterator rit =recvList.iterator();
				while(rit.hasNext()){
					Element el = (Element) rit.next();
					String uri = el.getAttributeValue("uri");
					Buddy buddy = store.getBuddyByUri(uri);
					if(buddy!=null){
						recieverList.add(buddy);
					}else if(context.getFetionUser().getUri().equals(uri)){		//可能是用户自己
						recieverList.add(context.getFetionUser());
					}else{
						buddy = UriHelper.createBuddy(uri);
						BeanHelper.setValue(buddy, "relation", Relation.STRANGER);
						recieverList.add(buddy);
						store.addBuddy(buddy);
					}
				}
				BeanHelper.setValue(sc, "receiverList", recieverList);
				
			}
		}
		return super.doActionOK(response);
	}
	
	
	

}
