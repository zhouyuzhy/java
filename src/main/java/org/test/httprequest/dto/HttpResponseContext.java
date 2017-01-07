package org.test.httprequest.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class HttpResponseContext
{

	private String content;

	private Map<String, String> cookies = new HashMap<>();

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public Map<String, String> getCookies()
	{
		return cookies;
	}

	public void setCookies(Map<String, String> cookies)
	{
		this.cookies = cookies;
	}
}
