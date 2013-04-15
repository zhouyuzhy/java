package org.csp.keypair;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.csp.exception.KeyPairException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:53
 */
public interface IKeyPairFactory {

	/**
	 * 
	 * @param keyFile
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(File keyFile) throws KeyPairException;

	/**
	 * 
	 * @param keyFilePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(String keyFilePath) throws KeyPairException;

	/**
	 * 
	 * @param key
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws KeyPairException;

	/**
	 * 
	 * @param keyInputStream
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(InputStream keyInputStream) throws KeyPairException;

	/**
	 * 
	 * @param key
	 */
	public PublicKey generatePublicKey(byte[] key) throws KeyPairException;

	/**
	 * 
	 * @param keyInputStream
	 */
	public PublicKey generatePublicKey(InputStream keyInputStream) throws KeyPairException;

	/**
	 * 
	 * @param key
	 */
	public PublicKey generatePublicKey(File keyFile) throws KeyPairException;

	
	/**
	 * 
	 * @param key
	 */
	public PublicKey generatePublicKey(String keyFilePath) throws KeyPairException;

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password);
}