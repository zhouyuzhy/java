package org.test.app.lualu.service;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.test.httprequest.dto.HttpRequestContext;
import org.test.httprequest.service.HttpRequestImpl;
import org.test.parsehtml.HtmlParser;
import org.test.parsehtml.visitor.TestVisitor;
import org.test.parsehtml.visitor.Visitor;
import org.test.thread.ThreadPoolUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhoushaoyu on 2017/1/7.
 */
public class LualuService
{

	public static final String DOWNLOAD_URL = "";

	public static final String DOWNLOAD_REFERER = "";

	private String indexUrl = "";

	private String hrefPrefix = "";

	private String dir = "E:/TDdownload/bak/thasdzxd5er6gh6767dg/";

	private List<String> contents = Arrays.asList();

	public String queryEachContent(String url) throws IOException
	{
		final StringBuilder content = new StringBuilder();
		content.append("<a href=\"").append(url).append("\">").append(url).append("</a>").append("<br>\n").append("<br>\n")
				.append("<br>\n");
		HtmlParser htmlParser = new HtmlParser(new HttpRequestImpl().request(new HttpRequestContext(url)).getContent());
		final StringBuilder title = new StringBuilder();
		htmlParser.parser(Arrays.asList("#main > div:nth-child(2) > table > tbody > tr > td > a:nth-child(5)"), new Visitor()
		{

			@Override
			public void visit(Element element)
			{
				title.append(element.text());
			}
		});
		if(title.toString().contains("灣搭拉咩拉"))
		{
			parseMiela(content, htmlParser);
		}
		else
		{
			parseCommon(content, htmlParser);
		}
		return content.toString();
	}

	private void parseMiela(final StringBuilder content, HtmlParser htmlParser)
	{
		htmlParser.parser(Arrays.asList("#read_tpc > a"), new Visitor()
		{

			@Override
			public void visit(Element element)
			{
				if(element.attr("href").equalsIgnoreCase(element.text()))
				{

					final String id = StringUtils.substringAfterLast(element.attr("href"), "/").replace(".html", "");
					ThreadPoolUtil.execute(new Runnable()
					{

						@Override
						public void run()
						{
							downloadTorrent(id);
						}

					});
					content.append("<a href=\"file:///" + dir + id + ".torrent\" >" + id + ".torrent" + "</a>").append("<br>\n")
							.append("<br>\n").append("<br>\n").append("<br>\n");
				}
				else
				{
					content.append(element.outerHtml()).append("<br>\n");
				}
			}
		});
	}

	private void parseCommon(final StringBuilder content, HtmlParser htmlParser)
	{
		htmlParser.parser(Arrays.asList("#read_tpc > a", "#read_tpc > img"), new Visitor()
		{

			@Override
			public void visit(final Element element)
			{
				String html = element.outerHtml();
				if (html.startsWith("<a href"))
				{

					final String id = StringUtils.substringAfterLast(element.attr("href"), "/").replace(".html", "");
					ThreadPoolUtil.execute(new Runnable()
					{

						@Override
						public void run()
						{
							downloadTorrent(id);
						}

					});
					content.append("<a href=\"file:///" + dir + id + ".torrent\" >" + id + ".torrent" + "</a>").append("<br>\n")
							.append("<br>\n").append("<br>\n").append("<br>\n");
				} else
				{
					content.append(element.outerHtml()).append("<br>\n");
				}

			}
		});
	}

	private void downloadTorrent(String id)
	{
		String fileName = dir + id + ".torrent";
		if (new File(fileName).exists())
		{
			return;
		}
		HttpRequestContext context = new HttpRequestContext(DOWNLOAD_URL);
		context.getParams().put("type", "torrent");
		context.getParams().put("id", id);
		context.getParams().put("name", id);
		context.getHeaders()
				.put("Referer", DOWNLOAD_REFERER + id + ".html");

		context.setFileName(fileName);
		try
		{
			new HttpRequestImpl().request(context);
		} catch (IOException e)
		{
			System.err.println("异常" + id);
			e.printStackTrace();
		}
	}

	public List<String> queryIndex(final int requireMonth, final int requireDate) throws IOException
	{
		final List<String> pageList = new ArrayList<>();
		int page = 1;
		while (true)
		{
			try
			{
				String url = indexUrl + "&page=" + (page++);
				HtmlParser htmlParser = new HtmlParser(new HttpRequestImpl().request(new HttpRequestContext(url)).getContent());
				htmlParser.parser(Arrays.asList("#ajaxtable > tbody:nth-child(2) > tr > td > h3 > a"), new Visitor()
				{

					@Override
					public void visit(Element element)
					{
						String title = element.text();
						if (!title.equalsIgnoreCase(element.html()))
						{
							return;
						}
						int month = Integer.parseInt(title.substring(1, 3));
						int date = Integer.parseInt(title.substring(4, 6));
						if (month < requireMonth || date < requireDate)
						{
							throw new IllegalStateException("列表获取完成" + title);
						}
						if (month != requireMonth || date != requireDate)
						{
							return;
						}

						for (String content : contents)
						{
							if (title.contains(content))
							{
								pageList.add(hrefPrefix + element.attr("href"));
							}
						}
					}
				});
			} catch (IllegalStateException e)
			{
				break;
			}
		}
		return pageList;
	}
}
