package org.test.httprequest.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class HttpRequestContext
{

	private String url;

	private Map<String, String> params = new HashMap<>();

	private Map<String, String> cookies = new HashMap<>();

	private Map<String, String> headers = new HashMap<>();

	private String proxyHost;

	private int proxyPort;

	private String proxyProtocol;

	private int connectionTimeout = 5000;

	private int socketTimeout = 30000;

	private String fileName;

	public HttpRequestContext(String url)
	{
		this.url = url;
	}

	public HttpRequestContext(String url, Map<String, String> params)
	{
		this.url = url;
		this.params = params;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public Map<String, String> getParams()
	{
		return params;
	}

	public Map<String, String> getCookies()
	{
		return cookies;
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public String getProxyHost()
	{
		return proxyHost;
	}

	public void setProxyHost(String proxyHost)
	{
		this.proxyHost = proxyHost;
	}

	public int getProxyPort()
	{
		return proxyPort;
	}

	public void setProxyPort(int proxyPort)
	{
		this.proxyPort = proxyPort;
	}

	public String getProxyProtocol()
	{
		return proxyProtocol;
	}

	public void setProxyProtocol(String proxyProtocol)
	{
		this.proxyProtocol = proxyProtocol;
	}

	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout()
	{
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout)
	{
		this.socketTimeout = socketTimeout;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

}
