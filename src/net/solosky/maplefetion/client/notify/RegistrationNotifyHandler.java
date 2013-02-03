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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.protocol.notify
 * File     : RegistrationNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-12-1
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.client.RegistrationException;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *	
 *	注册通知
 *	
 * @author solosky <solosky772@qq.com> 
 */
public class RegistrationNotifyHandler extends AbstractNotifyHandler
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.protocol.ISIPNotifyHandler#handle(net.solosky.maplefetion.sip.SIPNotify)
     */
    @Override
    public void handle(SipcNotify notify) throws FetionException
    {
    	Element root = XMLHelper.build(notify.getBody().toSendString());
    	Element event = XMLHelper.find(root, "/events/event");
    	String eventType = event.getAttributeValue("type");
    	
    	//用户在其他地方登陆
    	if(eventType!=null && eventType.equals("deregistered")) {
    		this.context.handleException(new RegistrationException(RegistrationException.DEREGISTERED));
    	}else if(eventType.equals("disconnect")){
    		this.context.handleException(new RegistrationException(RegistrationException.DISCONNECTED));
    	}else{
    		logger.warn("Unknown registration event type:"+eventType);
    	}
    }

}
