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
 * File     : PersonalDetail.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-30
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

import java.util.Date;

/**
 *	
 *	好友的详细资料 
 *  这个类主要是减少内存使用，因为客户端运行时并不是把好友的详细信息全部下载下来
 *
 * @author solosky <solosky772@qq.com> 
 */
public class BuddyExtend
{
	private String nation;
	private String province;
	private String city;
	private Date birth;
	private String blood;
	private String occupation;
	private String hobby;
	private String lunarAnimal;
	private String horoscope;
	private String feike;
	/**
     * @return the nation
     */
    public String getNation()
    {
    	return nation;
    }
	/**
     * @param nation the nation to set
     */
    public void setNation(String nation)
    {
    	this.nation = nation;
    }
	/**
     * @return the province
     */
    public String getProvince()
    {
    	return province;
    }
	/**
     * @param province the province to set
     */
    public void setProvince(String province)
    {
    	this.province = province;
    }
	/**
     * @return the city
     */
    public String getCity()
    {
    	return city;
    }
	/**
     * @param city the city to set
     */
    public void setCity(String city)
    {
    	this.city = city;
    }
	/**
     * @return the birth
     */
    public Date getBirth()
    {
    	return birth;
    }
	/**
     * @param birth the birth to set
     */
    public void setBirth(Date birth)
    {
    	this.birth = birth;
    }
	/**
     * @return the blood
     */
    public String getBlood()
    {
    	return blood;
    }
	/**
     * @param blood the blood to set
     */
    public void setBlood(String blood)
    {
    	this.blood = blood;
    }
	/**
     * @return the occupation
     */
    public String getOccupation()
    {
    	return occupation;
    }
	/**
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation)
    {
    	this.occupation = occupation;
    }
	/**
     * @return the hobby
     */
    public String getHobby()
    {
    	return hobby;
    }
	/**
     * @param hobby the hobby to set
     */
    public void setHobby(String hobby)
    {
    	this.hobby = hobby;
    }
	/**
     * @return the lunarAnimal
     */
    public String getLunarAnimal()
    {
    	return lunarAnimal;
    }
	/**
     * @param lunarAnimal the lunarAnimal to set
     */
    public void setLunarAnimal(String lunarAnimal)
    {
    	this.lunarAnimal = lunarAnimal;
    }
	/**
     * @return the horoscope
     */
    public String getHoroscope()
    {
    	return horoscope;
    }
	/**
     * @param horoscope the horoscope to set
     */
    public void setHoroscope(String horoscope)
    {
    	this.horoscope = horoscope;
    }
	/**
     * @return the feike
     */
    public String getFeike()
    {
    	return feike;
    }
	/**
     * @param feike the feike to set
     */
    public void setFeike(String feike)
    {
    	this.feike = feike;
    }
	
	
	
	
}
