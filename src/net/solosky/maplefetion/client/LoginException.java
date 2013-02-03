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
 * File     : LoginException.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-17
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.LoginState;

/**
 *
 * 登录异常，在登录失败时抛出
 *
 * @author solosky <solosky772@qq.com>
 */
public class LoginException extends FetionException
{
    private static final long serialVersionUID = 1L;
    private LoginState state;
    
    public LoginException(LoginState state)
    {
    	this.state = state;
    }
    
    
    
    /**
     * @param e
     * @param state
     */
    public LoginException(LoginState state, Throwable e)
    {
	    super(e);
	    this.state = state;
    }



	public LoginState getState()
    {
    	return this.state;
    }
    
    
	
}
