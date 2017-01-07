package org.test.httprequest.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContexts;
import org.test.httprequest.dto.HttpRequestContext;
import org.test.httprequest.dto.HttpResponseContext;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class HttpRequestImpl implements HttpRequest
{

	@Override
	public HttpResponseContext request(final HttpRequestContext context) throws IOException
	{

		Request request;

		if (context.getParams().size() > 0)
		{
			request = httppost(context.getUrl(), context.getParams());
		} else
		{
			request = httpget(context.getUrl());
		}

		for (Entry<String, String> entry : context.getHeaders().entrySet())
		{
			request.setHeader(entry.getKey(), entry.getValue());
		}

		request.connectTimeout(context.getConnectionTimeout());
		request.userAgent(
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
		request.socketTimeout(context.getSocketTimeout());
		if (StringUtils.isNotBlank(context.getProxyHost()))
		{
			HttpHost proxy = new HttpHost(context.getProxyHost(), context.getProxyPort(), context.getProxyProtocol());
			request.viaProxy(proxy);
		}
		return request.execute().handleResponse(new ResponseHandler<HttpResponseContext>()
		{

			@Override
			public HttpResponseContext handleResponse(HttpResponse response) throws ClientProtocolException, IOException
			{
				if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
				{
					throw new IOException("响应结果非成功，status=" + response.getStatusLine().getStatusCode() + ", reason=" + response
							.getStatusLine().getReasonPhrase());
				}
				HttpResponseContext responseContext = new HttpResponseContext();
				if (response.getFirstHeader("Content-Disposition") != null && response.getFirstHeader("Content-Disposition")
						.getValue().contains("attachment") && StringUtils.isNotBlank(context.getFileName()))
				{
					FileOutputStream fos = new FileOutputStream(context.getFileName());
					IOUtils.copy(response.getEntity().getContent(), fos);
					fos.close();

				} else
				{
					responseContext.setContent(IOUtils.toString(response.getEntity().getContent()));
				}
				Header[] cookies = response.getHeaders("set-cookie");
				for (Header cookie : cookies)
				{
					responseContext.getCookies().put(cookie.getName(), cookie.getValue());
				}
				return responseContext;
			}
		});
	}

	private Request httpget(String url)
	{
		return Request.Get(url);
	}

	private Request httppost(String url, Map<String, String> params)
	{
		Form form = Form.form();
		for (String key : params.keySet())
		{
			form.add(key, params.get(key));
		}
		return Request.Post(url).bodyForm(form.build());
	}
}
