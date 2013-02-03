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
 * Package  : net.solosky.maplefetion.net
 * File     : TransferSession.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-22
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.Hashtable;

/**
 *
 *	传输会话
 * 
 *  在多个处理器和创建器共享数据
 *  作用域为每一个传输服务中
 *
 * @author solosky <solosky772@qq.com> 
 */
public class DialogSession
{
	private Hashtable<String, Object> hashTable;
	
	/**
	 * 默认构造函数
	 */
	public DialogSession()
	{
		hashTable = new Hashtable<String, Object>();
	}
	
	/**
	 * 返回指定名字的对象
	 * 
	 * @param name
	 *         对象名字
	 * @return
	 *        如果找到返回对象，否则返回null
	 */
    public synchronized Object getAttribute(String name)
    {
    	return hashTable.get(name);
    }

	/**
	 * 判断是否有对象
	 * 
	 * @param name
	 *       对象名字
	 * @return
	 *       存在返回true不存在返回false
	 */
    public synchronized boolean hasAtrribute(String name)
    {
    	return hashTable.containsKey(name);
    }

    /**
	 * 删除对象
	 * 
	 * @param name
	 *       对象名字
	 */
    public synchronized void removeAttribute(String name)
    {
    	 hashTable.remove(name);
	    
    }

    /**
	 * 保存对象
	 * 
	 * @param name
	 *         对象名字
	 * @param o
	 *        保存的对象
	 */
    public synchronized void setAttribute(String name, Object o)
    {
    	hashTable.put(name, o);
    }
}
