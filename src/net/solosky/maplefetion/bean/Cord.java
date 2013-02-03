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
 * File     : FetionCord.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 * 飞信好友分组
 *
 * @author solosky <solosky772@qq.com> 
 */
public class Cord
{
	/**
	 * 分组编号
	 */
	private int id;
	
	/**
	 * 分组名
	 */
	private String title;
	
	/**
	 * 默认构造函数
	 */
	public Cord(int id, String title)
	{
		this.id = id;
		this.title = title;
	}

	/**
     * @return the cordId
     */
    public int getId()
    {
    	return id;
    }


	/**
     * @return the title
     */
    public String getTitle()
    {
    	return title;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
    	this.id = id;
    }

	/**
     * @param title the title to set
     */
    public void setTitle(String title)
    {
    	this.title = title;
    }

	public String toString()
    {
    	return "[Cord, id="+this.id+", title="+this.title+"]";
    }
	
}
