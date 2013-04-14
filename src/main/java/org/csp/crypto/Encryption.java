package org.csp.crypto;

import java.security.Key;

import org.csp.exception.CryptoException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 30-����-2013 10:05:42
 */
public interface Encryption {

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decryptBase64AsByte(Key key, String target) throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String decryptBase64AsString(Key key, String target) throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public byte[] decryptByteAsByte(Key key, byte[] target)  throws CryptoException;

	/**
	 * 
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String decryptByteAsString(Key key, byte[] target)  throws CryptoException;

	/**
	 * �����ֽ����飬����Base64
	 * @param key
	 * @param target
	 * @return
	 * @throws CryptoException
	 */
	public String encryptByteAsBase64(Key key, byte[] target)  throws CryptoException;

	/**
	 * �����ֽ����飬�����ֽ�����
	 * @param key ��Կ��
	 * @param target ���ܵ��ֽ�����
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encryptByteAsByte(Key key, byte[] target)  throws CryptoException;

	/**
	 * �����ַ���������Base64����
	 * @param key ������Կ��
	 * @param target ���ܵ��ַ���
	 * @return
	 * @throws CryptoException
	 */
	public String encryptStringAsBase64(Key key, String target)  throws CryptoException;

	/**
	 * �����ַ����������ֽ�����
	 * @param key ���ܵ���Կ��
	 * @param target ���ܵ��ַ���
	 * @return
	 * @throws CryptoException
	 */
	public byte[] encryptStringAsByte(Key key, String target)  throws CryptoException;

	/**
	 * ��ȡ�ԳƼ�����ʹ�õ�IV�������ǶԳƷ���NULL
	 * @return
	 */
	public byte[] getIv();
}