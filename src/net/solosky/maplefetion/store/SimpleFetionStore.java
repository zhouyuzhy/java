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
 * Package  : net.solosky.maplefetion
 * File     : FetionStore.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Cord;
import net.solosky.maplefetion.bean.Credential;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.bean.StoreVersion;
import net.solosky.maplefetion.bean.User;

/**
 * 
 * 简单的飞信存储的一个实现 
 * 所有的数据都是保存在内存中
 *
 * @author solosky <solosky772@qq.com> 
 */
public class SimpleFetionStore implements FetionStore
{
	/**
	 * 好友列表
	 * 使用HASH便于查找
	 */
	private Hashtable<String, Buddy> buddyList;
	
	/**
	 * 分组列表
	 */
	private ArrayList<Cord> cordList;
	
	/**
	 * 群列表
	 */
	private Hashtable<String, Group> groupList;
	
	/**
	 * 群成员列表
	 */
	private HashMap<String, HashMap<String, Member>> groupMemberList;
	
	/**
	 * 定时短信列表
	 */
	private ArrayList<ScheduleSMS> scheduleSMSList;
	
	/**
	 * 凭证列表
	 */
	private HashMap<String, Credential> credentialList;
	
	/**
	 * 存储版本
	 */
	private StoreVersion storeVersion;

	
	/**
	 * 构造函数
	 */
	public SimpleFetionStore()
	{
		this.buddyList = new Hashtable<String, Buddy>();
		this.cordList  = new ArrayList<Cord>();
		this.groupList = new Hashtable<String, Group>();
		this.groupMemberList = new HashMap<String, HashMap<String,Member>>();
		this.storeVersion = new StoreVersion();
		this.scheduleSMSList = new ArrayList<ScheduleSMS>();
		this.credentialList = new HashMap<String, Credential>();
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.IFetionStore#addBuddy(net.solosky.maplefetion.bean.FetionBuddy)
     */
	public synchronized void addBuddy(Buddy buddy)
	{
		this.buddyList.put(buddy.getUri(), buddy);
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getBuddyByUri(java.lang.String)
     */
    @Override
    public synchronized Buddy getBuddyByUri(String uri)
    {
    	if(uri==null)	return null;
		return this.buddyList.get(uri);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getBuddyByUserId(int)
     */
    @Override
    public synchronized Buddy getBuddyByUserId(int userId)
    {
    	Iterator<Buddy> it = this.buddyList.values().iterator();
    	while(it.hasNext()) {
    		Buddy buddy = it.next();
    		if(buddy.getUserId()==userId)
    			return buddy;
    	}
    	return null;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#removeBuddyByUri(java.lang.String)
     */
    @Override
    public synchronized void deleteBuddy(Buddy buddy)
    {
		this.buddyList.remove(buddy.getUri());
    }
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.IFetionStore#getBuddyList()
     */
	public synchronized Collection<Buddy> getBuddyList()
	{
		return this.buddyList.values();
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.IFetionStore#getBuddyList(java.lang.String)
     */
	public synchronized Collection<Buddy> getBuddyListByCord(Cord cord)
	{
		ArrayList<Buddy> list = new ArrayList<Buddy>();
		Iterator<Buddy> it = this.buddyList.values().iterator();
		Buddy buddy = null;
		String [] buddyCordIds = null;
		while(it.hasNext()) {
			buddy = it.next();
			if(buddy.getCordId()!=null){
				buddyCordIds = buddy.getCordId().split(";");
				for(String cid : buddyCordIds){
					if(cid.equals(Integer.toString(cord.getId()))){
						list.add(buddy);
					}
				}
			}
		}
		return list;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getBuddyListWithoutCord()
     */
    @Override
    public synchronized Collection<Buddy> getBuddyListWithoutCord()
    {
    	ArrayList<Buddy> list = new ArrayList<Buddy>();
		Iterator<Buddy> it = this.buddyList.values().iterator();
		Buddy buddy = null;
		String  buddyCordId = null;
		while(it.hasNext()) {
			buddy = it.next();
			buddyCordId = buddy.getCordId();
			if(buddyCordId==null || buddyCordId.length()==0) {
				list.add(buddy);
			}
		}
		return list;
    }
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.IFetionStore#addCord(net.solosky.maplefetion.bean.FetionCord)
     */
	public synchronized void addCord(Cord cord)
	{
		this.cordList.add(cord);
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getCord(int)
     */
    @Override
    public synchronized Cord getCord(int cordId)
    {
	   Iterator<Cord> it = this.cordList.iterator();
	   while(it.hasNext()) {
		   Cord cord = it.next();
		   if(cord.getId()==cordId)
			   return cord;
	   }
	   return null;
    }
    
    public synchronized void deleteCord(Cord cord)
    {
    	this.cordList.remove(cord);
    }
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.IFetionStore#getCordList()
     */
	public synchronized Collection<Cord> getCordList()
	{
		return this.cordList;
	}

    /**
     * 返回指定关系的列表
     * @param relation
     * @return
     */
	@Override
    public synchronized Collection<Buddy> getBuddyListByRelation(Relation relation)
    {
    	ArrayList<Buddy> list = new ArrayList<Buddy>();
 	   Iterator<Buddy> it = this.buddyList.values().iterator();
 	   while(it.hasNext()) {
 		   Buddy buddy = it.next();
 		   if(buddy.getRelation()==relation)
 			   list.add(buddy);
 	   }
 	   return list;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#flush()
     */
    @Override
    public synchronized void flush()
    {
	    // TODO Auto-generated method stub
	    
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#init(net.solosky.maplefetion.bean.FetionUser)
     */
    @Override
    public synchronized void init(User user)
    {
	    // TODO Auto-generated method stub
	    
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getStoreVersion()
     */
    @Override
    public synchronized StoreVersion getStoreVersion()
    {
    	return this.storeVersion;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#addGroup(net.solosky.maplefetion.bean.FetionGroup)
     */
    @Override
    public synchronized void addGroup(Group group)
    {
    	this.groupList.put(group.getUri(), group);
    	this.groupMemberList.put(group.getUri(), new HashMap<String,Member>());
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getGroup(java.lang.String)
     */
    @Override
    public synchronized Group getGroup(String uri)
    {
	    return this.groupList.get(uri);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getGroupList()
     */
    @Override
    public synchronized Collection<Group> getGroupList()
    {
	    return this.groupList.values();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#removeGroup(java.lang.String)
     */
    @Override
    public synchronized void deleteGroup(Group group)
    {
	    this.groupList.remove(group.getUri());
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#clear()
     */
    @Override
    public synchronized void clear()
    {
    	this.buddyList.clear();
    	this.cordList.clear();
    	this.groupList.clear();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#clearBuddyList()
     */
    @Override
    public synchronized void clearBuddyList()
    {
	    this.buddyList.clear();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#clearCordList()
     */
    @Override
    public synchronized void clearCordList()
    {
    	this.cordList.clear();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#clearGroupList()
     */
    @Override
    public synchronized void clearGroupList()
    {
    	this.groupList.clear();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#addGroupMember(net.solosky.maplefetion.bean.Group, net.solosky.maplefetion.bean.Member)
     */
    @Override
    public synchronized void addGroupMember(Group group, Member member)
    {
    	HashMap<String,Member> table = this.groupMemberList.get(group.getUri());
    	if(table!=null) {
    		table.put(member.getUri(), member);
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getGroupMemberList(net.solosky.maplefetion.bean.Group)
     */
    @Override
    public synchronized Collection<Member> getGroupMemberList(Group group)
    {
    	return this.groupMemberList.get(group.getUri()).values();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#removeGroupMember(net.solosky.maplefetion.bean.Group, net.solosky.maplefetion.bean.Member)
     */
    @Override
    public void deleteGroupMember(Group group, Member member)
    {
    	HashMap<String,Member> table = this.groupMemberList.get(group.getUri());
    	if(table!=null) {
    		table.remove(member.getUri());
    	}
	    
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getGroupMember(net.solosky.maplefetion.bean.Group, java.lang.String)
     */
    @Override
    public synchronized Member getGroupMember(Group group, String uri)
    {
    	HashMap<String,Member> table = this.groupMemberList.get(group.getUri());
    	if(table!=null) {
    		return table.get(uri);
    	}
    	return null;
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.store.FetionStore#addScheduleSMS(net.solosky.maplefetion.bean.ScheduleSMS)
	 */
	@Override
	public synchronized void addScheduleSMS(ScheduleSMS scheduleSMS)
	{
		this.scheduleSMSList.add(scheduleSMS);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.store.FetionStore#deleteScheduleSMS(net.solosky.maplefetion.bean.ScheduleSMS)
	 */
	@Override
	public synchronized void deleteScheduleSMS(ScheduleSMS scheduleSMS)
	{
		this.scheduleSMSList.remove(scheduleSMS);
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.store.FetionStore#getScheduleSMSList()
	 */
	@Override
	public synchronized Collection<ScheduleSMS> getScheduleSMSList()
	{
		return this.scheduleSMSList;
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.store.FetionStore#getScheduleSMS(int)
	 */
	@Override
	public synchronized ScheduleSMS getScheduleSMS(int scId)
	{
		Iterator<ScheduleSMS> it = this.scheduleSMSList.iterator();
		while(it.hasNext()){
			ScheduleSMS sc = it.next();
			if(sc.getId()==scId){
				return sc;
			}
		}
		return null;
	}

	@Override
	public void flushBuddy(Buddy buddy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushCord(Cord cord) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushGroup(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushMemeber(Group group, Member member) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushStoreVersion(StoreVersion version) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#addCredential(net.solosky.maplefetion.bean.Credential)
     */
    @Override
    public void addCredential(Credential credential)
    {
    	this.credentialList.put(credential.getDomain(), credential);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getCredential(java.lang.String)
     */
    @Override
    public Credential getCredential(String domain)
    {
	   return this.credentialList.get(domain);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.store.FetionStore#getCredentialList()
     */
    @Override
    public Collection<Credential> getCredentialList()
    {
	   return this.credentialList.values();
    }
}
