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
 * File     : SetPersonalInfoResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : SetPersonalInfoResponseHandler.java
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import org.jdom.Element;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

/**
 * @author solosky <solosky772@qq.com>
 *
 */
public class SetPersonalInfoResponseHandler extends AbstractResponseHandler {

	/**
	 * @param context
	 * @param dialog
	 * @param listener
	 */
	public SetPersonalInfoResponseHandler(FetionContext context, Dialog dialog,
			ActionEventListener listener) {
		super(context, dialog, listener);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doActionOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doActionOK(SipcResponse response)
			throws FetionException {
		Element root = XMLHelper.build(response.getBody().toSendString());  
		Element personal = XMLHelper.find(root, "/results/personal");
		if(personal!=null){
			BeanHelper.toBean(User.class, this.context.getFetionUser(), personal);
		}
		String version = personal.getAttributeValue("version");
		if(version!=null){
			this.context.getFetionStore().getStoreVersion().setPersonalVersion(Integer.parseInt(version));
		}
		return super.doActionOK(response);
	}
}
