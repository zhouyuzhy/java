package org.test.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.test.httpclient.HttpUtils;
import org.test.httpclient.HttpUtils.Response;
import org.test.httpclient.JSONUtils;

import com.alibaba.fastjson.JSONObject;

public class ChouJiang {

	public static final int times = 20;

	public static final long sleepTime = 2000;

	public static final String HOST = "http://c.3g.163.com";

	public static final String LOTTO_PATH = HOST + "/uc/api/prize/slot";

	public static final String LOTTO_BODY = "MOW5BEgrExQKeW7+pSsJkJbsI0JKEmfDhlvshukTfgXMoTbjRaIghrFj1Jur+ECDOghi8uMfgsltTmmwG2rnEozylM1Gk2XLW75XXLPy4zmygkTiDgwUBOt6uUcg+BUHPdElFEpF/o1P5Lu63IzDj28dD3n5qPyrSWoxw5ldy+";
	
	public static final String GOLDEN_PATH = HOST + "/uc/api/visitor/profile";

	public static final String GOLDEN_BODY = "f3xR2O12oZ2GhhR0xp1GXbX2WMCoMqapk08GhEzPqX4=";
	
	public static final Map<String, String> LOTTO_HEADER_MAP = new HashMap<String, String>() {
		{
			put("Host", "c.3g.163.com");
			put("Connection","keep-alive");
			put("Referer", "http://c.3g.163.com/CreditMarket/default.html");
			put("Origin", "http://c.3g.163.com");
			put("X-Requested-With", "XMLHttpRequest");
			put("Accept", "application/json");
			put("x-wap-profile","http://218.249.47.94/Xianghe/MTK_Phone_JB2_UAprofile.xml");
			put("User-Agent","Mozilla/5.0 (Linux; U; Android 4.2.2; zh-cn; HM NOTE 1W Build/JDQ39) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 NewsApp/3.9.2");
			put("COOKIE", "_ntes_nnid=041546940e432c6debb575e042d0bff1,1404483998458; _ntes_nuid=041546940e432c6debb575e042d0bff1");
			
			put("User-D", "jv5/0zK7usyFbe9bJ9hdjQ==");
			put("User-U", "f3xR2O12oZ2GhhR0xp1GXbX2WMCoMqapk08GhEzPqX4=");
		}
	};
	
	public static final Map<String, String> GOLDEN_HEADER_MAP = new HashMap<String, String>() {
		{
			put("User-U", "f3xR2O12oZ2GhhR0xp1GXbX2WMCoMqapk08GhEzPqX4=");
			put("User-D", "jv5/0zK7usyFbe9bJ9hdjQ==");
			put("Host", "c.3g.163.com");
			put("Connection","keep-alive");
			put("User-Agent","NTES Android");
		}
	};

	public static int lucky = 0;
	public static int unLocky = 0;
	public static final List<String> awardList = new ArrayList<String>();
	public static int goldcoin = 0;

	public static void main(String[] args) {
		process();
	}

	private static void process() {
		goldcoin = getGoldcoin();
		System.out.println("开始抽奖,剩余金币:" + goldcoin);
		for (int i = 1; i <= times; i++) {
			try{
				run(i);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		goldcoin = getGoldcoin();
		System.out.println("结束抽奖,剩余金币:" + goldcoin);
		statistics();
	}

	private static void run(int index) throws InterruptedException{
		doLotto(index);
		
		// 模拟手机行为
		getGoldcoin();
		
		Thread.sleep(sleepTime);
	}
	
	private static int getGoldcoin() {
		Response resp = HttpUtils.httpPost(GOLDEN_PATH, GOLDEN_BODY , GOLDEN_HEADER_MAP);
		int goldcoin = -1;
		if (resp.isRequestSuccess(GOLDEN_PATH)) {
			JSONObject obj = JSONUtils.toJSONObject(resp.content);
			goldcoin = obj.getIntValue("goldcoin");
		}
		return goldcoin;
	}

	private static void doLotto(int index) {
		String body = LOTTO_BODY + "fTGSfi/brdRgZHENsCw2Q8";
		Response resp = HttpUtils.httpPost(LOTTO_PATH, body, LOTTO_HEADER_MAP);
		if (resp.isRequestSuccess(LOTTO_PATH)) {
			JSONObject obj = JSONUtils.toJSONObject(resp.content);
			System.out.printf("第%d次抽奖:", index);
			if (obj.getBooleanValue("lucky")) {
				lucky++;
				String name = obj.getString("name");
				awardList.add(name);
				System.out.println(name);
				unLocky=0;
			} else {
				unLocky++;
				System.out.println("没中");
			}
		}
//		if(unLocky >= 3) {
//			System.err.println("unLocky="+ unLocky + "换个key吧...");
//			System.exit(1);
//		}
	}

	private static void statistics() {
		System.out.println("抽奖总数:" + times);
		System.out.println("抽中次数:" + lucky);
		System.out.println("抽中奖品:");
		for(String str : awardList) {
			System.out.println(str);
		}
	}

}
