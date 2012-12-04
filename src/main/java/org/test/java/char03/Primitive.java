package org.test.java.char03;

public class Primitive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * 范围小的数据类型可以赋值给范围大的数据类型
		 */
		short s = 1;
		int i = s;
		
		char c = 'a';
		i = c;
		
		byte b = 0x1;
		i = b;
		
		long i1 = i;
		
		float f = 0.1f;
		double d = f;
		
		/**
		 * 运算符取运算过程中范围大的类型作为返回类型
		 */
		double a = (double) c / i;
		
		/**
		 * 模运算结果与被除数符号相同
		 */
		int rt = (-5) % (-3);
		System.out.println(rt); //-2
		
		rt = (-5) % 3;
		System.out.println(rt); //-2
		
		rt = 5 % (-3);
		System.out.println(rt); //2
		
		/**
		 * 自增运算过程
		 * temp = i;
		 * i = temp + 1;
		 * e = temp;
		 */
		int e = i++;
		
		/**
		 * 自增运算过程
		 * i = i + 1;
		 * temp = i;
		 * e = temp;
		 */
		e = ++i;
	}

}
