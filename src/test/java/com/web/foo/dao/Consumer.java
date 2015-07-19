package com.web.foo.dao;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.web.foo.dto.TestDto;
import com.web.foo.service.FirstDubboService;

@Component
public class Consumer
{
	@Autowired
	@Qualifier("firstDubboService")
	private FirstDubboService service;
	
	public void test()
	{
		TestDto test = new TestDto();
		test.setList(Arrays.asList(new String[]{"a", "b"}));
		test.setTest("shortCall");
		service.sayHello(test);
	}
}
