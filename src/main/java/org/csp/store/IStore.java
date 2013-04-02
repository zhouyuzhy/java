package org.csp.store;
import java.util.List;

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
	 */
	public boolean exist(Key key);

	/**
	 * 
	 * @param key
	 */
	public Value get(Key key);

	public List<Key> list();

	/**
	 * ����ֵ�����ڼ�����
	 * 
	 * @param key
	 * @param value
	 */
	public boolean save(Key key, Value value);

}