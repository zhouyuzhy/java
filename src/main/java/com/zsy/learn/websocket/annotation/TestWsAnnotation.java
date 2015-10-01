package com.zsy.learn.websocket.annotation;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.zsy.learn.websocket.constant.WebsocketConstant;

/**
 * @Project websocket
 * @Description:
 * @Company youku
 * @Create 2015年10月1日上午11:55:11
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2015 youku, All Rights Reserved.
 */
@ServerEndpoint("/websocket/testWsAnnotation")
public class TestWsAnnotation
{
	@OnOpen
	public void onConnect(Session session)
	{
		WebsocketConstant.map.put("1", session);
	}
	

	@OnMessage
	public void echoTextMessage(Session session, String msg, boolean last)
	{
		try
		{
			WebsocketConstant.map.put("1", session);
			if (session.isOpen())
			{
				session.getBasicRemote().sendText(msg, last);
			}
		} catch (IOException e)
		{
			try
			{
				session.close();
			} catch (IOException e1)
			{
				// Ignore
			}
		}
	}

	@OnMessage
	public void echoBinaryMessage(Session session, ByteBuffer bb, boolean last)
	{
		try
		{
			if (session.isOpen())
			{
				session.getBasicRemote().sendBinary(bb, last);
			}
		} catch (IOException e)
		{
			try
			{
				session.close();
			} catch (IOException e1)
			{
				// Ignore
			}
		}
	}

	/**
	 * Process a received pong. This is a NO-OP.
	 *
	 * @param pm
	 *            Ignored.
	 */
	@OnMessage
	public void echoPongMessage(PongMessage pm)
	{
		// NO-OP
	}
}
