package com.web.foo.service;

import com.web.foo.dto.TestDto;

public class FirstDubboServiceImpl implements FirstDubboService
{

	@Override
	public void sayHello(TestDto test)
	{
		System.out.println("Hello World!");
	}

}
