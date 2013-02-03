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
 * File     : MessageTimeoutException.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-8
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.sipc.SipcOutMessage;
import net.solosky.maplefetion.sipc.SipcRequest;


/**
*
* 发送SIP信令在指定的时间没有回复就触发超时异常
*
* @author solosky <solosky772@qq.com>
*/
public class RequestTimeoutException extends FetionException
{
   private static final long serialVersionUID = 4681144597971987214L;
   private SipcRequest request;
   
   /**
    * 默认构造函数
    */
   public RequestTimeoutException()
   {
   }
   
   /**
    * 以一个发出包作为构造函数
    * @param outMessage
    */
   public RequestTimeoutException(SipcRequest request)
   {
	   this.request = request;
   }
   
   /**
    * 返回超时的消息 
    * @return
    */
   public SipcOutMessage getTimeoutRequest()
   {
   		return this.request;
   }
}