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
 * File     : AgreeApplicationResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 同意对方添加好友请求
 *
 * @author solosky <solosky772@qq.com>
 */
public class AgreeApplicationResponseHandler extends AbstractResponseHandler
{

	/**
     * @param client
     * @param dialog
     * @param listener
     */
    public AgreeApplicationResponseHandler(FetionContext client, Dialog dialog, ActionEventListener listener)
    {
	    super(client, dialog, listener);
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doActionOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doActionOK(SipcResponse response)
			throws FetionException
	{
		Element root = XMLHelper.build(response.getBody().toSendString());
		Element element = XMLHelper.find(root, "/results/contacts/buddies/buddy");
		if(element!=null && element.getAttributeValue("user-id")!=null) {
			Buddy buddy = this.context.getFetionStore().getBuddyByUserId(Integer.parseInt(element.getAttributeValue("user-id")));
			BeanHelper.toBean(Buddy.class, buddy, element);
			BeanHelper.setValue(buddy, "relation", Relation.BUDDY);
			context.getFetionStore().flushBuddy(buddy);
		}
		Element contacts = XMLHelper.find(root, "/result/contacts");
		if(contacts.getAttributeValue("version")!=null) {
			int version = Integer.parseInt(contacts.getAttributeValue("version"));
			context.getFetionStore().getStoreVersion().setContactVersion(version);
			context.getFetionUser().getStoreVersion().setContactVersion(version);
			context.getFetionStore().flushStoreVersion(context.getFetionUser().getStoreVersion());
		}
		
		return super.doActionOK(response);
	}
    
    

}
