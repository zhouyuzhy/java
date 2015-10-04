package com.zsy.learn.springwebsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.zsy.learn.springwebsocket.websockethandler.MySpringTextWsHandler;


/**
 * @Project websocket
 * @Description: 
 * @Company youku
 * @Create 2015年10月3日下午12:15:14
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2015 youku, All Rights Reserved.
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer
{

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param registry   
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
	{
		registry.addHandler(myWsHandler(), "/springwsbean/myhandler.htm");
		registry.addHandler(myWsHandler(), "/bean_sockjs").withSockJS();
	}

	@Bean
	public WebSocketHandler myWsHandler()
	{
		return new MySpringTextWsHandler();
	}

}
