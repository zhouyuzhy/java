package com.web.foo.beans;

import org.apache.log4j.Logger;


public class Test {
	protected final static Logger LOG = Logger.getLogger(Test.class);

	static{
		LOG.info("Test has been inited.");
	}
}
