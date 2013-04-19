package org.csp.store.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.csp.store.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-四月-2013 21:42:39
 */
public class Value {

	private InputStream value;
	private int length;

	public Value() {

	}
	
	/**
	 * 
	 * @param value
	 */
	public Value(InputStream value) {
		this.value = value;
	}

	/**
	 * 
	 * @param value
	 */
	public Value(InputStream value, int length) {
		this.value = value;
		this.length = length;
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

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte[] read() throws IOException {
		if (value == null)
			throw new IllegalArgumentException("还没有设置输入流。");
		byte[] result;
		if (length != 0) {
			result = new byte[length];
			try {
				this.value.read(result);
				return result;
			} finally {
				this.value.close();
			}
		} else {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			Utils.copyStream(value, os);
			return os.toByteArray();
		}
	}

}