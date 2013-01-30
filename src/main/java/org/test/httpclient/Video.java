package org.test.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Video {

	public static Map<String, String> history = new HashMap<String, String>();
	public static String pre = "http://www.dytt8.net";
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://www.dytt8.net/html/gndy/dyzz/index.html");
		method.getParams().setContentCharset("GBK");
		int status = client.executeMethod(method);
		if(status == 200){
			analysisHTML(method.getResponseBodyAsString());
		}
	}

	private static void analysisHTML(String responseBodyAsString) throws UnsupportedEncodingException {
		Document doc = Jsoup.parse(responseBodyAsString);
		List<Element> elements = doc.getElementsByAttributeValue("class", "ulink");
		for(Element e : elements){
			String href = e.attr("href");
			String name = e.text();
			if(!history.containsKey(href)){
				//不存在
				history.put(pre + href, name);
				//发送监听事件
			}
		}
		System.out.println(history);
	}
	
	private static void initHistory(){
		//反序列化
		history.put("abcd", "acb");
	}
	
	public static void storeHistory(){
		//序列化history
	}

}
