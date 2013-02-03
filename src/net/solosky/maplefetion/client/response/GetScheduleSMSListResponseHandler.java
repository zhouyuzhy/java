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
 * File     : GetScheduleSMSListResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-25
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 获取定时短信列表的回复处理
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class GetScheduleSMSListResponseHandler extends AbstractResponseHandler
{

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public GetScheduleSMSListResponseHandler(FetionContext context,
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
		
		synchronized (store) {
			while(it.hasNext()){
				Element e = (Element) it.next();
				//这里的 定时短信只有ID，如果需要定时短信的详细信息还需要发出另外的请求
				//真搞不懂为什么不把定时短信的详细直接返回，非得要发另外一个请求，很无语。。
				ScheduleSMS sc = new ScheduleSMS(Integer.parseInt(e.getAttributeValue("id")), null, null, null);
				store.addScheduleSMS(sc);
			}
        }
		
		return super.doActionOK(response);
	}
}
