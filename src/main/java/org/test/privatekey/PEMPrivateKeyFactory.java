package org.test.privatekey;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.test.util.PrivateKeyUtils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 17-¶þÔÂ-2013 21:14:13
 */
public class PEMPrivateKeyFactory extends AbstractDecoratePrivateKeyFactory {

	private String password;

	/**
	 * 
	 * @param privateKeyFactory
	 */
	public PEMPrivateKeyFactory(String password,
			IPrivateKeyFactory privateKeyFactory) {
		super(privateKeyFactory);
		this.password = password;
	}

	/**
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		return generatePrivateKey(new File(filePath));
	}

	/**
	 * 
	 * @param file
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(File file) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		InputStream is = new FileInputStream(file);
		try{
			return generatePrivateKey(is);
		}finally{
			is.close();
		}
	}

	/**
	 * 
	 * @param is
	 * @throws IOException
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] key = PrivateKeyUtils.readStreamToBytes(is);
		String keyStr = new String(key);
		if(!PrivateKeyUtils.isPem(keyStr)){
			return super.generatePrivateKey(key);
		}
		Security.addProvider(new BouncyCastleProvider());
		ByteArrayInputStream bais = new ByteArrayInputStream(key);
		PEMReader reader = new PEMReader(new InputStreamReader(bais), new PasswordFinder() {
			
			@Override
			public char[] getPassword() {
				return PEMPrivateKeyFactory.this.password.toCharArray();
			}
		});
		KeyPair keyPair = (KeyPair) reader.readObject();
		reader.close();
		return keyPair.getPrivate();
	}
	
}