package com.zy.findip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.zy.util.HttpsConnectionUtil;

public class FindIpService
{
	protected Logger log = Logger.getLogger(getClass());
	ExecutorService executor = Executors.newFixedThreadPool(300);
	ExecutorService writeResultExecutor = Executors.newFixedThreadPool(1);

	public void findProcess(String poolDir, final String outputFilePath) throws IOException
	{
		File dir = null;
		if (StringUtils.startsWithIgnoreCase(poolDir, "classpath://"))
		{
			dir = new File(getClass().getClassLoader().getResource(poolDir.replace("classpath://", "")).getFile());
		}
		else
		{
			dir = new File(poolDir);
		}
		File[] fileList = dir.exists() ? dir.listFiles() : new File[0];
		for (File file : fileList)
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			try
			{
				final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
				File outputFile = new File(outputFilePath);
				if (outputFile.exists())
				{
					outputFile.delete();
				}
				asynchronousWriteResult(outputFilePath, queue);
				String line = "";
				while ((line = br.readLine()) != null)
				{
					final String tempLine = line;
					executor.execute(new Runnable() {

						@Override
						public void run()
						{
							findHostPort(queue, tempLine);
						}

					});
				}
			}
			finally
			{
				br.close();
			}
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
					log.info(queue.size());
					BufferedWriter bw = null;
					try
					{
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath, true)));
						String ip = queue.take();
						bw.append(ip + System.getProperty("line.separator"));
					}
					catch (InterruptedException e)
					{
						log.fatal(e.getMessage(), e);
					}
					catch (IOException e)
					{
						log.fatal(e.getMessage(), e);
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
							log.fatal(e.getMessage(), e);
						}
					}
				}

			}
		});
	}

	private void findHostPort(ArrayBlockingQueue<String> queue, String tempLine)
	{
		try
		{
			log.info(tempLine);
			String r = (HttpsConnectionUtil.httpsUrlRequestGet("https://"+tempLine, null, 3000));
			if(r!=null)
				queue.put(tempLine);
		}
		catch (IllegalStateException e)
		{
			log.debug("ip:" + tempLine + ";" + e.getMessage());
		}
		catch (InterruptedException e)
		{
			log.fatal(e.getMessage(), e);
		}
	}
}
