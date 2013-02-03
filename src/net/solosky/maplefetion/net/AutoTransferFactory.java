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
 * File     : AutoTransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-21
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import org.apache.log4j.Logger;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.net.http.HttpTransferFactory;
import net.solosky.maplefetion.net.tcp.TcpTransferFactory;

/**
 *
 * 自适应的传输工厂
 * 这个工厂尝试各种传输方式，直到找到一个可以成功建立连接的传输工厂
 *
 * @author solosky <solosky772@qq.com>
 */
public class AutoTransferFactory implements TransferFactory
{

	/**
	 * 当前的传输工厂
	 */
	private TransferFactory activeFactory;
	/**
	 * 飞信上下文
	 */
	private FetionContext context;
	
	/**
	 * Logger
	 */
	private static Logger logger = Logger.getLogger(AutoTransferFactory.class);
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#closeFactory()
     */
    @Override
    public void closeFactory()
    {
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#createDefaultTransfer()
     */
    @Override
    public Transfer createDefaultTransfer() throws TransferException
    {
	    //这个方法是核心，将会尝试建立TCP连接HTTP连接，如果都失败，抛出异常
    	//先尝试建立TCP连接
    	Transfer transfer = null;
    	try {
    		logger.debug("Try to create Tcp default transfer...");
	        this.activeFactory = new TcpTransferFactory();
	        this.activeFactory.setFetionContext(this.context);
	        this.activeFactory.openFactory();
	        transfer = this.activeFactory.createDefaultTransfer();
	        return transfer;
        } catch (TransferException e) {
        	this.activeFactory.closeFactory();
        	logger.warn("Create tcp default transfer failed!!!");
        }
        
        //下面尝试建立HTTP连接
        try {
        	logger.debug("Try to create Http default transfer...");
	        this.activeFactory = new HttpTransferFactory();
	        this.activeFactory.setFetionContext(this.context);
	        this.activeFactory.openFactory();
	        transfer = this.activeFactory.createDefaultTransfer();
	        return transfer;
        } catch (TransferException e) {
        	this.activeFactory.closeFactory();
        	logger.debug("Create Http default transfer failed!!!");
        	throw new TransferException("Cannot create a valid transfer!!!");
        }
    	
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#createTransfer(net.solosky.maplefetion.net.Port)
     */
    @Override
    public Transfer createTransfer(Port port) throws TransferException
    {
	    return this.activeFactory.createTransfer(port);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#getDefaultTransferLocalPort()
     */
    @Override
    public Port getDefaultTransferLocalPort()
    {
	    return this.activeFactory.getDefaultTransferLocalPort();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#isMutiConnectionSupported()
     */
    @Override
    public boolean isMutiConnectionSupported()
    {
	    return this.activeFactory.isMutiConnectionSupported();
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

}
