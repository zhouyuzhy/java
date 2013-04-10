package org.csp.store;
import java.util.List;

import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-����-2013 21:42:44
 */
public interface IStore {

	/**
	 * �Ƿ����ָ��key�Ķ���
	 * 
	 * @param key
	 * @throws StoreException 
	 * @throws LoginFailedException 
	 */
	public boolean exist(Key key) throws StoreException, LoginFailedException;

	/**
	 * 
	 * @param key
	 * @throws StoreException 
	 * @throws LoginFailedException 
	 */
	public Value get(Key key) throws StoreException, LoginFailedException;

	public List<Key> list() throws StoreException, LoginFailedException;

	/**
	 * ����ֵ�����ڼ�����
	 * 
	 * @param key
	 * @param value
	 * @throws StoreException 
	 * @throws LoginFailedException 
	 */
	public boolean save(Key key, Value value) throws StoreException, LoginFailedException;

}