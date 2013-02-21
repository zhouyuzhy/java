package org.test.publickey;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import sun.misc.BASE64Decoder;


public class PublicKeyFactory {
	
	public static PublicKey generatePublicKey(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] result = new BASE64Decoder().decodeBuffer(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(result);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pubKey = kf.generatePublic(keySpec);
		return pubKey;
	}
	
}
