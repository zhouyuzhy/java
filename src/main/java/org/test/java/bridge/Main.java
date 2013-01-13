package org.test.java.bridge;

public class Main {

	public static void main(String[] args) {
		/**
		 * 
		 *   AbstractEngine <------------- AbstractCar
		 *        ��                            ��
		 *        |                            |
		 * 		  |                            |
		 *       / \ 						  / \
		 *      /   \						 /   \
		 *    C500  C2000				   Bus   Truck
		 *    
		 *    ����ά�ȵ������뿪����
		 */
		AbstractEngine engine = new C500Engine();
		AbstractCar car = new Bus(engine);
		System.out.println(car);
	}
}
