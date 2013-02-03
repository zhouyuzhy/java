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
 * Package  : net.solosky.maplefetion.store
 * File     : IFetionStore.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-27
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.store;

import java.util.Collection;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Cord;
import net.solosky.maplefetion.bean.Credential;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.bean.StoreVersion;

/**
 *
 *
 * @author solosky <solosky772@qq.com> 
 */
public interface FetionStore
{

	/**
	 * 添加好友
	 * @param buddy
	 */
	public abstract void addBuddy(Buddy buddy);

	/**
	 * 根据飞信Uri地址返回好友
	 * @param uri
	 * @return
	 */
	public Buddy getBuddyByUri(String uri);
	
	/**
	 * 根据飞信用ID返回好友
	 * @param userId
	 * @return
	 */
	public Buddy getBuddyByUserId(int userId);

	/**
	 * 删除好友
	 * @param uid
	 */
	public void deleteBuddy(Buddy buddy);

	/**
	 * 返回全部好友列表
	 * @return 
	 */
	public Collection<Buddy> getBuddyList();
	
	/**
	 * 清空好友列表
	 */
	public void clearBuddyList();

	/**
	 * 返回指定组的好友
	 * @param cordId
	 * @return
	 */
	public Collection<Buddy> getBuddyListByCord(Cord cord);
	
	
	/**
	 * 返回没有分组的好友列表
	 * @return
	 */
	public Collection<Buddy> getBuddyListWithoutCord();

	/**
	 * 添加好友分组
	 */
	public void addCord(Cord cord);
	
	/**
	 * 返回一个分组
	 */
	public Cord getCord(int cordId);
	
	/**
	 * 删除一个分组
	 * @param cord
	 */
	public void deleteCord(Cord cord);

	/**
	 * 返回所有分组列表
	 * @return
	 */
	public Collection<Cord> getCordList();
	
	/**
	 * 清除所有的组列表
	 */
	public void clearCordList();
	
	/**
	 * 根据关系返回列表
	 * @param relation		好友关系 定义在Relaction中
	 */
	public Collection<Buddy> getBuddyListByRelation(Relation relation);
    
    /**
     * 存储数据版本
     * @return
     */
    public StoreVersion getStoreVersion();
    /**
     * 初始化
     * @param user
     */
    public void init(User user);
    
    /**
     * 强制更新所有信息
     */
    public void flush();

    /**
     * 清除所有的信息
     */
    public void clear();
    
    /**
     * 更新好友对象
     * @param buddy
     */
    public void flushBuddy(Buddy buddy);
    
    /**
     * 更新分组对象
     * @param cord
     */
    public void flushCord(Cord cord);
    
    /**
     * 更新群信息
     * @param group
     */
    public void flushGroup(Group group);
    
    /**
     * 更新群中成员信息
     * @param group
     * @param member
     */
    public void flushMemeber(Group group, Member member);
    
    
    /**
     * 更新存储信息
     * @param version
     */
    public void flushStoreVersion(StoreVersion version);
    
    /**
     * 返回所有的群列表
     * @return
     */
    public Collection<Group> getGroupList();
    
    
    /**
     * 清除所有群列表
     */
    public void clearGroupList();
    
    /**
     * 返回群对象
     * @param uri		群的地址
     * @return
     */
    public Group getGroup(String uri);
    
    /**
     * 删除群对象
     * @param uri		群地址
     */
    public void deleteGroup(Group group);
    
    /**
     * 添加群对象
     * @param group		群对象
     */
    public void addGroup(Group group);
    
    /**
     * 群内添加一个成员
     * @param group
     * @param member
     */
    public void addGroupMember(Group group, Member member);
    
    /**
     * 群内删除一个成员
     * @param group
     * @param member
     */
    public void deleteGroupMember(Group group, Member member);
    
    /**
     * 返回群的成员列表
     * @param group
     * @return
     */
    public Collection<Member> getGroupMemberList(Group group);
    
    
    /**
     * 返回群成员
     * @param group		群对象
     * @param uri		成员的URI
     * @return
     */
    public Member getGroupMember(Group group, String uri);
    
    /**
     * 返回所有的定时短信列表
     * @return
     */
    public Collection<ScheduleSMS> getScheduleSMSList();
    
    /**
     * 根据定时短信的ID返回定时短信对象
     * @param scId
     * @return
     */
    public ScheduleSMS getScheduleSMS(int scId);
    
    /**
     * 添加定时短信到列表
     * @param scheduleSMS 定时短信对象
     */
    public void addScheduleSMS(ScheduleSMS scheduleSMS);
    
    /**
     * 删除定时短信列表
     * @param scheduleSMS
     */
    public void deleteScheduleSMS(ScheduleSMS scheduleSMS);
    
    
    /**
     * 添加一个凭证
     * @param credential
     */
    public void addCredential(Credential credential);
    
    /**
     * 根据域名查找凭证
     * @param domain	域名
     * @return
     */
    public Credential getCredential(String domain);
    
    /**
     * 返回所有的凭证列表
     * @return
     */
    public Collection<Credential> getCredentialList();

}
