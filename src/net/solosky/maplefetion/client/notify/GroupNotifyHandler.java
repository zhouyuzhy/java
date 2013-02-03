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
 * Package  : net.solosky.maplefetion.client.notify
 * File     : GroupNotifyHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-9
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.notify;

import java.util.Iterator;
import java.util.List;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.XMLHelper;

import org.jdom.Element;


/**
 *
 * 群成员状态改变
 *
 * @author solosky <solosky772@qq.com>
 */ 
public class GroupNotifyHandler extends AbstractNotifyHandler
{

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.NotifyHandler#handle(net.solosky.maplefetion.sipc.SipcNotify)
     */
    @Override
    public void handle(SipcNotify notify) throws FetionException
    {
    	Element root = XMLHelper.build(notify.getBody().toSendString());
    	Element event = XMLHelper.find(root, "/events/event");
    	String type = event.getAttributeValue("type");
    	if(type.equals("PresenceChanged")) {
    		this.presenceChanged(event);
    	}else if(type.equals("PGGetGroupInfo")){
    		this.getGroupInfo(event);
    	}else if(type.equals("PGGetGroupMembers")){
    		this.getGroupMembers(event);
    	}else{
    		logger.warn(" GroupNotifyHandler: Unknown event type - "+type);
    	}
    }
    
    
    /**
     * 处理状态改变
     * @throws FetionException 
     */
    private void presenceChanged(Element event) throws FetionException
    {
    	List groups = event.getChildren("group");
    	Iterator it = groups.iterator();
    	while(it.hasNext()) {
    		Element el = (Element) it.next();
    		Group group = this.context.getFetionStore().getGroup(el.getAttributeValue("uri"));
    		if(group!=null) {
    			//群信息
    			BeanHelper.toBean(Group.class, group, el);
    			
    			//成员
    			List members = el.getChildren("member");
    			Iterator mit = members.iterator();
    			while(mit.hasNext()) {
    				Element ell = (Element) mit.next();
    				Member member = this.context.getFetionStore().getGroupMember(group, ell.getAttributeValue("uri"));
    				if(member!=null){
    					BeanHelper.toBean(Member.class, member, ell);
    				}
    			}
    		}
    	}
    }
    
    /**
     * 获取群信息
     * @param event
     * @throws FetionException 
     */
    private void getGroupInfo(Element event) throws FetionException
    {
    	List groups = XMLHelper.findAll(event, "/event/results/groups/*group");
    	Iterator it = groups.iterator();
    	while(it.hasNext()){
    		Element el = (Element) it.next();
    		Group group = this.context.getFetionStore().getGroup(el.getAttributeValue("uri"));
    		if(group!=null){
    			   BeanHelper.toBean(Group.class, group, el);
    		}
    	}
    }
    
    /**
     * 获取群成员
     * @throws FetionException 
     */
    private void getGroupMembers(Element event) throws FetionException
    {
    	List groupList = XMLHelper.findAll(event, "/event/results/groups/*group");
		Iterator it = groupList.iterator();
		FetionStore store = this.context.getFetionStore();
		while(it.hasNext()) {
			Element g = (Element) it.next();
			Group group = store.getGroup(g.getAttributeValue("uri"));
			//群下面的成员迭代
			List memberList = XMLHelper.findAll(g, "/group/*member");
			Iterator mit = memberList.iterator();
			while(mit.hasNext()) {
				Element e = (Element) mit.next();
				Member member = new Member();
				
				member.setUri(e.getAttributeValue("uri"));
				member.setNickName(e.getAttributeValue("nickname"));
				member.setIicNickName(e.getAttributeValue("iicnickname"));
				//member.setUserId(Integer.parseInt(e.getAttributeValue("user-id")));
				member.setT6svcid(Integer.parseInt(e.getAttributeValue("t6svcid")));
				member.setIdentity(Integer.parseInt(e.getAttributeValue("identity")));
				
				store.addGroupMember(group, member);
			}
		}
    	
    	
    }
}
