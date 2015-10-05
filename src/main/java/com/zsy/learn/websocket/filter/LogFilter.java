package com.zsy.learn.websocket.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;



/**
 * @Project websocket
 * @Description: 
 * @Company youku
 * @Create 2015年10月1日下午4:27:50
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2015 youku, All Rights Reserved.
 */

public class LogFilter implements Filter
{

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param filterConfig
	 * @throws ServletException   
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		// TODO Auto-generated method stub
		
	}

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException   
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		System.out.println("log filter:"+((HttpServletRequest)request).getRequestURI());
		chain.doFilter(request, response);
	}

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>   
	 */
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

}
