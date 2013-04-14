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
public interface ISymmetricalEncryption extends IEncryption {

	/**
	 * 使用Key解密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidAlgorithmParameterException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public byte[] decrypt(Key key, byte[] target)  throws CryptoException;

	public byte[] getIv();
	
	/**
	 * 使用Key加密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidAlgorithmParameterException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidKeyException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public byte[] encrypt(Key key, byte[] target) throws CryptoException;

}