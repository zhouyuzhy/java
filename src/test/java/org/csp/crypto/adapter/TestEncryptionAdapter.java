package org.csp.crypto.adapter;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import junit.framework.TestCase;

import org.csp.crypto.adapter.EncryptionAdapter.EncryptionType;
import org.csp.exception.InvalidParamterException;
import org.test.java.UtilForTest;

public class TestEncryptionAdapter extends TestCase {

	private EncryptionAdapter aesEncryption;
	private EncryptionAdapter rsaEncryption;
	private Key aesKey;
	private PrivateKey priKey;
	private PublicKey pubKey;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		aesEncryption = new EncryptionAdapter(EncryptionType.AES);
		rsaEncryption = new EncryptionAdapter(EncryptionType.RSA);
		aesKey = UtilForTest.generateAesKey();
		priKey = UtilForTest.generateRsaKey().getPrivate();
		pubKey = UtilForTest.generateRsaKey().getPublic();
	}
	
	public void testDecryptBase64AsByte() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParamterException, IOException{
		byte[] text = new byte[]{0x00, 0x01, 0x02};
		String aesEncrypted = aesEncryption.encryptByteAsBase64(aesKey, text);
		byte[] ori = aesEncryption.decryptBase64AsByte(aesKey, aesEncrypted);
		UtilForTest.assertBytes(text, ori);
		
		String rsaEncrypted = rsaEncryption.encryptByteAsBase64(priKey, text);
		ori = rsaEncryption.decryptBase64AsByte(pubKey, rsaEncrypted);
		UtilForTest.assertBytes(text, ori);
	}
	
	public void testDecryptBase64AsString() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParamterException, IOException{
		String text = "abc";
		String aesEncrypted = aesEncryption.encryptStringAsBase64(aesKey, text);
		String ori = aesEncryption.decryptBase64AsString(aesKey, aesEncrypted);
		assertEquals(text, ori);
		
		aesEncrypted = rsaEncryption.encryptStringAsBase64(priKey, text);
		ori = rsaEncryption.decryptBase64AsString(pubKey, aesEncrypted);
		assertEquals(text, ori);
	}
	
	public void testDecryptByteAsString() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParamterException{
		String text = "abc";
		byte[] aesEncrypted = aesEncryption.encryptStringAsByte(aesKey, text);
		String ori = aesEncryption.decryptByteAsString(aesKey, aesEncrypted);
		assertEquals(text, ori);
		
		byte[] rsaEncrypted = rsaEncryption.encryptStringAsByte(priKey, text);
		ori = rsaEncryption.decryptByteAsString(pubKey, rsaEncrypted);
		assertEquals(text, ori);
	}
	
	public void testDecryptByteAsByte() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParamterException{
		byte[] text = new byte[]{0x00, 0x01, 0x02};
		byte[] aesEncrypted = aesEncryption.encryptByteAsByte(aesKey, text);
		byte[] ori = aesEncryption.decryptByteAsByte(aesKey, aesEncrypted);
		UtilForTest.assertBytes(text, ori);
		
		byte[] rsaEncrypted = rsaEncryption.encryptByteAsByte(priKey, text);
		ori = rsaEncryption.decryptByteAsByte(pubKey, rsaEncrypted);
		UtilForTest.assertBytes(text, ori);
	}
}
