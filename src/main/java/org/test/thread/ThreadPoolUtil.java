package org.test.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class ThreadPoolUtil
{

	private static ExecutorService executors = Executors.newFixedThreadPool(100);

	public static void execute(Runnable runnable)
	{
		executors.execute(runnable);
	}

	public static void shutdown()
	{
		executors.shutdown();
	}
}
