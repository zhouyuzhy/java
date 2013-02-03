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
 * File     : Transfer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import net.solosky.maplefetion.chain.Processor;


/**
*
*  SIP信令传输对象
*  
*  这个对象完成SIP信令的接受和发送
*
* @author solosky <solosky772@qq.com> 
*/
public interface Transfer extends Processor
{
	/**
	 * 启动传输
	 */
	public void startTransfer() throws TransferException;
	
	/**
	 * 停止传输
	 * @throws Exception 
	 */
	public void stopTransfer() throws TransferException;
	
	/**
	 * 返回这个传输对象的名字
	 * @return
	 */
	public String getTransferName();
	
}