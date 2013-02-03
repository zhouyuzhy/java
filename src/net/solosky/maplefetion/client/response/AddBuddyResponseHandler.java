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
 * File     : AddBuddyResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.event.action.success.AddBuddySuccessEvent;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;

/**
 *
 * 添加好友回复处理器
 *
 * @author solosky <solosky772@qq.com>
 */
public class AddBuddyResponseHandler extends AbstractResponseHandler
{

	/**
     * @param client
     * @param dialog
     * @param listener
     */
    public AddBuddyResponseHandler(FetionContext client, Dialog dialog, ActionEventListener listener)
    {
	    super(client, dialog, listener);
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doNoSubscription(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doNoSubscription(SipcResponse response)
			throws FetionException
	{
		//如果返回的是522，表明用户没开通飞信
		return new FailureEvent(FailureType.USER_NOT_FOUND);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doActionOK(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doActionOK(SipcResponse response)
			throws FetionException
	{
		//用户已经开通飞信,返回了用户的真实的uri,建立一个好友对象，并加入到好友列表中

		Element root = XMLHelper.build(response.getBody().toSendString());
		Element element = XMLHelper.find(root, "/results/contacts/buddies/buddy");
		String uri = element.getAttributeValue("uri");
		FetionStore store = this.context.getFetionStore();
		
		//如果被拒绝后，用户还可以发起添加对方的请求，这样用户可能发起了多次加好友请求
		//所以这里做个判断，如果原来的好友存在，直接删除掉
		int statusCode = Integer.parseInt(element.getAttributeValue("status-code"));
		switch(statusCode) {
		case 200:
			Buddy buddy = store.getBuddyByUri(uri);
			if(buddy!=null){
				store.deleteBuddy(buddy);
			}
			buddy = new Buddy();
			BeanHelper.toBean(Buddy.class, buddy, element);
			
			store.addBuddy(buddy);
			return new AddBuddySuccessEvent(buddy);
			
		case 520:
			return new FailureEvent(FailureType.MAX_BUDDIES_LIMITED);
			
		default:
			return new FailureEvent(FailureType.UNKNOWN_FAIL);	
		}
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doTaExsit(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doTaExsit(SipcResponse response)
			throws FetionException
	{
		return new FailureEvent(FailureType.BUDDY_EXISTS);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.response.AbstractResponseHandler#doBusyHere(net.solosky.maplefetion.sipc.SipcResponse)
	 */
	@Override
	protected ActionEvent doBusyHere(SipcResponse response)
			throws FetionException {
		return new FailureEvent(FailureType.ADD_BUDDY_TIMES_LIMITED);
	}
	
}
