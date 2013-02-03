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
 * Package  : net.solosky.net.maplefetion.net
 * File     : AbstractTransfer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import java.util.Arrays;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.chain.AbstractProcessor;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.net.buffer.ByteArrayReader;
import net.solosky.maplefetion.net.buffer.ByteWriter;

/**
 *
 * 抽象的传输类
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class AbstractTransfer extends AbstractProcessor implements Transfer
{
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#startProcessor()
     */
    @Override
    public void startProcessor() throws FetionException
    {
	    this.startTransfer();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#stopProcessor()
     */
    @Override
    public void stopProcessor() throws FetionException
    {
    	this.stopTransfer();
    }


	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Processor#getProcessorName()
     */
    @Override
    public String getProcessorName()
    {
    	return Transfer.class.getName();
    }
    
    /**
     * 传输对象已经读取字节后回调方法
     * 直接把这个对象交个下一个处理器，一般是parser
     * @param reader
     */
    protected void bytesRecived(byte[] buff, int offset, int len)
    {
    	try {
	        this.processIncoming(new ByteArrayReader(buff, len));
        } catch (FetionException e) {
	       	this.raiseException(e);
        }catch(Throwable t) {
        	this.raiseException(new SystemException(t, new String(Arrays.copyOfRange(buff, offset, offset+len))));
        }
    }
    
    /**
     * 发送数据包
     * @param buff
     * @param offset
     * @param len
     * @throws TransferException
     */
    protected abstract void sendBytes(byte[] buff, int offset, int len) throws TransferException;
    
    /**
     * 发送消息，直接交给子类发送
     */
    @Override
    public void processOutcoming(Object o) throws FetionException
    {
    	if(o instanceof ByteWriter) {
    		ByteWriter writer = (ByteWriter) o;
    		try {
	           this.sendBytes(writer.toByteArray(), 0, writer.size());
            } catch (TransferException e) {
            	this.raiseException(e);
            }
    	}
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.Transfer#startTransfer()
	 */
	@Override
	public void startTransfer() throws TransferException
	{
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.Transfer#stopTransfer()
	 */
	@Override
	public void stopTransfer() throws TransferException
	{
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.Transfer#getTransferName()
	 */
	@Override
	public String getTransferName()
	{
		return this.toString();
	}
    
    
}
