package org.csp.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.csp.exception.CryptoException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-三月-2013 10:05:42
 */
public interface IEncryption {

	/**
	 * 使用Key解密字节数组
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decrypt(Key key, byte[] target)  throws CryptoException;

	/**
	 * 使用Key加密字节数组
	 * @param key
	 * @param target
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidAlgorithmParameterException
	 * @throws InvalidParamterException
	 */
	public byte[] encrypt(Key key, byte[] target)  throws CryptoException;

	/**
	 * 获取对称加密的IV向量，非对称返回NULL
	 * @return
	 */
	public byte[] getIv();
}