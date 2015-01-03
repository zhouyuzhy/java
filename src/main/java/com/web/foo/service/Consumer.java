package com.web.foo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.web.foo.dto.TestDto;

@Component
public class Consumer
{
	@Autowired
	private FirstDubboService service;
	
	public void test()
	{
		TestDto test = new TestDto();
		test.setList(Arrays.asList(new String[]{"a", "b"}));
		test.setTest("t");
		service.sayHello(test);
	}
}
