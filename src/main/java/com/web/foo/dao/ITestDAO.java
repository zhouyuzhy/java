package com.web.foo.dao;

import java.util.List;

import com.web.foo.model.TestDO;

public interface ITestDAO {
	public int insert(String username);
	public List<TestDO> select(Integer id, String username);
	public int clear();
}
