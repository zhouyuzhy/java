package com.web.foo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

@Activate(group=Constants.PROVIDER, order=-5000)
public class TestFilter implements Filter
{

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException
	{
		System.out.println("test before invoke，"+RpcContext.getContext().getRemoteHost());
		return invoker.invoke(invocation);
	}

}
