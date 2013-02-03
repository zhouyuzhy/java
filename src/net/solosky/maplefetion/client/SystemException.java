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
 * File     : SystemException.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-11
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import net.solosky.maplefetion.FetionException;

/**
 *
 * 系统内部异常，抛出这个异常说明了不可恢复的错误，需结束整个客户端
 *
 * @author solosky <solosky772@qq.com>
 */
public class SystemException extends FetionException
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 参数数组，存放了发生这个异常的一些环境参数，可以交给CrushBuilder建立错误报告
     */
    private Object[] args;	
    
	public SystemException(String s, Object ...args) {
		super(s);
		this.args = args;
	}
	public SystemException(Throwable t, Object ...args) {
		super(t);
		this.args = args;
	}
	public Object[] getArgs()
	{
		return this.args;
	}
}
