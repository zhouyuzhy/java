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
 * Package  : net.solosky.maplefetion.net
 * File     : MutiConnectionTransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import java.net.UnknownHostException;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;

import org.apache.log4j.Logger;

/**
 *
 *
 * 抽象的多可以提供多个独立连接的工厂
 * 实现了建立默认连接等方法
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public abstract class MutiConnectionTransferFactory implements TransferFactory
{
	/**
	 * 飞信上下文
	 */
	protected FetionContext context;
	
	/**
	 * 默认连接的本地端口
	 */
	private Port localport;
	
	/**
	 * 日志记录
	 */
	protected static Logger logger  = Logger.getLogger(MutiConnectionTransferFactory.class);
	
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#createDefaultTransfer()
     */
    @Override
    public Transfer createDefaultTransfer() throws TransferException
    {
    	//尝试建立Sipc-proxy连接
    	Transfer transfer   = null;
    	String sipcProxy    = this.context.getLocaleSetting().getNodeText("/config/servers/sipc-proxy");
    	String sipcSslProxy = this.context.getLocaleSetting().getNodeText("/config/servers/sipc-ssl-proxy");
    	
    	if(sipcProxy==null)    sipcProxy    = FetionConfig.getString("server.sipc-proxy");
    	if(sipcSslProxy==null) sipcSslProxy = FetionConfig.getString("server.sipc-ssl-proxy");
    	
    	transfer = this.tryCreateTransfer(sipcProxy);
    	//尝试建立sipc-proxy-ssl连接
    	if(transfer==null) 
    		transfer = this.tryCreateTransfer(sipcSslProxy);
        
    	if(transfer==null) {
    		//仍然建立失败，抛出异常
    		throw new TransferException("Couldn't create default transfer..");
    	}else {
    		//建立成功
    		this.localport = this.getLocalPort(transfer); 
    		return transfer;
    	}
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#getDefaultTransferLocalPort()
	 */
	@Override
	public Port getDefaultTransferLocalPort()
	{
		return this.localport;
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#isMutiConnectionSupported()
	 */
	@Override
	public boolean isMutiConnectionSupported()
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#closeFactory()
	 */
	@Override
	public void closeFactory()
	{
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#openFactory()
	 */
	@Override
	public void openFactory()
	{
	}

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#setFetionContext(net.solosky.maplefetion.FetionContext)
	 */
	@Override
	public void setFetionContext(FetionContext context)
	{
		this.context = context;
	}
	
	  /**
     * 尝试建立传输对象，建立失败不抛出异常
     * @param port
     * @return
     */
    protected Transfer tryCreateTransfer(String portstr)
    {
    	try {
	        return this.createTransfer(new Port(portstr));
        } catch (TransferException e) {
        	logger.warn("Connect to "+portstr+" failed!!");
        } catch (UnknownHostException e) {
        	logger.warn("Connect to "+portstr+" failed!!");
        }
        return null;
    }

	/**
	 * 返回一个传输对象的本地端口
	 * @param transfer
	 * @return
	 */
	protected abstract Port getLocalPort(Transfer transfer);

}
