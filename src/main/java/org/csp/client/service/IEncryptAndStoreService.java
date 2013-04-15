package org.csp.client.service;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.csp.client.exception.ServiceException;
import org.csp.exception.CryptoException;
import org.csp.store.exception.LoginFailedException;

public interface IEncryptAndStoreService {
	/**
	 * 
	 * @param keys �洢��
	 * @param value ��Ҫ���ܴ洢��ֵ
	 * @param publicKey �������ܵĹ�Կ
	 * @return
	 * @throws CryptoException 
	 * @throws LoginFailedException 
	 */
	public boolean encryptAndSave(String[] keys, String value, PublicKey publicKey) throws ServiceException;
	
	/**
	 * 
	 * @param keys �洢��
	 * @param privateKey �������ܵ�˽Կ
	 * @return
	 * @throws LoginFailedException 
	 */
	public String getAndDecrypt(String[] keys, PrivateKey privateKey) throws ServiceException;
	
	/**
	 * 
	 * @param keys �洢��
	 * @return
	 * @throws ServiceException 
	 */
	public boolean exists(String[] keys) throws ServiceException;
	
	/**
	 * ��һά�����������key���ڶ�ά���������key��������key
	 * @return
	 * @throws ServiceException 
	 */
	public String[][] listKeys() throws ServiceException;
}
