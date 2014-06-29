package com.web.foo.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.web.foo.dto.TestDto;

//@Service(version="1.0")
@Component
public class FirstDubboServiceImpl implements FirstDubboService
{

	@Override
	public void sayHello(TestDto test)
	{
		System.out.println("Hello World!");
	}

}
