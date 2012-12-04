package org.test.java.char10;

public class ReferenceObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t  = new Test(0, "a");
		test(t);
		System.out.println(t.getA());
		System.out.println(t.getB());
		
		Test t1 = new Test(0, "a");
		test1(t1);
		System.out.println(t1.getA());
		System.out.println(t1.getB());
	}

	public static void test(Test t){
		// modify the same object attribute.
		t.setA(1);
		t.setB("b");
	}
	
	public static void test1(Test t){
		t = new Test(1, "b"); // modify reference to new object for parameter t.
	}
}

class Test {
	private int a;
	private String b;

	public Test(int a, String b) {
		super();
		this.a = a;
		this.b = b;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

}
