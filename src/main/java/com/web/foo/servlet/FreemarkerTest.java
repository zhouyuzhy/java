package com.web.foo.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerTest extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7551771537268986473L;
	protected final Logger LOG = Logger.getLogger(this.getClass().getName());
	
	@Override
	@SuppressWarnings(value={"unchecked","rawtypes"})
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Configuration c = new Configuration();
		c.setServletContextForTemplateLoading(getServletContext(), "/WEB-INF/template");
		Template t = c.getTemplate("index.ftl");
		Map map = new HashMap();
		map.put("hello", "hello world");
		map.put("name", "ZSY");
		map.put("marker", "!");
		try {
			t.process(map, resp.getWriter());
		} catch (TemplateException e) {
			LOG.error(e.getMessage(), e);
		}
	}
}
