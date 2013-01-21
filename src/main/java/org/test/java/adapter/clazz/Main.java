package org.test.java.adapter.clazz;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Api api = new Adapter();
		api.say("this is from client!");
	}

}
