package org.csp.keypair.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.csp.exception.KeyPairException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:49
 */
public class KeyPairFactoryImpl {

	protected String password;
	/**
	 * 
	 * @param password
	 */
	public KeyPairFactoryImpl(String password){
		this.password = password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws IOException 
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws KeyPairException{
		return null;
	}

	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws IOException 
	 */
	public PublicKey generatePublicKey(byte[] key) throws KeyPairException{
		return null;
	}



}