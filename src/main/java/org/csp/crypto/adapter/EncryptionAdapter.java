package org.csp.crypto.adapter;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.csp.crypto.Encryption;
import org.csp.crypto.IEncryption;
import org.csp.crypto.impl.AesEncryption;
import org.csp.crypto.impl.RsaEncryption;
import org.csp.exception.CryptoException;
import org.csp.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-ÈýÔÂ-2013 10:05:42
 */
public class EncryptionAdapter implements Encryption {

	public IEncryption encryption;

	public EncryptionAdapter(EncryptionType type) {
		switch (type) {
		case AES:
			this.encryption = new AesEncryption(null);
			break;
		case RSA:
			this.encryption = new RsaEncryption();
			break;
		default:
			throw new IllegalArgumentException("Not support for " + type);
		}
	}

	/**
	 * 
	 * @param key
	 * @param target
	 * @throws IOException
	 * @throws InvalidParamterException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public byte[] decryptBase64AsByte(Key key, String target)
			throws CryptoException {
		try {
			byte[] toDecrypt = Utils.base64Decode(target);
			return decryptByteAsByte(key, toDecrypt);
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	/**
	 * 
	 * @param key
	 * @param target
	 * @throws IOException
	 * @throws InvalidParamterException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public String decryptBase64AsString(Key key, String target)
			throws CryptoException {
		return new String(decryptBase64AsByte(key, target));
	}

	/**
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
	public byte[] decryptByteAsByte(Key key, byte[] target)
			throws CryptoException {
		return this.encryption.decrypt(key, target);
	}

	/**
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
	public String decryptByteAsString(Key key, byte[] target)
			throws CryptoException {
		return new String(decryptByteAsByte(key, target));
	}

	/**
	 * 
	 * @param key
	 * @param target
	 * @throws IOException
	 * @throws InvalidParamterException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public String encryptByteAsBase64(Key key, byte[] target)
			throws CryptoException {
		try {
			byte[] encrypted = encryptByteAsByte(key, target);
			return Utils.base64Encode(encrypted);
		} catch (IOException e) {
			throw new CryptoException(e);
		}
	}

	/**
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
	public byte[] encryptByteAsByte(Key key, byte[] target)
			throws CryptoException {
		return this.encryption.encrypt(key, target);
	}

	/**
	 * 
	 * @param key
	 * @param target
	 * @throws InvalidParamterException
	 * @throws IOException
	 * @throws InvalidAlgorithmParameterException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public String encryptStringAsBase64(Key key, String target)
			throws CryptoException {
		return encryptByteAsBase64(key, target.getBytes());
	}

	/**
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
	public byte[] encryptStringAsByte(Key key, String target)
			throws CryptoException {
		return encryptByteAsByte(key, target.getBytes());
	}

	@Override
	public byte[] getIv() {
		return this.encryption.getIv();
	}

	public static enum EncryptionType {
		AES, RSA
	}

}