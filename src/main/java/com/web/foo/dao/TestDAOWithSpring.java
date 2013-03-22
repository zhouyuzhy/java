package com.web.foo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.web.foo.model.TestDO;

public class TestDAOWithSpring implements ITestDAO {

	private SqlSession sqlSession;

	public TestDAOWithSpring() {
	}
	
	public TestDAOWithSpring(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public TestDAOWithSpring(String configPath) {
		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(this
				.getClass().getResourceAsStream(configPath));
		this.sqlSession = factory.openSession();
	}
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}
	
	public int insert(String username){
		TestDO user = new TestDO(username);
		int result = sqlSession.insert("TestDO.insert", user);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<TestDO> select(Integer id, String username){
		return sqlSession.selectList("TestDO.select", new TestDO(id, username));
	}
	
	public int clear(){
		int result = sqlSession.delete("TestDO.clear");
		return result;
	}

	public void commit(){
		sqlSession.commit();
	}
	
	public void close(){
		sqlSession.close();
	}
}
