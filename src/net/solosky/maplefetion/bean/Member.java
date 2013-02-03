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
 * Package  : net.solosky.maplefetion.bean
 * File     : GroupMember.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-28
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 *
 *  群成员
 *
 * @author solosky <solosky772@qq.com>
 */
public class Member extends Person
{
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 用户自定义的昵称  （不确定）
	 */
	private String iicNickName;
	
	/**
	 * 未知属性
	 */
	private int identity;
	
	/**
	 * 未知属性
	 */
	private int t6svcid;

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
     * @return the iicNickName
     */
    public String getIicNickName()
    {
    	return iicNickName;
    }

	/**
     * @param iicNickName the iicNickName to set
     */
    public void setIicNickName(String iicNickName)
    {
    	this.iicNickName = iicNickName;
    }

	/**
     * @return the identity
     */
    public int getIdentity()
    {
    	return identity;
    }

	/**
     * @param identity the identity to set
     */
    public void setIdentity(int identity)
    {
    	this.identity = identity;
    }

	/**
     * @return the t6svcid
     */
    public int getT6svcid()
    {
    	return t6svcid;
    }

	/**
     * @param t6svcid the t6svcid to set
     */
    public void setT6svcid(int t6svcid)
    {
    	this.t6svcid = t6svcid;
    }
    
    

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.bean.Person#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
	   if(this.iicNickName!=null && this.iicNickName.length()>0)
		   return this.iicNickName;
	   else if(this.nickName!=null && this.nickName.length()>0)
		   return this.nickName;
	   else
		   return getUri();
    }
}
