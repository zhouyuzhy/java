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
 * Package  : net.solosky.maplefetion.net.buffer
 * File     : MinaBufferReader.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-19
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.buffer;

import org.apache.mina.core.buffer.IoBuffer;

/**
 *
 * 作为Mina提供的IoBuffer做的一个读适配
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class MinaBufferReader implements ByteReader
{
	/**
	 * IoBuffer
	 */
	private IoBuffer ioBuffer;
	
	/**
	 * 
	 * @param buff
	 */
	public MinaBufferReader(IoBuffer buff)
	{
		this.ioBuffer =buff;
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.buffer.ByteReader#hasRemaining()
	 */
	@Override
	public boolean hasRemaining()
	{
		return this.ioBuffer.hasRemaining();
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.buffer.ByteReader#readByte()
	 */
	@Override
	public byte readByte()
	{
		return this.ioBuffer.get();
	}
}
