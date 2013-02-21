package org.test.privatekey;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 17-¶þÔÂ-2013 21:14:14
 */
public abstract class AbstractDecoratePrivateKeyFactory implements IPrivateKeyFactory {

	public IPrivateKeyFactory privateKeyFactory;

	/**
	 * 
	 * @param privateKeyFactory
	 */
	public AbstractDecoratePrivateKeyFactory(IPrivateKeyFactory privateKeyFactory){
		this.privateKeyFactory = privateKeyFactory;
	}

	/**
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(String filePath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		return this.privateKeyFactory.generatePrivateKey(filePath);
	}

	/**
	 * 
	 * @param file
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws FileNotFoundException 
	 */
	public PrivateKey generatePrivateKey(File file) throws FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		return this.privateKeyFactory.generatePrivateKey(file);
	}

	/**
	 * 
	 * @param is
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		return this.privateKeyFactory.generatePrivateKey(is);
	}
	
	@Override
	public PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return this.privateKeyFactory.generatePrivateKey(key);
	}

}