package com.web.foo.chartest;

import java.io.UnsupportedEncodingException;

public class GBK
{
	public static void main(String[] args) throws UnsupportedEncodingException
	{
		String a = UTF8.get();
		for(byte b : a.getBytes())
		{
			System.out.print(b + " ");
		}
	}
}
