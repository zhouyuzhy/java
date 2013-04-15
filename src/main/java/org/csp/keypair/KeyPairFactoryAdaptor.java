package org.csp.keypair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.csp.exception.KeyPairException;
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
	public PrivateKey generatePrivateKey(File keyFile) throws KeyPairException {
		InputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			return generatePrivateKey(is);
		} catch (FileNotFoundException e) {
			throw new KeyPairException(e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					throw new KeyPairException(e);
				}
		}

	}

	/**
	 * 
	 * @param keyInputStream
	 * @throws IOException
	 */
	public PrivateKey generatePrivateKey(InputStream keyInputStream)
			throws KeyPairException {
		try {
			return generatePrivateKey(Utils.readStreamToBytes(keyInputStream));
		} catch (IOException e) {
			throw new KeyPairException(e);
		}
	}

	/**
	 * 
	 * @param keyFilePath
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public PrivateKey generatePrivateKey(String keyFilePath)
			throws KeyPairException {
		return generatePrivateKey(new File(keyFilePath));
	}

	/**
	 * 
	 * @param keyInputStream
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public PublicKey generatePublicKey(InputStream keyInputStream)
			throws KeyPairException {
		try {
			return generatePublicKey(Utils.readStreamToBytes(keyInputStream));
		} catch (IOException e) {
			throw new KeyPairException(e);
		}
	}

	/**
	 * 
	 * @param key
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws KeyPairException {
		return this.keyPairFacotryImpl.generatePrivateKey(key);
	}

	/**
	 * 
	 * @param key
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public PublicKey generatePublicKey(byte[] key) throws KeyPairException {
		return this.keyPairFacotryImpl.generatePublicKey(key);
	}

	@Override
	public PublicKey generatePublicKey(File keyFile) throws KeyPairException {
		InputStream is = null;
		try {
			is = new FileInputStream(keyFile);
			return generatePublicKey(is);
		} catch (FileNotFoundException e) {
			throw new KeyPairException(e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					throw new KeyPairException(e);
				}
		}
	}

	@Override
	public PublicKey generatePublicKey(String keyFilePath)
			throws KeyPairException {
		return generatePublicKey(new File(keyFilePath));
	}

}