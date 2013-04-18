package org.csp.crypto.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.csp.crypto.ISymmetricalEncryption;
import org.csp.exception.CryptoException;
import org.csp.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-三月-2013 10:05:42
 */
public class AesEncryption extends BlockEncryption implements ISymmetricalEncryption {

	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private final byte[] iv;

	public AesEncryption(byte[] iv) {
		if (iv != null) {
			this.iv = iv;
		} else {
			this.iv = new byte[16];
			Utils.generateRandomString(this.iv);
		}
	}

	public byte[] getIv() {
		return iv;
	}

	/**
	 * 使用Key解密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] decrypt(Key key, byte[] target) throws CryptoException {
		return blocksCrypto(key, iv, target, 16, Cipher.DECRYPT_MODE, ALGORITHM);
	}

	/**
	 * 使用Key加密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] encrypt(Key key, byte[] target) throws CryptoException {
		return blocksCrypto(key, iv, target, 15, Cipher.ENCRYPT_MODE, ALGORITHM);
	}

}