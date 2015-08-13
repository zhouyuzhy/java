/**
 * 
 */
package org.test.httpclient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author hzzhoushaoyu
 *
 * @date 2015年8月13日
 */
public class Text
{
	public static void main(String[] args) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://www.kanunu8.com/wuxia/201102/1626.html");
		int status = client.executeMethod(method);
		if (200 == status)
		{
			String html = new String(method.getResponseBodyAsString().getBytes("ISO-8859-1"), "gbk");
			Document doc = Jsoup.parse(html);
			Elements eles = doc.getElementsByAttributeValueMatching("href", "1626/\\d+\\.html");
			Iterator<Element> iter = eles.iterator();
			FileOutputStream fos = new FileOutputStream("D:/tianlong.txt");
			Writer w = new OutputStreamWriter(fos);
			while (iter.hasNext())
			{
				Element element = iter.next();
				String url = "http://www.kanunu8.com/wuxia/201102/" + element.attr("href");
				read(url, w);
			}
			w.close();
		}
	}

	/**
	 * @param url
	 * @throws IOException 
	 * @throws HttpException 
	 */
	private static void read(String url, Writer os) throws HttpException, IOException
	{
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		int status = client.executeMethod(method);
		if (200 == status)
		{
			Document doc = Jsoup.parse(new String(method.getResponseBodyAsString().getBytes("ISO-8859-1"), "gbk"));

			String title = "\t\t\t" + doc.getElementsByTag("h2").get(0).text();
			String content = doc.getElementsByTag("p").get(0).html().replaceAll("<br />", "\r\n")
					.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\t");

			os.append(title + "\r\n\r\n");
			os.append(content + "\r\n\r\n");
			System.out.println(title);
		}
	}
}
