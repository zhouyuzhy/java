package com.zsy.learn.websocket.endpoint;

import java.io.IOException;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 * @Project websocket
 * @Description:
 * @Company youku
 * @Create 2015年10月1日下午8:48:19
 * @author zhoushaoyu
 * @version 1.0 Copyright (c) 2015 youku, All Rights Reserved.
 */

public class TestWsEndpoint extends Endpoint
{

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * 
	 * @param session
	 * @param config
	 */
	@Override
	public void onOpen(Session session, EndpointConfig config)
	{
		session.addMessageHandler(new EchoMessageHandler(session.getBasicRemote()));
	}

	private static class EchoMessageHandler implements MessageHandler.Whole<String>
	{

		private final RemoteEndpoint.Basic remoteEndpointBasic;

		private EchoMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic)
		{
			this.remoteEndpointBasic = remoteEndpointBasic;
		}

		@Override
		public void onMessage(String message)
		{
			try
			{
				if (remoteEndpointBasic != null)
				{
					remoteEndpointBasic.sendText(message);
				}
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
