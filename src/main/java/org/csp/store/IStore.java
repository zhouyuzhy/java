package org.csp.store;
import java.util.List;

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
	 */
	public boolean exist(Key key) throws StoreException;

	/**
	 * 
	 * @param key
	 * @throws StoreException 
	 */
	public Value get(Key key) throws StoreException;

	public List<Key> list();

	/**
	 * ����ֵ�����ڼ�����
	 * 
	 * @param key
	 * @param value
	 * @throws StoreException 
	 */
	public boolean save(Key key, Value value) throws StoreException;

}