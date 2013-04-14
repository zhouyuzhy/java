package org.csp.crypto;

import java.security.Key;

import org.csp.exception.CryptoException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-三月-2013 10:05:42
 */
public interface Encryption {

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decryptBase64AsByte(Key key, String target) throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String decryptBase64AsString(Key key, String target) throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decryptByteAsByte(Key key, byte[] target)  throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String decryptByteAsString(Key key, byte[] target)  throws CryptoException;

	/**
	 * 加密字节数组，返回Base64
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String encryptByteAsBase64(Key key, byte[] target)  throws CryptoException;

	/**
	 * 加密字节数组，返回字节数组
	 * @param key 密钥对
	 * @param target 加密的字节数组
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encryptByteAsByte(Key key, byte[] target)  throws CryptoException;

	/**
	 * 加密字符串，返回Base64编码
	 * @param key 加密密钥对
	 * @param target 加密的字符串
	 * @return
	 * @throws CryptoException
	 */
	public String encryptStringAsBase64(Key key, String target)  throws CryptoException;

	/**
	 * 加密字符串，返回字节数组
	 * @param key 加密的密钥对
	 * @param target 加密的字符串
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encryptStringAsByte(Key key, String target)  throws CryptoException;

	/**
	 * 获取对称加密中使用的IV向量，非对称返回NULL
	 * @return
	 */
	public byte[] getIv();
}