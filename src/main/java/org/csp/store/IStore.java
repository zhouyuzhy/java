package org.csp.store;
import java.util.List;

import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-四月-2013 21:42:44
 */
public interface IStore {

	/**
	 * 是否存在指定key的对象
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
	 * 保存值，存在即覆盖
	 * 
	 * @param key
	 * @param value
	 * @throws StoreException 
	 * @throws LoginFailedException 
	 */
	public boolean save(Key key, Value value) throws StoreException, LoginFailedException;

}