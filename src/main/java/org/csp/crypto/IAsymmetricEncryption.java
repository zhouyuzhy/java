package org.csp.crypto;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.csp.exception.CryptoException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-ÈýÔÂ-2013 10:05:42
 */
public interface IAsymmetricEncryption extends IEncryption {

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decrypt(Key key, byte[] target) throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decrypt(PublicKey key, byte[] target)  throws CryptoException;

	/**
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encrypt(Key key, byte[] target)  throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encrypt(PrivateKey key, byte[] target)  throws CryptoException;

	/**
	 * 
	 */
	public byte[] getIv();
}