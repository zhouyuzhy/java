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
 * Package  : net.solosky.maplefetion.client
 * File     : ResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public interface ResponseHandler
{
	/**
	 * 处理消息
	 * @param message			消息对象
	 * @throws FetionException
	 */
	public void handle(SipcResponse response) throws FetionException;
	
	/**
	 * 回复超时
	 * @param request
	 * @throws FetionException
	 */
	public void timeout(SipcRequest request);
	
	/**
	 * 发送请求时错误
	 * @param request
	 * @throws FetionException
	 */
	public void ioerror(SipcRequest  request);
	
	/**
	 * 系统内部错误
	 * @param request
	 * @param throwable 
	 */
	public void syserror(SipcRequest request, Throwable throwable);
}
