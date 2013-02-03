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
 * Package  : net.solosky.maplefetion.util
 * File     : ParseHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-30
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import net.solosky.maplefetion.bean.Relation;


/**
 *
 *	工具类，解析XML到对象
 *
 * @author solosky <solosky772@qq.com> 
 */
public class ParseHelper
{
	
	public static String parseString(String s)
	{
		return s;
	}
	public static Long parseLong(String s)
	{
		try {
	        return Long.parseLong(s);
        } catch (NumberFormatException e) {
        	return new Long(0);
        }
	}
	
	public static String toString(String s)
	{
		return s;
	}
	
	public static String toLong(Long l)
	{
		return Long.toString(l);
	}
	
	public static Integer parseInteger(String s)
	{
		try {
	        return Integer.parseInt(s);
        } catch (NumberFormatException e) {
	        return new Integer(0);
        }
	}
	
	public static String toInteger(Integer i)
	{
		return Integer.toString(i);
	}

	public static Relation parseRelation(String r) throws ParseException 
	{
		return Relation.valueOf(Integer.parseInt(r));
	}
	
	public static String toRelation(Relation r)
	{
		return r.toString();
	}
}
