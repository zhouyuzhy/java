package com.web.foo.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

@Component
public class Consumer
{
	@Reference(version="1.0")
	private FirstDubboService service;
	
	public void test()
	{
		service.sayHello();
	}
}
