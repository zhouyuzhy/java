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
 * File     : RegistrationException.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-17
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import net.solosky.maplefetion.FetionException;

/**
 *
 * 服务器发回的注册异常
 * 比如用户在其他地方登陆了服务器就会发送Deregistered通知
 * 为了统一释放资源，把服务器发乎的注册事件当做异常来处理
 *
 * @author solosky <solosky772@qq.com>
 */
public class RegistrationException extends FetionException
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户在其他地方登陆
     */
    public static final int  DEREGISTERED = 0x1;
    
    /**
     * 服务器关闭了连接
     */
    public static final int DISCONNECTED = 0x2;
	/**
	 * 注册类型 上面定义的静态常量
	 */
	private int registrationType;
	
	public RegistrationException(int registrationType)
	{
		this.registrationType = registrationType;
	}

	/**
     * @return the registrationType
     */
    public int getRegistrationType()
    {
    	return registrationType;
    }
	
	
}
