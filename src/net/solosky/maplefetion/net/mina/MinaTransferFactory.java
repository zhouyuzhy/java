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
 * File     : MinaTransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-19
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.mina;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;

import net.solosky.maplefetion.net.MutiConnectionTransferFactory;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.net.Transfer;
import net.solosky.maplefetion.net.TransferException;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * Mina传输工厂
 * 可以使用Mina在同时登陆多个客户端的时候提高效率
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class MinaTransferFactory extends MutiConnectionTransferFactory
{
	/**
	 * Connector
	 */
	private NioSocketConnector connector;
	
	
	/**
	 * 以一个执行器构造
	 * @param executor		线程执行器
	 */
	public MinaTransferFactory(Executor executor)
	{
		this.connector = new NioSocketConnector(new  NioProcessor(executor));
		this.connector.setHandler(new MinaNioHandler());
	}
	
	/**
	 * 以一个NioSocketConnector构建
	 * @param connector
	 */
	public MinaTransferFactory(NioSocketConnector connector)
	{
		this.connector = connector;
	}


	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.MutiConnectionTransferFactory#getLocalPort(net.solosky.maplefetion.net.Transfer)
	 */
	@Override
	protected Port getLocalPort(Transfer transfer)
	{
		MinaTransfer ts = (MinaTransfer) transfer;
		InetSocketAddress addr = (InetSocketAddress) ts.getSession().getLocalAddress();
		return new Port(addr.getAddress(), addr.getPort());
	}


	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.TransferFactory#createTransfer(net.solosky.maplefetion.net.Port)
	 */
	@Override
	public Transfer createTransfer(Port port) throws TransferException
	{
		 ConnectFuture cf = connector.connect(new InetSocketAddress(port.getAddress(), port.getPort()));
		 
		 //等待连接结果
    	 try {
			cf.await();
		} catch (InterruptedException e) {
			throw new TransferException(e);
		}
		
		//判断是否连接成功，如果不成功抛出异常
		if(!cf.isConnected())
			throw new TransferException("Connecting to "+port+" failed..");
		
		Transfer transfer = new MinaTransfer(cf.getSession());
		cf.getSession().setAttribute(MinaTransfer.class, transfer);
		
    	 return transfer;
	}
	
	/**
	 * 真正的关闭连接池，释放资源
	 * 因为Mina是为了在登陆多个客户端时提高效率的，所以一个客户端关闭时，并不关闭这个工厂
	 * 需要用户手动的关闭这个工厂
	 */
	public void reallyCloseFactory()
	{
		this.connector.dispose();
	}
}
