package com.web.foo.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import junit.framework.TestCase;

public class TestTest extends TestCase {

	public void testTrue(){
		this.assertTrue(true);
		try {
			jdbc();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void jdbc() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "pwroot";
		Connection conn = DriverManager.getConnection(url, user, password);
	}
	
}
