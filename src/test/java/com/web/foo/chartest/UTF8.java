package com.web.foo.chartest;

public class UTF8
{
	public static String get()
	{
		return "这个是一个商品信息";
	}
	
	public static void main(String[] args)
	{
		String a = get();
		System.out.println(a.getBytes().length);
		for(byte b : a.getBytes())
		{
			System.out.print(b + " ");
		}
	}
}
