package org.csp.crypto.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.csp.crypto.IAsymmetricEncryption;
import org.csp.exception.CryptoException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-三月-2013 10:05:42
 */
public class RsaEncryption implements IAsymmetricEncryption {

	private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

	/**
	 * 使用Key解密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidParamterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public byte[] decrypt(Key key, byte[] target) throws CryptoException {
		if (!(key instanceof PublicKey)) {
			throw new IllegalArgumentException(
					"Decryption key should be PublicKey!");
		}
		return decrypt((PublicKey) key, target);
	}

	/**
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
	public byte[] decrypt(PublicKey key, byte[] target) throws CryptoException {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
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
		}
	}

	/**
	 * 
	 * @param key
	 * @param target
	 */
	public byte[] encrypt(PrivateKey key, byte[] target) throws CryptoException {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
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
		}
	}

	/**
	 * 使用Key加密字节数组
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidParamterException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public byte[] encrypt(Key key, byte[] target) throws CryptoException {
		if (!(key instanceof PrivateKey)) {
			throw new IllegalArgumentException(
					"Decryption key should be PublicKey!");
		}
		return encrypt((PrivateKey) key, target);
	}

	@Override
	public byte[] getIv() {
		return null;
	}

}