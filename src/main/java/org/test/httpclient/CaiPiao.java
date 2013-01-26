package org.test.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CaiPiao {

	/**
	 * @param args中文
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://zx.caipiao.163.com/trend/ssq_historyPeriod.html?periodNumber=100");
		int status = client.executeMethod(method);
		if(status == 200){
			analysisHTML(method.getResponseBodyAsString());
		}
	}
	/**
	 * 01 05 13 26 28 32
	 *	09 15
	 * @param html
	 */
	public static void analysisHTML(String html){
		Document doc = Jsoup.parse(html);
		String redBallClassValue = "c_ba2636";
		Map<String, Integer> red = calBall(doc, redBallClassValue);
		String blueBallClassValue = "c_1e50a2";
		Map<String, Integer> blue = calBall(doc, blueBallClassValue);
	}

	/**
	 * @param doc
	 * @param ballClassValue
	 * @return
	 */
	private static Map<String, Integer> calBall(Document doc,
			String ballClassValue) {
		Map<String, Integer> balls = new HashMap<String, Integer>();
		for(Element td : doc.getElementsByAttributeValue("class", ballClassValue)){
			if(td.tagName()!="td")
				continue;
			String ball = (td.text());
			
			Integer num = balls.get(ball);
			if(num==null){
				num = 0;
			}
			balls.put(ball, num + 1);
		}
		List<Map.Entry<String, Integer>> sortResult = new ArrayList<Map.Entry<String, Integer>>(balls.entrySet());
		Collections.sort(sortResult,
		new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue() - o1.getValue();
			};
		});
		System.out.println(sortResult);
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(Map.Entry<String, Integer> i : sortResult){
			result.put(i.getKey(), i.getValue());
		}
		return result;
	}

}
