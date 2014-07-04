package org.test.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.Gson;

public class DouBan {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		login("zhouyuzhy@126.com", "18368017837");
		// HttpClient client = new HttpClient();
		// HttpMethod method = new
		// GetMethod("http://douban.fm/j/mine/playlist");
		// NameValuePair[] nvp = new NameValuePair[7];
		// nvp[0] = new NameValuePair("context",
		// "channel:0|musician_id:104301");
		// nvp[1] = new NameValuePair("type", "n");
		// nvp[2] = new NameValuePair("sid", "");
		// nvp[3] = new NameValuePair("pt", "0.0");
		// nvp[4] = new NameValuePair("channel", "0");
		// nvp[5] = new NameValuePair("from", "mainsite");
		// nvp[6] = new NameValuePair("r", "727977a189");
		// method.setQueryString(nvp);
		// int status = client.executeMethod(method);
		// if (status == 200) {
		// analysisHtml(method.getResponseBodyAsString());
		// }
	}

	private static void login(String username, String password)
			throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(
				"https://www.douban.com/accounts/login");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new NameValuePair("form_email", username));
		pairs.add(new NameValuePair("form_password", password));
		method.setRequestBody(pairs.toArray(new NameValuePair[] {}));
		int status = client.executeMethod(method);
		if (status == 302) {
			
		}
	}

	@SuppressWarnings("unchecked")
	private static void analysisHtml(String responseBodyAsString) {
		Gson gson = new Gson();
		List<Map> songs = (List<Map>) (gson.fromJson(responseBodyAsString,
				HashMap.class)).get("song");
		for (Map song : songs) {
			System.out.println(song);
		}

	}

}
