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
 * Package  : net.solosky.net.maplefetion.net.buffer
 * File     : ByteArrayReader.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.buffer;

import java.io.ByteArrayInputStream;

/**
 *
 * 读取字节数组类
 * 使用字节数组输入流读取
 *
 * @author solosky <solosky772@qq.com>
 */
public class ByteArrayReader implements ByteReader
{
	/**
	 * 字节流 
	 **/
	private ByteArrayInputStream in;
	
	/**
	 * 总共大小
	 */
	private int size;
	
	/**
	 * 构造函数
	 * @param byteArray
	 */
	public ByteArrayReader(byte[] byteArray, int len)
	{
		this.in   = new ByteArrayInputStream(byteArray, 0, len);
		this.size = byteArray.length; 
	}

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteReader#hasRemaining()
     */
    @Override
    public boolean hasRemaining()
    {
	   return this.in.available()!=0;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteReader#readByte()
     */
    @Override
    public byte readByte()
    {
    	return (byte) this.in.read();
    }
}
