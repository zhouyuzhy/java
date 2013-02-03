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
 * Project  : MapleFetion-2.5.0
 * Package  : net.solosky.maplefetion.bean
 * File     : Credential.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-9-22
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 *
 * 凭证，可能和单点登录有关
 *
 * @author solosky <solosky772@qq.com>
 */
public class Credential
{

	/**
	 * 所属的域
	 */
	private String domain;
	
	/**
	 * 凭证
	 */
	private String credential;
	
	/**
     * @param domain
     * @param credential
     */
    public Credential(String domain, String credential)
    {
	    super();
	    this.domain = domain;
	    this.credential = credential;
    }
    
    public Credential() {
    	
    }

	/**
     * @return the domain
     */
    public String getDomain()
    {
    	return domain;
    }

	/**
     * @param domain the domain to set
     */
    public void setDomain(String domain)
    {
    	this.domain = domain;
    }

	/**
     * @return the credential
     */
    public String getCredential()
    {
    	return credential;
    }

	/**
     * @param credential the credential to set
     */
    public void setCredential(String credential)
    {
    	this.credential = credential;
    }
	
}
