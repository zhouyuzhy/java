package org.test.java.char10;

public class Polymorphic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * ��������ָ���������
		 */
		Flower f = new Rose();
		f.sing();
	}

}


class Flower{
	
	public void sing(){
		System.out.println("singing.");
	}
}

class Rose extends Flower{
	
}