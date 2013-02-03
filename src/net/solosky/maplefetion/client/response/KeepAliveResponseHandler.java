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
 * Project  : MapleFetion-2.5.0
 * Package  : net.solosky.maplefetion.client.response
 * File     : KeepAliveResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-9-23
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Credential;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.DigestHelper;
import net.solosky.maplefetion.util.StringHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class KeepAliveResponseHandler extends AbstractResponseHandler
{

	/**
     * @param context
     * @param dialog
     * @param listener
     */
    public KeepAliveResponseHandler(FetionContext context, Dialog dialog,
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
    	Element root = XMLHelper.build(response.getBody().toSendString());
    	Element credentialList = XMLHelper.find(root, "/results/credentials");
		List list = XMLHelper.findAll(root, "/results/credentials/*credential");
		Iterator it = list.iterator();
		FetionStore store = this.context.getFetionStore();
		User user = this.context.getFetionUser();
		
		user.setSsiCredential(this.decryptCredential(credentialList.getAttributeValue("kernel")));
		
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String domain = e.getAttributeValue("domain");
			Credential c = store.getCredential(domain);
			if(c==null) {
				c = new Credential(domain, null);
				store.addCredential(c);
			}
			c.setCredential(this.decryptCredential(e.getAttributeValue("c")));
		}
    	
	    return super.doActionOK(response);
    }
    
    /**
     * 使用AES解密Credential
     * @param c
     * @return
     * @throws FetionException
     */
    private String decryptCredential(String c) throws FetionException {
    	User user = this.context.getFetionUser();
    	try {
            byte[] encrypted = StringHelper.base64Decode(c);
            byte[] decrypted = DigestHelper.AESDecrypt(encrypted, user.getAesKey(), user.getAesIV());
            return new String(decrypted,"utf8");
        } catch (IOException ex) {
        	throw new SystemException("decrypt credential failed.", ex);
        }
    }
    

}
