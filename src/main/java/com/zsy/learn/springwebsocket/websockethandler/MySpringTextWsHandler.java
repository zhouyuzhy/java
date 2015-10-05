package com.zsy.learn.springwebsocket.websockethandler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.zsy.learn.springwebsocket.constant.SpringWebsocketConstant;


/**
 * @Project websocket
 * @Description: 
 * @Company youku
 * @Create 2015年10月3日下午12:09:55
 * @author zhoushaoyu
 * @version 1.0
 * Copyright (c) 2015 youku, All Rights Reserved.
 */

public class MySpringTextWsHandler extends TextWebSocketHandler
{

	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param session
	 * @param message
	 * @throws Exception   
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
	{
		if(session.isOpen())
		{
			session.sendMessage(message);
		}
	}
	
	/** 
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @param session
	 * @throws Exception   
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception
	{
		super.afterConnectionEstablished(session);
		SpringWebsocketConstant.map.put("1", session);
	}
	
}
