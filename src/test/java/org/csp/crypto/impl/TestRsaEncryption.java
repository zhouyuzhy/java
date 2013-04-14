package org.csp.crypto.impl;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import junit.framework.TestCase;

import org.csp.exception.CryptoException;
import org.test.java.UtilForTest;

public class TestRsaEncryption extends TestCase{
	
	
	private PrivateKey priKey;
	private PublicKey pubKey;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		KeyPair keyPair = UtilForTest.generateRsaKey();
		priKey = keyPair.getPrivate();
		pubKey = keyPair.getPublic();
	}
	
	public void testRsaEncrypt() throws CryptoException {
		RsaEncryption rsa = new RsaEncryption();
		byte[] text = new byte[] { 0x00, 0x01, 0x02 };
		byte[] encrypted = rsa.encrypt(priKey, text);
		byte[] ori = rsa.decrypt(pubKey, encrypted);
		UtilForTest.assertBytes(text, ori);
	}
}
