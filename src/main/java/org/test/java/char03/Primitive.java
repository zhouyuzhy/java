package org.test.java.char03;

public class Primitive {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * ��ΧС���������Ϳ��Ը�ֵ����Χ�����������
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
		 * �����ȡ��������з�Χ���������Ϊ��������
		 */
		double a = (double) c / i;
		
		/**
		 * ģ�������뱻����������ͬ
		 */
		int rt = (-5) % (-3);
		System.out.println(rt); //-2
		
		rt = (-5) % 3;
		System.out.println(rt); //-2
		
		rt = 5 % (-3);
		System.out.println(rt); //2
		
		/**
		 * �����������
		 * temp = i;
		 * i = temp + 1;
		 * e = temp;
		 */
		int e = i++;
		
		/**
		 * �����������
		 * i = i + 1;
		 * temp = i;
		 * e = temp;
		 */
		e = ++i;
	}

}
