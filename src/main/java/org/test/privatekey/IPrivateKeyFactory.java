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
 * @created 17-¶þÔÂ-2013 21:14:09
 */
public interface IPrivateKeyFactory {

	/**
	 * 
	 * @param filePath
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public abstract PrivateKey generatePrivateKey(String filePath) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	public PrivateKey generatePrivateKey(File file) throws FileNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, IOException;

	/**
	 * 
	 * @param is
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 */
	public PrivateKey generatePrivateKey(InputStream is) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;

	/**
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeySpecException 
	 */
	public PrivateKey generatePrivateKey(byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException;
}