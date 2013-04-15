package org.csp.client.service;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.csp.client.exception.ServiceException;
import org.csp.exception.CryptoException;
import org.csp.store.exception.LoginFailedException;

public interface IEncryptAndStoreService {
	/**
	 * 
	 * @param keys 存储键
	 * @param value 需要加密存储的值
	 * @param publicKey 用来加密的公钥
	 * @return
	 * @throws CryptoException 
	 * @throws LoginFailedException 
	 */
	public boolean encryptAndSave(String[] keys, String value, PublicKey publicKey) throws ServiceException;
	
	/**
	 * 
	 * @param keys 存储键
	 * @param privateKey 用来解密的私钥
	 * @return
	 * @throws LoginFailedException 
	 */
	public String getAndDecrypt(String[] keys, PrivateKey privateKey) throws ServiceException;
	
	/**
	 * 
	 * @param keys 存储键
	 * @return
	 * @throws ServiceException 
	 */
	public boolean exists(String[] keys) throws ServiceException;
	
	/**
	 * 第一维数组代表所有key，第二维数组代表单个key的所有子key
	 * @return
	 * @throws ServiceException 
	 */
	public String[][] listKeys() throws ServiceException;
}
