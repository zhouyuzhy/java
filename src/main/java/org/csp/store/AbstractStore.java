package org.csp.store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	 */
	public boolean save(Key key, Value value){
		return save(key.toString(), value.getvalue());
	}
	
	protected abstract boolean save(String key, InputStream value);

	/**
	 * �Ƿ����ָ��key�Ķ���
	 * 
	 * @param key
	 */
	public boolean exist(Key key){
		return exist(key.toString());
	}
	
	protected abstract boolean exist(String key);

	/**
	 * ��ȡָ������
	 * @param key ��ȡ�����Key
	 */
	public Value get(Key key){
		return new Value(get(key.toString()));
	}

	protected abstract InputStream get(String key);
	/**
	 * �о�Ŀǰ�洢������key
	 * @return ����Key
	 */
	public List<Key> list(){
		List<String> keyStrs = listKeys();
		List<Key> keys = new ArrayList<Key>();
		for(String keyStr : keyStrs){
			keys.add(Key.fromString(keyStr));
		}
		return keys;
	}
	
	protected abstract List<String> listKeys();

}