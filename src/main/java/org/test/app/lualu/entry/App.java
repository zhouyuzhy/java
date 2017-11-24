package org.test.app.lualu.entry;

import org.apache.commons.io.IOUtils;
import org.test.app.lualu.service.LualuService;
import org.test.thread.ThreadPoolUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class App
{

	public static void main(String[] args) throws IOException
	{
		StringBuilder html = new StringBuilder();
		html.append("<html><head></head><body class=\"\">");
		int month = 11;
		int date = 24;
		int range = 7;
		List<String > index = new ArrayList<>();
		for (int i = 0; i < range; i++)
			index.addAll(new LualuService().queryIndex(month, date - i));
		for (String url : index)
		{
			html.append(new LualuService().queryEachContent(url));
			html.append("<br>\n");
			html.append("<br>\n");
			html.append("<br>\n");
		}
		html.append("</body></html>");
		IOUtils.write(html.toString(),
				new FileOutputStream("E:\\TDdownload\\bak\\thasdzxd5er6gh6767dg\\85182755\\" + month + "_" + date + ".html"));
		System.out.println("E:\\TDdownload\\bak\\thasdzxd5er6gh6767dg\\85182755\\" + month + "_" + date + ".html");
		ThreadPoolUtil.shutdown();
	}
}

