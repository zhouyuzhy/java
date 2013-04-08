package org.csp.store.model;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:39
 */
public class Value {

	private InputStream value;

	public Value() {

	}

	/**
	 * 
	 * @param value
	 */
	public Value(InputStream value) {
		this.value = value;
	}

	public InputStream getvalue() {
		return value;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setvalue(InputStream newVal) {
		value = newVal;
	}

	public byte[] read() throws IOException {
		try {
			byte[] result = new byte[this.value.available()];
			this.value.read(result);
			return result;
		} finally {
			this.value.close();
		}
	}

}