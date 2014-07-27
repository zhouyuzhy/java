package com.zy.findip;

import java.util.HashMap;
import java.util.Map;

public class WindownsPingIpService extends PingIpService
{

	@Override
	protected Map<String, String> handle(String pingOutput)
	{
		try
		{
			Map<String, String> map = new HashMap<String, String>();
			String[] list = pingOutput.split(System.getProperty("line.separator"));
			String line3 = list[list.length - 3];
			String line1 = list[list.length - 1];
			String send = line3.split("已发送 = ")[1].split("，")[0];
			String receive = line3.split("已接收 = ")[1].split("，")[0];
			String abandon = line3.split("丢失 = ")[1].split(" \\(")[0];
			String time = line1.split("平均 = ")[1].split("ms")[0];
			map.put("send", send);
			map.put("receive", receive);
			map.put("abandon", abandon);
			map.put("time", time);
			return map;
		}
		catch (Exception e)
		{
			log.fatal(e.getMessage(), e);
			return null;
		}
	}

	@Override
	protected String getCode(String ip)
	{
		return "ping -n 5 " + ip;
	}

}
