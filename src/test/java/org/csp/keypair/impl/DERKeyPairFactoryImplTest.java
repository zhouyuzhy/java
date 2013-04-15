package org.csp.keypair.impl;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import org.csp.exception.KeyPairException;

import junit.framework.TestCase;

public class DERKeyPairFactoryImplTest extends TestCase {

	private byte[] encodePrivateKey;
	private byte[] encodePublicKey;
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		keyGen.initialize(1024, random);
		KeyPair pair = keyGen.generateKeyPair();
		return pair;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		KeyPair keyPair = generateKeyPair();
		privateKey = keyPair.getPrivate();
		encodePrivateKey = keyPair.getPrivate().getEncoded();
		publicKey = keyPair.getPublic();
		encodePublicKey  = keyPair.getPublic().getEncoded();
	}
	
	public void testGeneratePrivateKey() throws KeyPairException{
		KeyPairFactoryImpl kpfi = new DERKeyPairFactoryImpl("");
		PrivateKey key = kpfi.generatePrivateKey(encodePrivateKey);
		assertEquals(privateKey.getAlgorithm(), key.getAlgorithm());
		assertTrue(Arrays.equals(privateKey.getEncoded(), key.getEncoded()));
		assertTrue(key instanceof RSAPrivateKey);
	}
	
	public void testGeneratePublicKey() throws KeyPairException{
		KeyPairFactoryImpl kpfi = new DERKeyPairFactoryImpl("");
		PublicKey key = kpfi.generatePublicKey(encodePublicKey);
		assertEquals(publicKey.getAlgorithm(), key.getAlgorithm());
		assertTrue(Arrays.equals(publicKey.getEncoded(), key.getEncoded()));
		assertTrue(key instanceof RSAPublicKey);
	}
	
}
