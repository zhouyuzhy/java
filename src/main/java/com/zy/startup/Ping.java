package com.zy.startup;

import java.io.IOException;

import com.zy.findip.PingIpService;
import com.zy.findip.WindownsPingIpService;

public class Ping
{
	public static void main(String[] args) throws IOException
	{
		PingIpService service = new WindownsPingIpService();
		service.pingProcess("find_ip_1.out", "ping.out");
	}
}
