package org.test.java.char10;

public class Polymorphic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 父类引用指向子类对象
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