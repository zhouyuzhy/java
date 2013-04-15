package org.csp.keypair.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import org.csp.exception.KeyPairException;

import sun.misc.BASE64Decoder;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 04-ÈýÔÂ-2013 22:56:51
 */
public class OpensshKeyPairFactoryImpl extends KeyPairFactoryImpl {


	public OpensshKeyPairFactoryImpl(String password) {
		super(password);
	}

	/**
	 * 
	 * @param key
	 */
	public PrivateKey generatePrivateKey(byte[] key){
		return null;
	}

	public static int decodeUInt32(byte[] key, int start_index){
		byte[] test = Arrays.copyOfRange(key, start_index, start_index + 4);
		return new BigInteger(test).intValue();
	}
	
	/**
	 * 
	 * @param key
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 * @throws IOException 
	 */
	public PublicKey generatePublicKey(byte[] key)  throws KeyPairException{
		String keyStr = new String(key);
		if(keyStr.startsWith("ssh-rsa")){
			String keyBase64 = keyStr.split(" ")[1];
			try {
				key = new BASE64Decoder().decodeBuffer(keyBase64);
			} catch (IOException e) {
				throw new KeyPairException(e);
			}
		}
		byte[] sshrsa = new byte[] { 0, 0, 0, 7, 's', 's', 'h', '-', 'r', 's',
		'a' };
		int start_index = sshrsa.length;
		/* Decode the public exponent */
		int len = decodeUInt32(key, start_index);
		start_index += 4;
		byte[] pe_b = new byte[len];
		for(int i= 0 ; i < len; i++){
			pe_b[i] = key[start_index++];
		}
		BigInteger pe = new BigInteger(pe_b);
		/* Decode the modulus */
		len = decodeUInt32(key, start_index);
		start_index += 4;
		byte[] md_b = new byte[len];
		for(int i = 0 ; i < len; i++){
			md_b[i] = key[start_index++];
		}
		BigInteger md = new BigInteger(md_b);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			KeySpec ks = new RSAPublicKeySpec(md, pe);
			return (RSAPublicKey) keyFactory.generatePublic(ks);
		} catch (NoSuchAlgorithmException e) {
			throw new KeyPairException(e);
		} catch (InvalidKeySpecException e) {
			throw new KeyPairException(e);
		}
	}

}