package org.test.httprequest.service;

import org.test.httprequest.dto.HttpRequestContext;
import org.test.httprequest.dto.HttpResponseContext;

import java.io.IOException;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public interface HttpRequest
{
	public HttpResponseContext request(HttpRequestContext context) throws IOException;
}
