package com.zy.startup;

import java.io.IOException;

import com.zy.findip.FindIpService;

public class Find
{
	public static void main(String[] args) throws IOException
	{
		FindIpService service = new FindIpService();
		service.findProcess("classpath://google_ip_pool/", "find_ip.out");
	}
}
