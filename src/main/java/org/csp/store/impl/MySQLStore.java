package org.csp.store.impl;
import java.io.InputStream;
import java.util.List;

import org.csp.store.AbstractStore;
import org.csp.store.exception.UnimplementException;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:56
 */
public class MySQLStore extends AbstractStore {

	
	public MySQLStore(){
	}
	

	@Override
	protected boolean save(String key, InputStream value) {
		throw new UnimplementException("Mysql Store does not implement.");
	}

	@Override
	protected boolean exist(String key) {
		throw new UnimplementException("Mysql Store does not implement.");
	}

	@Override
	protected InputStream get(String key) {
		throw new UnimplementException("Mysql Store does not implement.");
	}

	@Override
	protected List<String> listKeys() {
		throw new UnimplementException("Mysql Store does not implement.");
	}

}