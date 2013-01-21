package org.test.java.adapter.clazz;

public class Adapter extends Adaptee implements Api {

	public void say(String str) {
		super.say("Adapter", str);
	}

}
