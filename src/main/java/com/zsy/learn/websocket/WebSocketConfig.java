package com.zsy.learn.websocket;

import java.util.HashSet;
import java.util.Set;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;

import org.apache.commons.lang3.text.WordUtils;

/**
 * 仅支持TOMCAT 8
 * @Project websocket
 * @Description: 
 * @Company youku
 * @Create 2015年10月2日下午7:31:07
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2015 youku, All Rights Reserved.
 */
public class WebSocketConfig implements ServerApplicationConfig
{

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> scanned)
	{
		Set<ServerEndpointConfig> result = new HashSet<ServerEndpointConfig>();
		for (Class<? extends Endpoint> ep : scanned)
		{
			result.add(ServerEndpointConfig.Builder.create(ep, "/websocket/" + WordUtils.uncapitalize(ep.getSimpleName())).build());
		}
		return result;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned)
	{
		return scanned;
	}

}
