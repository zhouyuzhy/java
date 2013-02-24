package org.csp.keypair;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.csp.keypair.impl.DERKeyPairFactoryImpl;
import org.csp.keypair.impl.KeyPairFactoryImpl;
import org.csp.keypair.impl.OpensshKeyPairFactoryImpl;
import org.csp.keypair.impl.PEMKeyPairFactoryImpl;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:48
 */
public abstract class KeyPairFactory {

	public KeyPairFactoryImpl keyPairFacotryImpl;

	/**
	 * 
	 * @param keyPairType
	 * @param password
	 */
	public KeyPairFactory(KeyPairType keyPairType, String password){
		switch(keyPairType){
		case DER:
			this.keyPairFacotryImpl = new DERKeyPairFactoryImpl(password);
			break;
		case PEM:
			this.keyPairFacotryImpl = new PEMKeyPairFactoryImpl(password);
			break;
		case OPENSSH:
			this.keyPairFacotryImpl = new OpensshKeyPairFactoryImpl(password);
			break;
		}
	}

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public abstract PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	/**
	 * 
	 * @param key
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public abstract PublicKey generatePublicKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

}