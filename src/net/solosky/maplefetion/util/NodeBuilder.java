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
 * File     : NodeBuilder.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-3-3
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.Hashtable;
import java.util.Iterator;

/**
 * 
 *  内部工具类，方便建立一个XML节点
 *
 * @author solosky <solosky772@qq.com>
 */
public class NodeBuilder
{
	private Hashtable<String, String> attrs;
	private String nodeName;
	
	public NodeBuilder(String nodeName)
	{
		this.nodeName = nodeName;
		this.attrs = new Hashtable<String, String>();
	}
	
	public NodeBuilder()
	{
		this(null);
	}
	
	/**
	 * 添加一组属性
	 * @param name
	 * @param value
	 */
	public void add(String name,String value)
	{
		this.attrs.put(name, value);
	}
	
	/**
	 * 转化为XML
	 */
	public String toXML(String nodeName)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("<"+nodeName);
		Iterator<String> it = this.attrs.keySet().iterator();
		while(it.hasNext()) {
			String name = it.next();
			String value = this.attrs.get(name);
			if(name!=null && value!=null) {
    			buffer.append(" ");
    			buffer.append(name);
    			buffer.append("=\"");
    			buffer.append(value);
    			buffer.append("\"");
			}
		}
		buffer.append(" />");
		
		return buffer.toString();
	}
	
	public String toXML()
	{
		return this.toXML(nodeName);
	}
}
