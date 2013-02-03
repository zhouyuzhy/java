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
 * Package  : net.solosky.maplefetion.util
 * File     : CallHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-9
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.Random;

import net.solosky.maplefetion.sipc.SipcRequest;

/**
 *
 * 一般来说，群对话，第一版的聊天会话就是一次CALL
 * 在一个CALL的过程中，每次请求CALL的编号（I-callId）不变，但序号(Q-sequence)会不断增加
 * 
 * 服务器会话的CALL有点特别，好像还没有找到规律，callId的编号是随每一次发送请求自动加一，
 * 如果连续两次请求的方法都是一样的，callId不变，sequence加一，总体说来保持唯一就行了
 *
 * 这个类是帮助管理在一个CALL中发出请求的序号管理
 * @author solosky <solosky772@qq.com>
 */
public class CallHelper
{
	/**
	 * callId
	 */
	private int callId;
	
	/**
	 * sequence
	 */
	private int sequence;
	
	/**
	 * 可以传递一个CallId
	 * @param callId
	 */
	public CallHelper(int callId)
	{
		this.callId = callId;
		this.sequence = 1;
	}
	
	/**
	 * 随机生成CallId
	 */
	public CallHelper()
	{
		this.callId = Math.abs(new Random().nextInt());
		this.sequence = 1;
	}
	
	/**
	 * 设置请求的callId和Sequence
	 * @param request
	 * @return
	 */
	public SipcRequest set(SipcRequest request)
	{
		request.setCallID(this.callId);
		request.setSequence((++sequence)+" "+request.getMethod());
		return request;
	}
}
