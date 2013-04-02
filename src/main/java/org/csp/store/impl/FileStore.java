package org.csp.store.impl;
import java.io.InputStream;
import java.util.List;

import org.csp.store.AbstractStore;
import org.csp.store.model.Value;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:56
 */
public class FileStore extends AbstractStore {

	public FileStore(){

	}

	@Override
	protected boolean save(String key, InputStream value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean exist(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected InputStream get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> listKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}