package org.test.java.char10;

public class DefaultValue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test1 t = new Test1();
		System.out.println(t.a);// null
		System.out.println(t.i); //0
		System.out.println(t.f); //0.0
		System.out.println(t.c); // (�ո�)
		System.out.println(t.b); //false
	}

}


class Test1{
	public String a;
	public int i; //int byte short long����0
	public float f; // float double ����0.0
	public char c; // '\u0000'
	public boolean b; // false
}