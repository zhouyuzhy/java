package org.test.privatekey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import org.test.util.PrivateKeyUtils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 17-¶þÔÂ-2013 21:14:11
 */
public class DERPrivateKeyFactory implements IPrivateKeyFactory {

	public DERPrivateKeyFactory(){

	}

	/**
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(String filePath)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		return generatePrivateKey(new File(filePath));
	}

	/**
	 * 
	 * @param file
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(File file)
			throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		InputStream is = new FileInputStream(file);
		try{
			return generatePrivateKey(is);
		}finally{
			is.close();
		}
	}

	/**
	 * 
	 * @param reader
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 */
	public PrivateKey generatePrivateKey(InputStream is)
			throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		return generatePrivateKey(PrivateKeyUtils.readStreamToBytes(is));
	}
	
	@Override
	public PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeySpec keySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

}