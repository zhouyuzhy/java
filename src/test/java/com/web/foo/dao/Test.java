package com.web.foo.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.web.foo.service.FirstDubboService;

public class Test
{
	public static void main(String[] args) throws InterruptedException
	{
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml", "dubbo_consumer.xml");
//		FirstDubboService service = (FirstDubboService) ac.getBean("firstDubboServiceImpl");
//		service.sayHello();
//		System.out.println("加载完成");
//		Thread.sleep(1000 * 3600);
		Consumer consumer = (Consumer) ac.getBean("consumer");
		consumer.test();
	}
}
