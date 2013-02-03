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
 * File     : StoreVersion.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-27
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 * 用户存储信息版本
 *
 * @author solosky <solosky772@qq.com>
 */
public class StoreVersion
{
	private int localeSettingVersion;
	private int userSettingVersion;
	private int personalVersion;
	private int contactVersion;
	private int groupVersion;
	private int permissionVersion;
	private int scheduleSMSVersion;
	
	public StoreVersion()
	{
		this.localeSettingVersion = 0;
		this.userSettingVersion   = 0;
		this.permissionVersion    = 0;
		this.contactVersion       = 0;
		this.groupVersion         = 0;
		this.personalVersion      = 0;
		this.scheduleSMSVersion   = 0;
	}
	/**
     * @return the localeSettingVersion
     */
    public int getLocaleSettingVersion()
    {
    	return localeSettingVersion;
    }
	/**
     * @param localeSettingVersion the localeSettingVersion to set
     */
    public void setLocaleSettingVersion(int localeSettingVersion)
    {
    	this.localeSettingVersion = localeSettingVersion;
    }
	/**
     * @return the userSettingVersion
     */
    public int getUserSettingVersion()
    {
    	return userSettingVersion;
    }
	/**
     * @param userSettingVersion the userSettingVersion to set
     */
    public void setUserSettingVersion(int userSettingVersion)
    {
    	this.userSettingVersion = userSettingVersion;
    }
	/**
     * @return the personalVersion
     */
    public int getPersonalVersion()
    {
    	return personalVersion;
    }
	/**
     * @param personalVersion the personalVersion to set
     */
    public void setPersonalVersion(int personalVersion)
    {
    	this.personalVersion = personalVersion;
    }
	/**
     * @return the contactVersion
     */
    public int getContactVersion()
    {
    	return contactVersion;
    }
	/**
     * @param contactVersion the contactVersion to set
     */
    public void setContactVersion(int contactVersion)
    {
    	this.contactVersion = contactVersion;
    }
	/**
     * @return the groupVersion
     */
    public int getGroupVersion()
    {
    	return groupVersion;
    }
	/**
     * @param groupVersion the groupVersion to set
     */
    public void setGroupVersion(int groupVersion)
    {
    	this.groupVersion = groupVersion;
    }
	/**
     * @return the permissionVersion
     */
    public int getPermissionVersion()
    {
    	return permissionVersion;
    }
	/**
     * @param permissionVersion the permissionVersion to set
     */
    public void setPermissionVersion(int permissionVersion)
    {
    	this.permissionVersion = permissionVersion;
    }
	/**
	 * @return the scheduleSMSVersion
	 */
	public int getScheduleSMSVersion()
	{
		return scheduleSMSVersion;
	}
	/**
	 * @param scheduleSMSVersion the scheduleSMSVersion to set
	 */
	public void setScheduleSMSVersion(int scheduleSMSVersion)
	{
		this.scheduleSMSVersion = scheduleSMSVersion;
	}
}
