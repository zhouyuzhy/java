package com.zy.findip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zy.util.JsonUtil;

public abstract class PingIpService
{
	ExecutorService executor = Executors.newFixedThreadPool(100);
	ExecutorService writeResultExecutor = Executors.newFixedThreadPool(1);

	public void pingProcess(String sourceFile, String outputFilePath) throws IOException
	{
		File outputFile = new File(outputFilePath);
		if (outputFile.exists())
		{
			outputFile.delete();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
		try
		{
			final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
			String ip = "";
			asynchronousWriteResult(outputFilePath, queue);
			while ((ip = br.readLine()) != null)
			{
				final String temp = ip;
				executor.execute(new Runnable() {

					@Override
					public void run()
					{
						try
						{
							ping(queue, temp);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				});

			}
		}
		finally
		{
			br.close();
		}
	}

	/**
	 * @param outputFilePath
	 * @param queue
	 */
	private void asynchronousWriteResult(final String outputFilePath, final ArrayBlockingQueue<String> queue)
	{
		writeResultExecutor.execute(new Runnable() {

			@Override
			public void run()
			{
				while (true)
				{
					System.out.println(queue.size());
					BufferedWriter bw = null;
					try
					{
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath, true)));
						String ip = queue.take();
						bw.append(ip + System.getProperty("line.separator"));
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					finally
					{
						try
						{
							if (bw != null)
								bw.close();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}

			}
		});
	}

	private void ping(ArrayBlockingQueue<String> queue, String ip) throws IOException, InterruptedException
	{
		String code = getCode(ip);
		System.out.println(code);
		Process process = Runtime.getRuntime().exec(code);
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append(System.getProperty("line.separator"));
			}
			Map<String, String> result = handle(sb.toString());
			if (result == null)
			{
				return;
			}
			result.put("ip", ip);
			System.out.println(result);
			queue.put(JsonUtil.serialize(result));
		}
		finally
		{
			process.destroy();
		}
	}

	protected abstract Map<String, String> handle(String pingOutput);

	protected abstract String getCode(String ip);
}
