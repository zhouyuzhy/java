package org.csp.store;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.csp.store.model.Key;
import org.csp.store.model.Value;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-四月-2013 21:42:46
 */
public abstract class AbstractStore implements IStore {

	
	/**
	 * 保存值，存在即覆盖
	 * 
	 * @param key
	 * @param value
	 */
	public boolean save(Key key, Value value){
		return save(key.toString(), value.getvalue());
	}
	
	protected abstract boolean save(String key, InputStream value);

	/**
	 * 是否存在指定key的对象
	 * 
	 * @param key
	 */
	public boolean exist(Key key){
		return exist(key.toString());
	}
	
	protected abstract boolean exist(String key);

	/**
	 * 获取指定对象
	 * @param key 获取对象的Key
	 */
	public Value get(Key key){
		return new Value(get(key.toString()));
	}

	protected abstract InputStream get(String key);
	/**
	 * 列举目前存储的所有key
	 * @return 所有Key
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