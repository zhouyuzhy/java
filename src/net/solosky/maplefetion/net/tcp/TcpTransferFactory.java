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
 * Package  : net.solosky.maplefetion.net.tcp
 * File     : TcpTransferFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.tcp;

import net.solosky.maplefetion.net.MutiConnectionTransferFactory;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.net.Transfer;
import net.solosky.maplefetion.net.TransferException;

/**
 *
 *	TCP连接工厂
 *
 * @author solosky <solosky772@qq.com>
 */
public class TcpTransferFactory extends MutiConnectionTransferFactory
{
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.TransferFactory#createTransfer(java.lang.String, int)
     */
    @Override
    public Transfer createTransfer(Port port) throws TransferException
    {
	   return new TcpTransfer(port);
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.net.MutiConnectionTransferFactory#getLocalPort(net.solosky.maplefetion.net.Transfer)
	 */
	@Override
	protected Port getLocalPort(Transfer transfer)
	{
		TcpTransfer trs = (TcpTransfer) transfer;
		return new Port(trs.getSocket().getLocalAddress(), trs.getSocket().getLocalPort()); 

	}


}
