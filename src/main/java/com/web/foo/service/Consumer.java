package com.web.foo.service;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.web.foo.dto.TestDto;

@Component
public class Consumer
{
	@Reference(version="1.0", lazy=true)
	private FirstDubboService service;
	
	public void test()
	{
		TestDto test = new TestDto();
		test.setList(Arrays.asList(new String[]{"a", "b"}));
		test.setTest("t");
		service.sayHello(test);
	}
}
