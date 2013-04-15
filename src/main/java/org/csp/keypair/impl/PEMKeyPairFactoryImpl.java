package org.csp.keypair.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.csp.exception.KeyPairException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:58:34
 */
public class PEMKeyPairFactoryImpl extends KeyPairFactoryImpl {


	public PEMKeyPairFactoryImpl(String password) {
		super(password);
		Security.addProvider(new BouncyCastleProvider());
	}

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws KeyPairException{
		KeyPair keyPair = generateKeyPairFromPem(key);
		return keyPair.getPrivate();
	}

	/**
	 * @param key
	 * @return
	 * @throws IOException
	 */
	protected KeyPair generateKeyPairFromPem(byte[] key) throws KeyPairException {
		ByteArrayInputStream bais = new ByteArrayInputStream(key);
		PEMReader reader = new PEMReader(new InputStreamReader(bais), new PasswordFinder() {
			
			@Override
			public char[] getPassword() {
				return PEMKeyPairFactoryImpl.this.password.toCharArray();
			}
		});
		KeyPair keyPair;
		try {
			keyPair = (KeyPair) reader.readObject();
			reader.close();
		} catch (IOException e) {
			throw new KeyPairException(e);
		}
		
		return keyPair;
	}

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 */
	public PublicKey generatePublicKey(byte[] key) throws KeyPairException{
		KeyPair keyPair = generateKeyPairFromPem(key);
		return keyPair.getPublic();
	}

}