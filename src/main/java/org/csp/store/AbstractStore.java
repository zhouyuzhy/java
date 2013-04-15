package org.csp.store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-����-2013 21:42:46
 */
public abstract class AbstractStore implements IStore {

	/**
	 * ����ֵ�����ڼ�����
	 * 
	 * @param key
	 * @param value
	 * @throws StoreException
	 * @throws LoginFailedException
	 */
	public boolean save(Key key, Value value) throws StoreException,
			LoginFailedException {
		return save(key.toString(), value.getvalue());
	}

	protected abstract boolean save(String key, InputStream value)
			throws StoreException, LoginFailedException;

	/**
	 * �Ƿ����ָ��key�Ķ���
	 * 
	 * @param key
	 * @throws StoreException
	 */
	public boolean exist(Key key) throws StoreException, LoginFailedException {
		return exist(key.toString());
	}

	protected boolean exist(String key) throws StoreException,
			LoginFailedException{
		List<String> keys = listKeys();
		return keys.contains(key);
	}

	/**
	 * ��ȡָ������
	 * 
	 * @param key
	 *            ��ȡ�����Key
	 * @throws StoreException
	 */
	public Value get(Key key) throws StoreException, LoginFailedException {
		return new Value(get(key.toString()));
	}

	protected abstract InputStream get(String key) throws StoreException,
			LoginFailedException;

	/**
	 * �о�Ŀǰ�洢������key
	 * 
	 * @return ����Key
	 */
	public List<Key> list() throws StoreException, LoginFailedException {
		List<String> keyStrs = listKeys();
		List<Key> keys = new ArrayList<Key>();
		for (String keyStr : keyStrs) {
			keys.add(Key.fromString(keyStr));
		}
		return keys;
	}

	protected abstract List<String> listKeys() throws StoreException,
			LoginFailedException;

}