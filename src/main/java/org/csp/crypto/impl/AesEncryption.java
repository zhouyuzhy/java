package org.csp.crypto.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.csp.crypto.ISymmetricalEncryption;
import org.csp.exception.CryptoException;
import org.csp.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-三月-2013 10:05:42
 */
public class AesEncryption implements ISymmetricalEncryption {

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
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			return cipher.doFinal(target);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		} catch (NoSuchPaddingException e) {
			throw new CryptoException(e);
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		} catch (IllegalBlockSizeException e) {
			throw new CryptoException(e);
		} catch (BadPaddingException e) {
			throw new CryptoException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 使用Key加密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] encrypt(Key key, byte[] target) throws CryptoException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
			return cipher.doFinal(target);
		} catch (NoSuchAlgorithmException e) {
			throw new CryptoException(e);
		} catch (NoSuchPaddingException e) {
			throw new CryptoException(e);
		} catch (InvalidKeyException e) {
			throw new CryptoException(e);
		} catch (IllegalBlockSizeException e) {
			throw new CryptoException(e);
		} catch (BadPaddingException e) {
			throw new CryptoException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
	}

}