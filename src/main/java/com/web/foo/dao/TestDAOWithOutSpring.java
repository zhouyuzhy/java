package com.web.foo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.web.foo.model.TestDO;

public class TestDAOWithOutSpring {

	private SqlSession sqlSession;

	public TestDAOWithOutSpring(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public TestDAOWithOutSpring(String configPath) {
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(this
				.getClass().getResourceAsStream(configPath));
		this.sqlSession = factory.openSession();
	}
	
	public int insert(String username){
		TestDO user = new TestDO(username);
		int result = sqlSession.insert("TestDO.insert", user);
		sqlSession.commit();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<TestDO> select(Integer id, String username){
		return sqlSession.selectList("TestDO.select", new TestDO(id, username));
	}
	
	public int clear(){
		int result = sqlSession.delete("TestDO.clear");
		sqlSession.commit();
		return result;
	}
	
	public void close(){
		sqlSession.close();
	}
}
