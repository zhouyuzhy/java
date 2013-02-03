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
 * Package  : net.solosky.maplefetion.bean
 * File     : FetionBuddy.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

import java.awt.image.BufferedImage;


/**
 *  飞信好友
 *
 * @author solosky <solosky772@qq.com> 
 */
public class Buddy extends Person
{
	/**
	 * 和用户的关系， 只读属性
	 */
	protected Relation relation;
	
	/**
	 * 所属分组编号
	 */
	protected String cordId;
	
	/**
     * 用户设置的好友备注 
     */
	protected String localName;
	
	/**
	 * 好友昵称
	 */
	protected String nickName;
	
	/**
	 * 真实姓名
	 */
	protected String trueName;
	
	/**
	 * 好友签名
	 */
	protected String impresa;
	
	/**
	 * 好友邮件
	 */
	protected String email;
	/**
	 * 级别
	 */
	protected int level;
	
	/**
	 * 好友扩展信息
	 */
	protected BuddyExtend extend;
	
	/**
	 * 权限设置
	 */
	protected Permission permission;
	
	/**
	 * 短信策略
	 */
	protected SMSPolicy smsPolicy;
	
	/**
	 * 头像
	 */
	protected BufferedImage portrait;

	/**
     * @return the nickName
     */
    public String getNickName()
    {
    	return nickName;
    }

	/**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName)
    {
    	this.nickName = nickName;
    }
    
	/**
     * @return the trueName
     */
    public String getTrueName()
    {
    	return trueName;
    }

	/**
     * @param trueName the trueName to set
     */
    public void setTrueName(String trueName)
    {
    	this.trueName = trueName;
    }

	/**
     * @return the level
     */
    public int getLevel()
    {
    	return level;
    }


	/**
     * @return the impresa
     */
    public String getImpresa()
    {
    	return impresa;
    }

	/**
     * @param impresa the impresa to set
     */
    public void setImpresa(String impresa)
    {
    	this.impresa = impresa;
    }

	/**
     * @return the extend
     */
    public BuddyExtend getExtend()
    {
    	return extend;
    }

	/**
     * @param extend the extend to set
     */
    public void setExtend(BuddyExtend extend)
    {
    	this.extend = extend;
    }

	/**
     * @return the permission
     */
    public Permission getPermission()
    {
    	return permission;
    }
    
    
	/**
     * @return the smsPolicy
     */
    public SMSPolicy getSMSPolicy()
    {
    	return smsPolicy;
    }
    
    

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	/**
     * @param smsPolicy the smsPolicy to set
     */
    public void setSmsPolicy(SMSPolicy smsPolicy)
    {
    	this.smsPolicy = smsPolicy;
    }

	/**
     * @param level the level to set
     */
    public void setLevel(int level)
    {
    	this.level = level;
    }

	/**
     * @param permission the permission to set
     */
    public void setPermission(Permission permission)
    {
    	this.permission = permission;
    }

	/**
     * 返回可以显示的名字
     */
    public String getDisplayName()
    {
    	if(getLocalName()!=null && getLocalName().length()>0)
    		return getLocalName();
    	if(getNickName()!=null && getNickName().length()>0)
    		return getNickName();
    	if(getTrueName()!=null && getTrueName().length()>0)
    		return getTrueName();
    	if(getFetionId()>0)
    		return Integer.toString(getFetionId());
    	if(getMobile()!=0)
    		return Long.toString(getMobile());
    	return null;
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.bean.Person#getDisplayPresence()
	 */
	@Override
	public String getDisplayPresence() {
		if(this.getRelation()!=Relation.BUDDY){
			return "离线";
		}
		if(this.getPresence().getValue()==Presence.OFFLINE){
			if(this.getSMSPolicy().isSMSOnline()){
				return "短信在线";
			}else{
				return "离线";
			}
		}else{
			return Presence.presenceValueToDisplayString(this.getPresence().getValue());
		}
	}
	

	
	/**
	 * 默认构造函数
	 */
    public Buddy()
    {
    	relation =  Relation.BUDDY;
    	this.permission = new Permission();
		this.smsPolicy  = new SMSPolicy();
    }


	/**
     * @return the relation
     */
    public Relation getRelation()
    {
    	return relation;
    }

	/**
     * @return the cordId
     */
    public String getCordId()
    {
    	return cordId;
    }


	/**
     * @return the localName
     */
    public String getLocalName()
    {
    	return localName;
    }
    

	/**
     * @param cordId the cordId to set
     */
    public void setCordId(String cordId)
    {
    	this.cordId = cordId;
    }


	/**
     * @param localName the localName to set
     */
    public void setLocalName(String localName)
    {
    	this.localName = localName;
    }
    

	/**
     * @param relation the relation to set
     */
    public void setRelation(Relation relation)
    {
    	this.relation = relation;
    }
    

	/**
     * @return the portrait
     */
    public BufferedImage getPortrait()
    {
    	return portrait;
    }

	/**
     * @param portrait the portrait to set
     */
    public void setPortrait(BufferedImage portrait)
    {
    	this.portrait = portrait;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Buddy [getDisplayName()=" + getDisplayName() + ", getUri()="
				+ getUri() + "]";
	}
}
