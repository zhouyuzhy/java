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
 * Package  : net.solosky.maplefetion.util
 * File     : DigestHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *	
 *	加密算法工具
 *
 * @author solosky <solosky772@qq.com> 
 */
public class DigestHelper
{
	
	/**
	 * 计算MD5值
	 * @param bytes		须计算的字节数组
	 * @return			计算结果
	 */
	public static byte[] MD5(byte[] bytes)
	{
		return digest("MD5", bytes);
	}
	
	/**
	 * 计算SHA1值
	 * @param bytes		须计算的字节数组
	 * @return			计算结果
	 */
	public static byte[] SHA1(byte[] bytes)
	{
		return digest("SHA-1",bytes);
	}
	
	/**
	 * 计算HASH值
	 * @param type		Hash类型
	 * @param bytes		需计算的字节
	 * @return			HASH结果
	 */
	private static byte[] digest(String type, byte[] bytes)
	{
        try {
	   		MessageDigest dist = MessageDigest.getInstance(type);
		    return dist.digest(bytes);
        } catch (NoSuchAlgorithmException e) {
	      throw new IllegalArgumentException("Cannot find digest:"+type, e);
        }
	}
	
	/**
	 * 生成256位AES对称加密算法的密钥
	 * @return
	 */
	public static byte[] createAESKey() {
		try {
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        kgen.init(0x100);
	        SecretKey skey = kgen.generateKey();
	        return skey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
        	 throw new IllegalArgumentException("Cannot find key generator: AES", e);
        }
	}
	
	
	/**
	 * 使用AES算法解密数据，
	 * Note:默认的JDK不支持256位解密，需要从java官方网站下载 两个文件 US_export_policy.jar local_policy.jar 并替换$JDK_HOME/jre/lib/security下面的两个文件 
	 * https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewProductDetail-Start?ProductRef=jce_policy-6-oth-JPR@CDS-CDS_Developer
	 * 以后考虑自己实现256解密算法。。
	 * @param encrypted
	 * @param key			密钥为256位
	 * @param iv			加密矢量，增加加密强度
	 * @return
	 */
	public static byte[] AESDecrypt(byte[] encrypted, byte[] key, byte[] iv) {
		try {
	        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
	        IvParameterSpec ivSpec = new IvParameterSpec(iv);
	        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivSpec);
	        return cipher.doFinal(encrypted);
		} catch(InvalidKeyException e) {
			throw new IllegalArgumentException(
					"if you see 'java.security.InvalidKeyException: Illegal key size', it cause by default JCE does not support 256-aes key for shipping reason.\n" +
					"but you can download those two policy files" +
					"		(US_export_policy.jar,local_policy.jar : Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files) " +
					"and replace the same files in '$JDK_HOME/jre/lib/security',\n" +
					"then it still work well. I would implement AES/CBC/NoPadding algorithm to make things better in futrue.\n" +
					"download link:https://cds.sun.com/is-bin/INTERSHOP.enfinity/WFS/CDS-CDS_Developer-Site/en_US/-/USD/ViewProductDetail-Start?ProductRef=jce_policy-6-oth-JPR@CDS-CDS_Developer",
				e);
        } catch (Exception e) {
        	throw new IllegalArgumentException("AESDecrypt failed.", e);
        }
	}
}
