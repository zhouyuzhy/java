package com.web.foo.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.web.foo.dao.ITestDAO;

public class TestAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -667365494746867929L;

	private ITestDAO testDAO;
	
	public void setTestDAO(ITestDAO testDAO) {
		this.testDAO = testDAO;
	}
	
	public ITestDAO getTestDAO(){
		return this.testDAO;
	}
	
	@Override
	public String execute() throws Exception {
		testDAO.select(1, "abc");
		return super.execute();
	}
	
}
