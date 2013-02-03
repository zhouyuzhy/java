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
 * File     : ByteArrayWriter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.buffer;



/**
 *	
 *	使用字节来写入数组
 *
 * @author solosky <solosky772@qq.com>
 */
public class ByteArrayWriter implements ByteWriter
{
	/**
	 * 字节数组
	 */
	private byte[]  buffer;
	
	/**
	 * 数据大小
	 */
	private int size;
	
	/**
	 * 构造函数
	 */
	public ByteArrayWriter()
	{
		this.buffer = new byte[255];
		this.size    = 0; 
	}
	
	/**
	 * 以一个数组构造
	 * @param bytes
	 * @param len
	 */
	public ByteArrayWriter(byte[] bytes)
	{
		this.buffer = bytes;
		this.size  = bytes.length;
	}

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteWriter#size()
     */
    @Override
    public int size()
    {
	    return this.size;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteWriter#toByteArray()
     */
    @Override
    public byte[] toByteArray()
    {
	   byte[] newBytes = new byte[this.size];
	   System.arraycopy(this.buffer, 0, newBytes, 0, this.size);
	   return newBytes;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteWriter#write(byte[])
     */
    @Override
    public void write(byte[] bytes)
    {
    	this.compact(bytes.length);
    	System.arraycopy(bytes, 0, this.buffer, this.size, bytes.length);
    	this.size += bytes.length;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteWriter#writeByte(int)
     */
    @Override
    public void writeByte(int b)
    {
    	this.compact(1);
	    this.buffer[this.size++] = (byte) b;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.net.buffer.ByteWriter#clear()
     */
    @Override
    public void clear()
    {
	    this.size = 0;
    }
    
    
    private void compact(int needSize)
    {
    	if(buffer.length-this.size< needSize) {
    		int nextSize = this.buffer.length;
        	while(nextSize<this.buffer.length+needSize) {
        		nextSize+=255;
        	}
    		byte[] newBytes = new byte[nextSize];
    		System.arraycopy(this.buffer, 0, newBytes, 0, this.size);
    		this.buffer = newBytes;
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.buffer.ByteWriter#writerBytes(byte[], int, int)
     */
    @Override
    public void writeBytes(byte[] bytes, int offset, int len)
    {
    	this.compact(len);
    	System.arraycopy(bytes, offset, this.buffer, this.size, len);
    	this.size += len;
    }
}
