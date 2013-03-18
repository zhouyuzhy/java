package com.web.foo.dao;

import junit.framework.TestCase;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.UrlResource;

public class TestTestDAOWithSpring extends TestCase {

	private TestDAOWithSpring t;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		XmlBeanFactory factory = new XmlBeanFactory(new UrlResource(this.getClass().getResource("/bean/mybatis.xml")));
		this.t = (TestDAOWithSpring)factory.getBean("testDAO");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		t.clear();
	}
	
	public void testInsert(){
		this.assertEquals(1, t.insert("test1"));
	}
	
	public void testSelect(){
		this.assertEquals(1, t.insert("test1"));
		this.assertEquals(1, t.select(null, "test1").size());
	}
	
}
