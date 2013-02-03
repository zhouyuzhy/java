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
 * File     : Port.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-29
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * 一个连接端口，通常由一个主机地址和端口组成
 *
 * @author solosky <solosky772@qq.com>
 */
public class Port
{
	//地址
	private InetAddress address;
	//端口
	private int port;
	
	/**
	 * 默认的构造函数
	 */
	public Port()
	{
	}
	
	/**
	 * 可以用这样的格式来构造这个端口对象
	 * 221.176.31.108:8080
	 * @throws UnknownHostException 
	 */
	public Port(String s) throws UnknownHostException
	{
		parse(s);
	}
	
	/**
	 * 或者使用主机名和端口构造
	 * @param host
	 * @param port
	 * @throws UnknownHostException 
	 */
	public Port(String host, int port) throws UnknownHostException
	{
		this.address = Inet4Address.getByName(host);
		this.port = port;
	}
	
	/**
	 * 使用地址对象和端口构造
	 * @param address
	 * @param port
	 */
	public Port(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}
	
	/**
	 * 解析这样的格式
	 * 221.176.31.108:8080
	 * @throws UnknownHostException 
	 */
	public void parse(String s) throws UnknownHostException
	{
		int i = s.indexOf(':');
		if(i==-1) throw new IllegalArgumentException();
		this.address = InetAddress.getByName(s.substring(0,i));
		this.port    = Integer.parseInt(s.substring(i+1));
	}

	/**
     * @return the address
     */
    public InetAddress getAddress()
    {
    	return address;
    }

	/**
     * @return the port
     */
    public int getPort()
    {
    	return port;
    }
	
	public String toString()
	{
		return this.address.getHostAddress()+":"+Integer.toString(this.port);
	}
	
	
}
