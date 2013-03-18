package com.web.foo.dao;

import junit.framework.TestCase;

public class TestTestDAOWithOutSpring extends TestCase{

	private TestDAOWithOutSpring t;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		t = new TestDAOWithOutSpring("/mybatis-Configuration.xml");
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		t.clear();
		t.close();
	}
	
	public void testInsert(){
		this.assertEquals(1, t.insert("test1"));
	}
	
	public void testSelect(){
		this.assertEquals(1, t.insert("test1"));
		this.assertEquals(1, t.select(null, "test1").size());
	}
	
}
