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
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.zy.util.JsonUtil;

public class SortIpService
{
	protected Logger log = Logger.getLogger(getClass());

	@SuppressWarnings("unchecked")
	public void sort(String sourceFilePath, String outputFilePath) throws IOException
	{
		File source = new File(sourceFilePath);
		if (!source.exists())
		{
			throw new IllegalStateException(sourceFilePath + "源文件不存在");
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
		String line = "";
		try
		{
			Map<Long, String> receivedAllIps = new TreeMap<Long, String>();
			while ((line = br.readLine()) != null)
			{
				try
				{
					Map<String, String> map = JsonUtil.deserialize(line, Map.class);
					String receive = map.get("receive");
					String time = map.get("time");
					String ip = map.get("ip");
					if (StringUtils.isBlank(receive) || StringUtils.isBlank(time))
					{
						continue;
					}
					if (Integer.parseInt(receive) < 5)
					{
						log.debug(ip + " received < 5, continue");
						continue;
					}
					receivedAllIps.put(Long.parseLong(time), ip);
				}
				catch (Exception e)
				{
					log.fatal(e.getMessage(), e);
					continue;
				}
			}
			write(receivedAllIps, outputFilePath);
		}
		finally
		{
			br.close();
		}
	}

	private void write(Map<Long, String> receivedAllIps2, String outputFilePath)
	{
		File output = new File(outputFilePath);
		if (output.exists())
		{
			output.delete();
		}
		BufferedWriter bw = null;
		try
		{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath, true)));
			bw.write(receivedAllIps2.values().toString().replace(", ", "|").replaceAll("[\\[\\]]", ""));
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
