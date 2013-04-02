package org.csp.store.model;

import java.io.InputStream;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:39
 */
public class Value {

	private InputStream value;

	public Value(){

	}

	/**
	 * 
	 * @param value
	 */
	public Value(InputStream value){

	}

	public InputStream getvalue(){
		return value;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setvalue(InputStream newVal){
		value = newVal;
	}

}