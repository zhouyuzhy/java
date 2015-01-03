package com.web.foo.dto;

import java.io.Serializable;
import java.util.List;

public class TestDto implements Serializable
{
	private static final long serialVersionUID = -828175054553575934L;
	private String test;
	private List<String> list;
	public String getTest()
	{
		return test;
	}
	public void setTest(String test)
	{
		this.test = test;
	}
	public List<String> getList()
	{
		return list;
	}
	public void setList(List<String> list)
	{
		this.list = list;
	}
	
}
