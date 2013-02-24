package org.csp.keypair;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.csp.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:52
 */
public class KeyPairFactoryAdaptor extends KeyPairFactory implements
		IKeyPairFactory {

	/**
	 * 
	 * @param keyPairType
	 * @param password
	 */
	public KeyPairFactoryAdaptor(KeyPairType keyPairType, String password) {
		super(keyPairType, password);
	}

	/**
	 * 
	 * @param keyFile
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(File keyFile) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		InputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			return generatePrivateKey(is);
		}finally{
			if(is != null)
				is.close();
		}
		
	}

	/**
	 * 
	 * @param keyInputStream
	 * @throws IOException 
	 */
	public PrivateKey generatePrivateKey(InputStream keyInputStream) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return generatePrivateKey(Utils.readStreamToBytes(keyInputStream));
	}

	/**
	 * 
	 * @param keyFilePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(String keyFilePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		return generatePrivateKey(new File(keyFilePath));
	}

	/**
	 * 
	 * @param keyInputStream
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PublicKey generatePublicKey(InputStream keyInputStream) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		return generatePublicKey(Utils.readStreamToBytes(keyInputStream));
	}

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		return this.keyPairFacotryImpl.generatePrivateKey(key);
	}

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PublicKey generatePublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
		return this.keyPairFacotryImpl.generatePublicKey(key);
	}

	@Override
	public PublicKey generatePublicKey(File keyFile)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			IOException {
		InputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			return generatePublicKey(is);
		}finally{
			if(is != null)
				is.close();
		}
	}

	@Override
	public PublicKey generatePublicKey(String keyFilePath)
			throws NoSuchAlgorithmException, InvalidKeySpecException,
			IOException {
		return generatePublicKey(new File(keyFilePath));
	}

}