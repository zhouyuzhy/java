package org.csp.store.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-ËÄÔÂ-2013 21:42:37
 */
public class Key {

	protected static String keySplit = ",";
	
	private List<String> keys;

	public Key(){
	}
	
	public Key(String... keys){
		this.keys = Arrays.asList(keys);
	}

	/**
	 * 
	 * @param keys
	 */
	public Key(List<String> keys){
		this.keys = keys;
	}

	public List<String> getkeys(){
		return keys;
	}

	/**
	 * 
	 * @param newVal
	 */
	public void setkeys(List<String> newVal){
		keys = newVal;
	}

	public static Key fromString(String key){
		String[] keyStrs = key.split(keySplit);
		return new Key(keyStrs);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String k : getkeys()){
			sb.append(k).append(keySplit);
		}
		sb.deleteCharAt(sb.lastIndexOf(keySplit));
		return sb.toString();
	}
}