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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.sip
 * File     : SIPMethod.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-19
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;

/**
 *	请求方法类
 *
 * @author solosky <solosky772@qq.com> 
 */
public interface SipcMethod
{
	public final static String BYE      = "B";
	public final static String ACK      = "A";
	public final static String INFO     = "IN";
	public final static String REGISTER = "R";
	public final static String SERVICE  = "S";
	public final static String INVATE   = "I";
	public final static String MESSAGE  = "M";
	public final static String OPTION   = "O";
	public final static String BENOTIFY = "BN";
	public final static String SUBSCRIBE = "SUB";
}
