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
 * Package  : net.solosky.net.maplefetion.sipc
 * File     : SipcNotify.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.sipc;


/**
 * 
 * 从服务器主动发回的通知类
 *
 * @author solosky <solosky772@qq.com> 
 */
public class SipcNotify extends SipcInMessage
{
	/**
	 * 请求方法
	 */
	private String method;
	
	/**
	 * SID
	 * 按RFC来说这里应该是一个标准的URI，形如sip:1234@fetion.com.cn 但服务返回的只有1234。
	 */
	private int sid;

	/**
	 * 构造函数
	 * @param method		请求方法
	 * @param sid			SID
	 */
	
	public SipcNotify(String method, int sid)
	{
		this.method = method;
		this.sid    = sid;
	}
	
	/**
	 * 以请求头构造通知
	 * @param headline		请求头
	 */
	public SipcNotify(String headline)
	{
		//BN 685592830 SIP-C/2.0
    	int fb = headline.indexOf(' ');
    	int nb = headline.indexOf(' ', fb+1);
    	this.method = headline.substring(0,fb);			//通知方法
    	this.sid    = Integer.parseInt(headline.substring(fb+1,nb));		//SID
	}
	
	
	/**
	 * 返回通知方法
	 * @return		通知方法
	 */
	public String getMethod()
	{
		return this.method;
	}
	
	/**
	 * 返回SID
	 * @return		SID 
	 */
	public int getSid()
	{
		return this.sid;
	}
	
	public String toString()
	{
		return "[SIPNotify: M="+this.getMethod()+"; I:"+this.getCallID()+"; Q:"+this.getSequence()+"; L:"+this.getContentLength()+"]";
	}

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.sipc.SipcMessage#toHeadLine()
     */
    @Override
    protected String toHeadLine()
    {
	    return this.method+" "+Integer.toString(this.sid)+" "+SipcMessage.SIP_VERSION;
    }
}