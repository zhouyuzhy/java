package com.web.foo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TestListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("test");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
