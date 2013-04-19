package org.csp.store.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.csp.store.AbstractStore;
import org.csp.store.exception.StoreException;
import org.csp.store.exception.StoreKeyNotFoundException;
import org.csp.store.model.Value;
import org.csp.store.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:56
 */
public class FileStore extends AbstractStore {

	private String dir;

	public FileStore() {
	}

	public FileStore(String dir) {
		setDir(dir);
	}

	@Override
	protected boolean save(String key, InputStream value) throws StoreException {
		if (value == null)
			throw new IllegalArgumentException("Save value should not be null.");
		String fileName = key;
		OutputStream os = null;
		File file = new File(this.dir + fileName);
		try {
			os = new FileOutputStream(file);
			Utils.copyStream(value, os);
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			StoreException exception = null;
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					exception = new StoreException(e);
				}
			try {
				value.close();
			} catch (IOException e) {
				exception = new StoreException(e);
			}
			if (exception != null)
				throw exception;
		}
		return true;
	}

	@Override
	protected boolean exist(String key) throws StoreException {
		String fileName = key;
		File file = new File(this.dir + fileName);
		return file.exists();
	}

	@Override
	protected Value get(String key) throws StoreException {
		if (!exist(key)) {
			throw new StoreKeyNotFoundException("Not found for key " + key);
		}
		String fileName = key;
		InputStream is = null;
		try {
			is = new FileInputStream(this.dir + fileName);
		} catch (FileNotFoundException e) {
			throw new StoreException(e);
		}
		return new Value(is);
	}

	@Override
	protected List<String> listKeys() {
		File directory = new File(this.dir);
		return Arrays.asList(directory.list());
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		if (!dir.endsWith(File.separator))
			dir += File.separator;
		this.dir = dir;
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}else if(!directory.isDirectory()){
			throw new IllegalArgumentException(dir + " already exists and is not directory.");
		}
	}

}