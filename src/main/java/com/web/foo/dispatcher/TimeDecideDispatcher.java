package com.web.foo.dispatcher;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Dispather;
import com.web.foo.handler.TimeDecideChannelHandler;


public class TimeDecideDispatcher implements Dispather
{

	 public static final String NAME = "timeDecide";

	@Override
	public ChannelHandler dispath(ChannelHandler handler, URL url)
	{
		return new TimeDecideChannelHandler(handler, url);
	}

}
