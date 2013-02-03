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
 * File     : Person.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;


/**
 *
 * 普通飞信用户类
 * 定义了所有飞信用户的基本信息
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class Person
{
	/**
	 * 用户ID，每个飞信用户都有
	 */
	protected int userId;
	
	/**
	 * 飞信号
	 */
	protected int fetionId;
	
	/**
	 * 用户所在的域
	 */
	protected String domain;
	
	/**
	 * 用户URI，唯一的
	 */
	protected String uri;
	
	/**
	 * 用户手机号
	 */
	protected long mobile;
	
	
	/**
	 * 用户状态, 只读属性
	 */
	protected Presence presence;

	/**
	 * 构造函数
	 */
	public Person()
	{
		presence = new Presence();
	}

	/**
     * @return the userId
     */
    public int getUserId()
    {
    	return userId;
    }

	/**
     * @return the fetionId
     */
    public int getFetionId()
    {
    	return fetionId;
    }


	/**
     * @return the domain
     */
    public String getDomain()
    {
    	return domain;
    }


	/**
     * @return the uri
     */
    public String getUri()
    {
    	return uri;
    }


	/**
     * @param uri the uri to set
     */
    public void setUri(String uri)
    {
    	this.uri = uri.trim();
    	if(uri.startsWith("sip")) {
    		this.fetionId = Integer.parseInt(uri.substring(4, uri.indexOf('@')));
    	}else if(uri.startsWith("tel")){
    		this.mobile = Long.parseLong(uri.substring(4));
    	}else {
    		throw new IllegalArgumentException("Illegal uri:"+uri);
    	}
    }


	/**
     * @return the mobile
     */
    public long getMobile()
    {
    	return mobile;
    }

	/**
     * @return the presence
     */
    public Presence getPresence()
    {
    	return presence;
    }

	/**
     * 返回可以显示的名字
     * 这只是个工具方法，不存在DisplayName这个字段
     * 这个方法在子类会有不同的实现，总是可以返回一个可以显示的名字，简化程序的编写
     * @return
     */
    public String getDisplayName()
    {
    	return Integer.toString(this.userId);
    }
    
    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId)
    {
    	this.userId = userId;
    }

	/**
     * @param fetionId the fetionId to set
     */
    public void setFetionId(int fetionId)
    {
    	this.fetionId = fetionId;
    }

	/**
     * @param domain the domain to set
     */
    public void setDomain(String domain)
    {
    	this.domain = domain;
    }

	/**
     * @param mobile the mobile to set
     */
    public void setMobile(long mobile)
    {
    	this.mobile = mobile;
    }

	/**
     * @param presence the presence to set
     */
    public void setPresence(Presence presence)
    {
    	this.presence = presence;
    }

	/**
     * 返回可以显示的状态
     * 也是工具方法，返回的状态可能 电脑在线，电脑离开，电脑忙碌，短信在线，离线
     */
    public String getDisplayPresence()
    {
    	return Presence.presenceValueToDisplayString(this.presence.getValue());
    }
}