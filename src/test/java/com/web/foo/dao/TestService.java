package com.web.foo.dao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class TestService
{
	public static void main(String[] args) throws InterruptedException
	{
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml", "dubbo_provider.xml");
		ac.getBean("firstDubboServiceImpl");
		System.out.println("加载完成");
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.execute(new Runnable() {
			
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				System.out.println();
			}
		});
	}
}
