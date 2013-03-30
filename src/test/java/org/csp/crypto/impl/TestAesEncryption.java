package org.csp.crypto.impl;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import junit.framework.TestCase;

import org.test.java.UtilForTest;

public class TestAesEncryption extends TestCase {

	

	public void testEncrypt() throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		Key key = UtilForTest.generateAesKey();
		AesEncryption enc = new AesEncryption(null);
		byte[] text = new byte[] { 0x00, 0x01, 0x02 };
		byte[] encrypted = enc.encrypt(key, text);
		
		byte[] ori = enc.decrypt(key, encrypted);
		UtilForTest.assertBytes(text, ori);
	}

}
