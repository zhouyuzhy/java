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
 * Package  : net.solosky.maplefetion.net.mina
 * File     : MinaTransfer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-19
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.mina;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.net.AbstractTransfer;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.buffer.MinaBufferReader;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 *
 * Mina传输模式
 *
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class MinaTransfer extends AbstractTransfer
{

	/**
	 * IoSession
	 */
	private IoSession session;
	
	/**
	 * 是否是用户主动关闭了连接
	 */
	private boolean isUserClosed;
	
	
	/**
	 * 以一个IoSession构建传输对象
	 * @param session
	 */
	public MinaTransfer(IoSession session)
	{
		this.session = session;
		this.isUserClosed = false;
	}
	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.AbstractTransfer#sendBytes(byte[], int, int)
	 */
	@Override
	protected synchronized void sendBytes(byte[] buff, int offset, int len)
			throws TransferException
	{
		this.session.write(IoBuffer.wrap(buff, offset, len));
	}
	/**
	 * @return the session
	 */
	public IoSession getSession()
	{
		return session;
	}
	
	/**
	 * 接受到数据了
	 * @param tmpBuffer
	 */
	public void bufferReceived(IoBuffer tmpBuffer)
	{
		try {
	        this.processIncoming(new MinaBufferReader(tmpBuffer));
        } catch (FetionException e) {
	       	this.raiseException(e);
        }catch(Throwable t) {
        	this.raiseException(new SystemException(t, new String(tmpBuffer.array())));
        }
	}
	
	/**
	 * 有错误发生
	 */
	public void handleException(Throwable throwable)
	{
		this.raiseException(new TransferException(throwable));
	}
	
	/**
	 * 服务关闭了连接
	 */
	public void connectionClosed()
	{
		if(!this.isUserClosed)
			this.raiseException(new TransferException("Server closed connection.."));
	}
	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.AbstractTransfer#stopTransfer()
	 */
	@Override
	public void stopTransfer() throws TransferException
	{
		this.session.close(true);
		this.isUserClosed = true;
	}
}
