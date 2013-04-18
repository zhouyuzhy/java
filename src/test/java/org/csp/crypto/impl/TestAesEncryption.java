package org.csp.crypto.impl;

import java.security.Key;

import junit.framework.TestCase;

import org.csp.exception.CryptoException;
import org.test.java.UtilForTest;

public class TestAesEncryption extends TestCase {

	

	public void testEncrypt() throws CryptoException{
		Key key = UtilForTest.generateAesKey();
		AesEncryption enc = new AesEncryption(null);
		byte[] text = new byte[] { 0x00, 0x01, 0x02 };
		byte[] encrypted = enc.encrypt(key, text);
		byte[] ori = enc.decrypt(key, encrypted);
		UtilForTest.assertBytes(text, ori);
	}

	public void testEncryptMoreThan128Byte() throws CryptoException {
		Key key = UtilForTest.generateAesKey();
		AesEncryption enc = new AesEncryption(null);
		int size = 17;
		byte[] text = new byte[size];
		for (int i = 0; i < size; i++)
			text[i] = (byte)i;
		byte[] encrypted = enc.encrypt(key, text);
		byte[] ori = enc.decrypt(key, encrypted);
		UtilForTest.assertBytes(text, ori);
	}
	
}
