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
 * File     : GetGroupListResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-24
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Group;
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
 *
 * @author solosky <solosky772@qq.com>
 */
public class GetGroupListResponseHandler extends AbstractResponseHandler
{

	/**
     * @param client
     * @param dialog
     * @param listener
     */
    public GetGroupListResponseHandler(FetionContext client, Dialog dialog,
            ActionEventListener listener)
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
		//群列表版本
		Element root = XMLHelper.build(response.getBody().toSendString());
		Element v = XMLHelper.find(root, "/results/group-list");
		int groupVersion = Integer.parseInt(v.getAttributeValue("version"));
		this.context.getFetionUser().getStoreVersion().setGroupVersion(groupVersion);
		//版本不一致
		if(this.context.getFetionStore().getStoreVersion().getGroupVersion()!=groupVersion){
			//解析群列表
			this.context.getFetionStore().clearGroupList();
			List groupList = XMLHelper.findAll(root, "/results/group-list/*group");
			Iterator it = groupList.iterator();
			FetionStore store = this.context.getFetionStore();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				Group group = new Group();
				BeanHelper.setValue(group, "uri", e.getAttributeValue("uri"));
				//group.setIdentity( Integer.parseInt(e.getAttributeValue("identity")));
				store.addGroup(group);
			}
		}
		return super.doActionOK(response);
	}
    
    
}
