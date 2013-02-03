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
 * Package  : net.solosky.maplefetion.net.http
 * File     : HttpTransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-16
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.http;

import java.net.UnknownHostException;
import java.util.UUID;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.net.Transfer;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.TransferFactory;

/**
 *
 * HTTP连接传输工厂
 * 
 * HTTP连接时有且仅有一个活动的连接
 *
 * @author solosky <solosky772@qq.com>
 */
public class HttpTransferFactory implements TransferFactory
{

	private FetionContext context;
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
	    String pragma = "xz4BBcV"+UUID.randomUUID().toString();
	    String httpTunnel = this.context.getLocaleSetting().getNodeText("/config/servers/http-tunnel");
	    if(httpTunnel==null) httpTunnel = FetionConfig.getString("server.http-tunnel");
	    return new HttpTransfer(httpTunnel, this.context.getFetionUser().getSsiCredential(), pragma);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#createTransfer(net.solosky.maplefetion.net.Port)
     */
    @Override
    public Transfer createTransfer(Port port) throws TransferException
    {
    	throw new TransferException("HttpTransfer only support one active transfer..");
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#getDefaultTransferLocalPort()
     */
    @Override
    public Port getDefaultTransferLocalPort()
    {
    	try {
	        return new Port("127.0.0.1:8001");
        } catch (UnknownHostException e) {
	        return null;
        }
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#isMutiConnectionSupported()
     */
    @Override
    public boolean isMutiConnectionSupported()
    {
	    return false;
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
