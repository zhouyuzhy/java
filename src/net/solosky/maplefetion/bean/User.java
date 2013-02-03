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
 * Package  : net.solosky.maplefetion.bean
 * File     : FetionUser.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.solosky.maplefetion.util.AccountValidator;
import net.solosky.maplefetion.util.ConvertHelper;
import net.solosky.maplefetion.util.DigestHelper;

/**
 *
 *	飞信用户
 *	
 *	代表了一个登陆用户
 *
 * @author solosky <solosky772@qq.com> 
 */
public class User extends Buddy
{
	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 会话标志
	 */
	private String ssiCredential;
	
	/**
	 * 用于解密Credential的key
	 */
	private byte[] aesKey;
	
	/**
	 * 用于解密Credential的iv
	 */
	private byte[] aesIV;
	
	/**
	 * 存储版本，这是服务器上最新的版本
	 */
	private StoreVersion storeVersion;
	
	/**
	 * 详细的构造函数
	 * @param account	 	账号，可以是飞信号，手机号 ,Email（Email暂时不支持）
	 * @param password		密码
	 * @param domain		账号所在的域，默认为fetion.com.cn
	 */
	public User(String account, String password, String domain)
	{
		AccountValidator validator = new AccountValidator(account);
		if(validator.isValidEmail()){
			throw new UnsupportedOperationException("Sorry, the Fetion2008 Protocol does not supported Email sign in.");
		}else if(validator.isValidMobile()){
			this.mobile = validator.getMobile();
		}else if(validator.getFetionId()>0){
			this.fetionId = validator.getFetionId();
		}else{
			throw new IllegalStateException("Invalid account "+account+", it should be CMCC mobile number, FetionId or Registered Email.");
		}
		
		this.password = password;
		this.domain = domain;
		this.storeVersion = new StoreVersion();
		this.aesKey = DigestHelper.createAESKey();
		this.aesIV  = ConvertHelper.hexString2ByteNoSpace("00399F3D125DB5530AB5E000D6B0F45A");	//固定值
		
	}
	
	/**
     * @return the aesIV
     */
    public byte[] getAesIV()
    {
    	return aesIV;
    }



	/**
     * @return the password
     */
    public String getPassword()
    {
    	return password;
    }

	/**
     * @return the ssic
     */
    public String getSsiCredential()
    {
    	return ssiCredential;
    }

	/**
     * @param ssic the ssic to set
     */
    public void setSsiCredential(String ssic)
    {
    	this.ssiCredential = ssic;
    }

	/**
     * @return the storeVersion
     */
    public StoreVersion getStoreVersion()
    {
    	return storeVersion;
    }



	/**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
    	this.password = password;
    }


	/**
     * @return the aesKey
     */
    public byte[] getAesKey()
    {
    	return aesKey;
    }



	/**
     * @param storeVersion the storeVersion to set
     */
    public void setStoreVersion(StoreVersion storeVersion)
    {
    	this.storeVersion = storeVersion;
    }
    
    
}
