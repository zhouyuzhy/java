package org.test.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class CaiPiao1 {

	/**
	 * @param args中文
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void main(String[] args) throws HttpException, IOException {
		List<Integer> periods = new ArrayList<Integer>();
		periods.add(500);
//		periods.add(50);
//		periods.add(100);
//		periods.add(300);
//		periods.add(500);
//		periods.add(700);
//		periods.add(1000);
		//SSQ(periods);
		DLT(periods);
	}
	
	private static void SSQ(List<Integer> periods) throws HttpException, IOException{
		HttpClient client = new HttpClient();
		for(int i : periods){
			HttpMethod method = new GetMethod("http://zx.caipiao.163.com/trend/ssq_historyPeriod.html?periodNumber=" + i);
			int status = client.executeMethod(method);
			if(status == 200){
				analysisSSQHTML(method.getResponseBodyAsString());
			}
		}
		TreeMap<String, String> m = map.get("SSQ蓝1");
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(new ValueComparator(m));
		sortedMap.putAll(m);
		System.out.println(sortedMap);
	}
	
	private static void DLT(List<Integer> periods) throws HttpException, IOException{
		HttpClient client = new HttpClient();
		for(int i : periods){
			HttpMethod method = new GetMethod("http://zx.caipiao.163.com/trend/dlt_basic.html?periodNumber=" + i);
			int status = client.executeMethod(method);
			if(status == 200){
				analysisDLTHTML(method.getResponseBodyAsString());
			}
		}
		TreeMap<String, String> m = map.get("DLT蓝1");
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(new ValueComparator(m));
		sortedMap.putAll(m);
		System.out.println(sortedMap);
	}
	/**
	 * 01 05 13 26 28 32
	 *	09 15
	 * @param html
	 */
	public static void analysisSSQHTML(String html){
		Document doc = Jsoup.parse(html);
		SSQBall(doc);
	}

	public static void analysisDLTHTML(String html){
		Document doc = Jsoup.parse(html);
		DLTBall(doc);
	}
	
	/**
	 * @param doc
	 * @param ballClassValue
	 * @return
	 */
	private static Map<String, Integer> DLTBall(Document doc) {
		Element tbody = doc.getElementById("cpdata");
		for(Element tr : tbody.getElementsByTag("tr")){
			Element td = tr.getElementsByAttributeValue("align", "center").first();
			if(td == null)
				continue;
			String qiShu = td.ownText();
			List<String> red = new ArrayList<String>();
			for(Element tdRed : tr.getElementsByAttributeValue("class", "chartBall01"))
			{
				red.add(tdRed.ownText());
			}
			for(Element tdRed : tr.getElementsByAttributeValue("class", "chartBall07"))
			{
				red.add(tdRed.ownText());
			}
			List<String> blue = new ArrayList<String>();
			for(Element tdBlue: tr.getElementsByAttributeValue("class", "chartBall02"))
			{
				blue.add(tdBlue.ownText());
			}
			saveForDLTOneLine(qiShu, red, blue);
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		return result;
	}
	
	private static void saveForDLTOneLine(String qiShu, List<String> red,
			List<String> blue) {
		for(int i = 1 ; i <= 5; i++)
			saveForBall(qiShu, red, i, "DLT红");
		for(int i = 1 ; i <= 2; i++)
			saveForBall(qiShu, blue, i, "DLT蓝");
	}
	/**
	 * @param doc
	 * @param ballClassValue
	 * @return
	 */
	private static Map<String, Integer> SSQBall(Document doc) {
		Element tbody = doc.getElementById("cpdata");
		for(Element tr : tbody.getElementsByTag("tr")){
			Element td = tr.getElementsByAttribute("title").first();
			if(td == null)
				continue;
			String qiShu = td.ownText();
			List<String> red = new ArrayList<String>();
			for(Element tdRed : tr.getElementsByAttributeValue("class", "c_ba2636"))
			{
				red.add(tdRed.ownText());
			}
			String blue = tr.getElementsByAttributeValue("class", "c_1e50a2").first().ownText();
			saveForSSQOneLine(qiShu, red, blue);
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		return result;
	}
	private static void saveForSSQOneLine(String qiShu, List<String> red,
			String blue) {
		for(int i = 1 ; i <= 6; i++)
			saveForBall(qiShu, red, i, "SSQ红");
		saveForBall(qiShu, Arrays.asList(new String[]{blue}), 1, "SSQ蓝");
	}

	private static void saveForBall(String qiShu, List<String> ball, int count, String key) {
		Combination comb = new Combination();
		comb.mn(ball.toArray(new String[0]), count);
		for(String r : comb.getCombList())
		{
			saveInBaidu(qiShu, key+r, key+count);
		}
	}
	private static void saveInBaidu(String value, String key, String fileName) {
		//save list
		String oldValue = getInBaiduForKey(key, fileName);
		String newValue = oldValue;
		if(StringUtil.isBlank(oldValue))
		{
			newValue = value;
		}
		else
		{
			if(!oldValue.contains("," + value) && !oldValue.contains(value+","))
			{
				List<String> oldList = Arrays.asList(oldValue.split(","));
				List<String> newList = new ArrayList<String>(oldList);
				newList.add(value);
				Collections.sort(newList);
				newValue = StringUtils.join(newList,",");
			}
		}
		TreeMap<String, String> localMap = map.get(fileName);
		if(localMap == null)
			localMap = new TreeMap<String, String>();
		localMap.put(key, newValue);
		map.put(fileName, localMap);
	}

	
	private static String getInBaiduForKey(String key, String fileName) {
		Map<String, String> localMap = map.get(fileName);
		if(localMap == null)
			return null;
		return localMap.get(key);
	}


	private static Map<String, TreeMap<String, String>> map = new HashMap<String, TreeMap<String, String>>();
}

class ValueComparator implements Comparator<String> {

    Map<String, String> base;
    public ValueComparator(Map<String, String> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (getCount(base.get(a)) > getCount(base.get(b))) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
    
    private int getCount(String a){
    	return StringUtils.isBlank(a) ? 0 : a.split(",").length;
    }
}
