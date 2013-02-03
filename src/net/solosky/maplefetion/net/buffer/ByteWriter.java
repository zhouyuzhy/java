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
 * File     : ByteWriter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.buffer;

/**
 *
 * 字节写入接口，方便移植程序
 *
 * @author solosky <solosky772@qq.com>
 */
public interface ByteWriter
{
	/**
	 * 写入一个字节数组
	 * @param bytes
	 */
	public abstract void write(byte[] bytes);

	/**
	 * 写入一个字节
	 * @param b
	 */
	public abstract void writeByte(int b);
	
	/**
	 * 写入一个字节数组
	 * @param bytes
	 * @param offset
	 * @param len
	 */
	public abstract void writeBytes(byte[] bytes, int offset, int len);
	
	/**
	 * 返回写入的字节数
	 * @return
	 */
	public abstract int size();

	/**
	 * 返回写入的字节数组
	 * @return
	 */
	public abstract byte[] toByteArray();
	
	/**
	 * 清空缓冲区
	 */
	public abstract void clear();

}