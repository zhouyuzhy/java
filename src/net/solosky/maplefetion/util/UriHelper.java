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
 * Package  : net.solosky.maplefetion.util
 * File     : UriHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-28
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import net.solosky.maplefetion.bean.Buddy;

/**
 *
 *  URI工具类，帮助URI处理
 *
 * @author solosky <solosky772@qq.com>
 */
public class UriHelper
{
	/**
	 * 判断是否是群URI
	 * @return
	 */
	public static boolean isGroup(String uri)
	{
		return uri!=null && uri.indexOf("PG")!=-1;
	}
	
	/**
	 * 判断是否是手机号组成的ＵＲＩ
	 * @param uri
	 * @return
	 */
	public static boolean isMobile(String uri)
	{
		return uri!=null && uri.indexOf("tel")!=-1;
	}
	
	
	/**
	 * 根据一个URI来创建好友对象
	 * @param uri
	 * @return
	 */
	public static Buddy createBuddy(String uri)
	{
		Buddy buddy = new Buddy();
		buddy.setUri(uri);
		
		return buddy;
	}
	
	/**
	 * 从一个URI解析出飞信号
	 * @param uri
	 * @return
	 */
	public static int parseFetionId(String uri)
	{
		if(uri.startsWith("sip")) {
			return Integer.parseInt(uri.substring(4, uri.indexOf('@')));
    	}else{
    		return 0;
    	}
	}
}
