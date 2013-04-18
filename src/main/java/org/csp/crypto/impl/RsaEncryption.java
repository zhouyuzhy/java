package org.csp.crypto.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
public class RsaEncryption extends BlockEncryption implements IAsymmetricEncryption {

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
		if (!(key instanceof PrivateKey)) {
			throw new IllegalArgumentException(
					"Decryption key should be PrivateKey!");
		}
		return decrypt((PrivateKey) key, target);
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
	public byte[] decrypt(PrivateKey key, byte[] target) throws CryptoException {
		if (!(key instanceof RSAPrivateKey))
			throw new IllegalArgumentException("该公钥不支持RSA加密算法。");
		int blockSize = ((RSAPrivateKey) key).getModulus().bitLength() / 8;
		return blocksCrypto(key, null, target, blockSize, Cipher.DECRYPT_MODE, ALGORITHM);
	}

	/**
	 * 
	 * @param key
	 * @param target
	 */
	public byte[] encrypt(PublicKey key, byte[] target) throws CryptoException {
		if (!(key instanceof RSAPublicKey))
			throw new IllegalArgumentException("该公钥不支持RSA加密算法。");
		int blockSize = ((RSAPublicKey) key).getModulus().bitLength() / 8 - 11; // PKCS1Padding
		return blocksCrypto(key, null, target, blockSize, Cipher.ENCRYPT_MODE, ALGORITHM);
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
		if (!(key instanceof PublicKey)) {
			throw new IllegalArgumentException(
					"Encryption key should be PublicKey!");
		}
		return encrypt((PublicKey) key, target);
	}

	@Override
	public byte[] getIv() {
		return null;
	}

}