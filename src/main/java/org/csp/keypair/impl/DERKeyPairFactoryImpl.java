package org.csp.keypair.impl;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.csp.exception.KeyPairException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:50
 */
public class DERKeyPairFactoryImpl extends KeyPairFactoryImpl {

	public DERKeyPairFactoryImpl(String password) {
		super(password);
	}

	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws KeyPairException {
		KeySpec keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyPairException(e);
		} catch (InvalidKeySpecException e) {
			throw new KeyPairException(e);
		}
	}

	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public PublicKey generatePublicKey(byte[] key) throws KeyPairException {
		try {
			KeySpec keySpec = new X509EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyPairException(e);
		} catch (InvalidKeySpecException e) {
			throw new KeyPairException(e);
		}
	}

}