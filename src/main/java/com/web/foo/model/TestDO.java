package com.web.foo.model;

import java.io.Serializable;

public class TestDO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1323546252816901746L;

	private Integer id;
	private String name;

	public TestDO(String name) {
		this.name = name;
	}
	
	public TestDO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	 public TestDO() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
