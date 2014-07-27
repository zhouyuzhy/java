package com.zy.startup;

import java.io.IOException;

import com.zy.findip.SortIpService;

public class Sort
{
	public static void main(String[] args) throws IOException
	{
		SortIpService service = new SortIpService();
		service.sort("ping.out", "final.txt");
	}
}
