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
 * File     : Presence.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 *
 * 飞信在线状态
 *
 * @author solosky <solosky772@qq.com>
 */
public class Presence
{
	/**
	 * 在线状态
	 */
	public static final int ONLINE = 400;
	
	/**
	 * 离线状态，和隐身状态是一个值
	 */
	public static final int OFFLINE = 000;
	
	/**
	 * 忙碌状态
	 */
	public static final int BUSY = 600;
	
	/**
	 * 离开状态
	 */
	public static final int AWAY = 100;
	
	/**
	 * 隐身状态，和离线状态是一个值
	 */
	public static final int HIDEN = 000;
	
	/**
	 * 机器人状态
	 */
	public static final int ROBOT = 499;
	
	/**
	 * 在线状态
	 */
	private int value;
	
	/**
	 * 状态的描述
	 */
	private String desc;
	
	/**
	 * 客户端类型
	 */
	private String clientType;
	
	/**
	 * 客户端ID
	 */
	private String clientId;
	
	/**
	 * 客户端属性
	 */
	private String clientCaps;
	
	
	/**
	 * 默认构造函数
	 */
	public Presence()
	{
		
	}


	/**
     * @return the value
     */
    public int getValue()
    {
    	return value;
    }

	/**
     * @return the clientType
     */
    public String getClientType()
    {
    	return clientType;
    }

	/**
     * @return the clientId
     */
    public String getClientId()
    {
    	return clientId;
    }


	/**
     * @return the clientCaps
     */
    public String getClientCaps()
    {
    	return clientCaps;
    }

    /**
	 * @return the desc
	 */
	public String getDesc()
	{
		return desc;
	}
	

	/**
     * @param value the value to set
     */
    public void setValue(int value)
    {
    	this.value = value;
    }


	/**
     * @param desc the desc to set
     */
    public void setDesc(String desc)
    {
    	this.desc = desc;
    }


	/**
     * @param clientType the clientType to set
     */
    public void setClientType(String clientType)
    {
    	this.clientType = clientType;
    }


	/**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId)
    {
    	this.clientId = clientId;
    }


	/**
     * @param clientCaps the clientCaps to set
     */
    public void setClientCaps(String clientCaps)
    {
    	this.clientCaps = clientCaps;
    }


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Presence [value=" + value + ", desc=" + desc + ", clientId="
				+ clientId + "]";
	}

	/**
	 * 把状态值转换为可以显示的字符串
	 * @param value
	 * @return
	 */
	public static String presenceValueToDisplayString(int value)
	{
		switch(value){
	    	case Presence.ONLINE:	return "电脑在线";
	    	case Presence.BUSY:		return "电脑忙碌";
	    	case Presence.AWAY:		return "电脑离开";
	    	case Presence.OFFLINE:	return "离线";
	    	case Presence.ROBOT:	return "机器人在线";
	    	default:	return "未知状态";
    	}
	}

	/**
     * 检查是否是合法的presence
     * @param presence
     * @return
     */
    public static boolean isValidPresenceValue(int presence)
    {
    	switch(presence) {
    	case AWAY:
    	case ONLINE:
    	case HIDEN:
    	case BUSY:
    	case ROBOT:
    		return true;
    	
    	default:
    		return false;
    		
    	}
    }
}
