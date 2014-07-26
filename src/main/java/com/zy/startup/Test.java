package com.zy.startup;

import com.zy.util.HttpsConnectionUtil;

public class Test
{
	public static void main(String[] args)
	{
		System.out.println(HttpsConnectionUtil.httpsUrlRequestGet("https://173.194.128.168/", null, 3000));
	}
}
