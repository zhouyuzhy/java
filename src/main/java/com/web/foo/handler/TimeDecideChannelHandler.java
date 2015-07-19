package com.web.foo.handler;

import java.util.concurrent.ExecutorService;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.threadpool.support.fixed.FixedThreadPool;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.ExecutionException;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.Request;
import com.alibaba.dubbo.remoting.transport.dispather.ChannelEventRunnable;
import com.alibaba.dubbo.remoting.transport.dispather.ChannelEventRunnable.ChannelState;
import com.alibaba.dubbo.remoting.transport.dispather.WrappedChannelHandler;
import com.alibaba.dubbo.rpc.Invocation;
import com.web.foo.dto.TestDto;

/**
 * @Project ewallet-web
 * @Description:
 * @Create 2015年7月19日下午6:25:17
 * @author zhoushaoyu
 * 
 */

public class TimeDecideChannelHandler extends WrappedChannelHandler
{
	FixedThreadPool tp = new FixedThreadPool();
	ExecutorService exe;
	/**
	 * @param handler
	 * @param url
	 */
	public TimeDecideChannelHandler(ChannelHandler handler, URL url)
	{
		super(handler, url);
		url = url.addParameter(Constants.THREAD_NAME_KEY, "my-fixed-thread-pool");
		exe = (ExecutorService) tp.getExecutor(url);
	}

	public void connected(Channel channel) throws RemotingException
	{
		ExecutorService cexecutor = getExecutorService();
		try
		{
			cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CONNECTED));
		} catch (Throwable t)
		{
			throw new ExecutionException("connect event", channel, getClass() + " error when process connected event .", t);
		}
	}

	public void disconnected(Channel channel) throws RemotingException
	{
		ExecutorService cexecutor = getExecutorService();
		try
		{
			cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.DISCONNECTED));
		} catch (Throwable t)
		{
			throw new ExecutionException("disconnect event", channel, getClass() + " error when process disconnected event .",
					t);
		}
	}

	public void received(Channel channel, Object message) throws RemotingException
	{
		ExecutorService cexecutor = getExecutorService(message);
		try
		{
			cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.RECEIVED, message));
		} catch (Throwable t)
		{
			throw new ExecutionException(message, channel, getClass() + " error when process received event .", t);
		}
	}

	public void caught(Channel channel, Throwable exception) throws RemotingException
	{
		ExecutorService cexecutor = getExecutorService();
		try
		{
			cexecutor.execute(new ChannelEventRunnable(channel, handler, ChannelState.CAUGHT, exception));
		} catch (Throwable t)
		{
			throw new ExecutionException("caught event", channel, getClass() + " error when process caught event .", t);
		}
	}

	private ExecutorService getExecutorService(Object message)
	{
		Object inv = message;
		if (message instanceof Request)
		{
			inv = ((Request) message).getData();
		}
		if (!(inv instanceof Invocation))
		{
			return getExecutorService();
		}
		for (Object p : ((Invocation) inv).getArguments())
		{
			if (p instanceof String && "longCall".equalsIgnoreCase((String) p))
			{
				
				return (ExecutorService) exe;
			}
			if (p instanceof TestDto && "longCall".equalsIgnoreCase(((TestDto) p).getTest()))
			{
				return (ExecutorService) exe;
			}
		}
		return getExecutorService();
	}

	private ExecutorService getExecutorService()
	{
		ExecutorService cexecutor = executor;
		if (cexecutor == null || cexecutor.isShutdown())
		{
			cexecutor = SHARED_EXECUTOR;
		}
		return cexecutor;
	}
}
