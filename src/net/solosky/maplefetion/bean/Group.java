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
 * Package  : net.solosky.net.maplefetion.bean
 * File     : FetionGroup.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;



/**
 *
 * 飞信群
 *
 * @author solosky <solosky772@qq.com>
 */
public class Group
{
	/**
	 * 群地址
	 */
	private String uri;
	
	/**
	 * 群名字
	 */
	private String name;
	
	/**
	 * 群简介
	 */
	private String intro;
	
	/**
	 * 群公告
	 */
	private String bulletin;
	
	/**
	 * 当前群成员数目
	 */
	private int memberCount;
	
	
	/**
	 * 构造函数
	 */
	public Group()
	{
	}

	/**
     * @return the uri
     */
    public String getUri()
    {
    	return uri;
    }


	/**
     * @return the name
     */
    public String getName()
    {
    	return name;
    }

	/**
     * @param name the name to set
     */
    public void setName(String name)
    {
    	this.name = name;
    }

	/**
     * @return the intro
     */
    public String getIntro()
    {
    	return intro;
    }

	/**
     * @param intro the intro to set
     */
    public void setIntro(String intro)
    {
    	this.intro = intro;
    }

	/**
     * @return the bulletin
     */
    public String getBulletin()
    {
    	return bulletin;
    }

	/**
     * @param bulletin the bulletin to set
     */
    public void setBulletin(String bulletin)
    {
    	this.bulletin = bulletin;
    }
    
    /**
     * @return the memberCount
     */
    public int getMemberCount()
    {
    	return memberCount;
    }

	/**
     * @param memberCount the memberCount to set
     */
    public void setMemberCount(int memberCount)
    {
    	this.memberCount = memberCount;
    }

	/**
     * @param uri the uri to set
     */
    public void setUri(String uri)
    {
    	this.uri = uri;
    }

	public String toString()
    {
    	return "[Group name="+this.name+", uri="+this.uri+", members="+this.memberCount+" ]";
    }
}
