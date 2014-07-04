package org.test.httpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import sun.misc.BASE64Encoder;

import com.google.gson.Gson;

public class Music {

	public static String dir = "D:/mp3/";
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		for (String artist : new String[] { "周杰伦", "S.H.E", "田馥甄", "梁静茹",
				"孙燕姿", "张靓颖","张惠妹" }) {
			try {
				String id = search(artist);
				List<String> mp3s = requestTop50(id);
				for (String mp3 : mp3s) {
					if(new File(dir+mp3).exists())
						continue;
					try {
						String[] result = mp3.split("#");
						String name = result[0];
						String url = result[1];
						download(name, url);
					} catch (Exception e) {
						System.err.println(mp3);
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.err.println(artist);
				e.printStackTrace();
			}
		}
	}

	public static void download(String name, String url) throws HttpException,
			IOException {
		System.out.println(url);
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		method.getParams().setContentCharset("UTF-8");
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		setCookie(method);
		int status = client.executeMethod(method);
		if (status == 200) {
			InputStream is = (method.getResponseBodyAsStream());
			FileOutputStream fos = new FileOutputStream(dir + name
					+ ".mp3");
			byte[] temp = new byte[1024];
			int len = 0;
			while ((len = is.read(temp, 0, 1024)) != -1) {
				fos.write(temp, 0, len);
			}
			System.out.println(name + " 下载完成！");
			is.close();
			fos.close();
		}
	}

	private static List<String> requestTop50(String id) throws HttpException,
			IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod("http://music.163.com/api/artist/"
				+ id + "?top=50");
		method.getParams().setContentCharset("UTF-8");
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		setCookie(method);
		int status = client.executeMethod(method);
		if (status == 200) {
			return anayliseHTML(method.getResponseBodyAsString());
		}
		return null;
	}

	private static void setCookie(HttpMethod method) {
		method.setRequestHeader(
				"Cookie",
				"MUSIC_U=0eea34ad7591b54df6223d04816329c6db32c075d605af3d14f29636de1a8a75a115ff12972bbbc7b55765cbc246c265bad53574e915ac36; usertrack=ezq0aVEKlMGk21GQBxAnAg==");
		// +
		// "deviceId=860173013067420; appver=1.3.0; os=android; osver=2.3.5; mobilename=MI-ONE Plus; "
		// + "resolution=854x480; channel=official");
	}

	// /_hCXT00xYeRN85zTnJ0kAg==/1287528116133971.mp3
	private static List<String> anayliseHTML(String responseBodyAsString) {
		List<String> ret = new ArrayList<String>();
		Gson gson = new Gson();
		HashMap result = gson.fromJson(responseBodyAsString, HashMap.class);
		List<Map> hotSongs = (List<Map>) result.get("hotSongs");
		for (Map song : hotSongs) {
			String name = (String) song.get("name");
			String hUrl = "";
			Object hMusic = song.get("hMusic");
			if (hMusic != null) {
				long id = ((Double) ((Map) hMusic).get("dfsId")).longValue();
				hUrl = getMp3Url(id);
			}
			ret.add(name + "#" + hUrl);
		}
		return ret;
	}

	private static String base64(String paramString) {
		while (true) {
			int j = 0;
			String str = "";
			try {
				byte[] arrayOfByte1 = "3go8&$8*3*3h0k(2)2"
						.getBytes("ISO_8859_1");
				byte[] arrayOfByte2 = paramString.getBytes("ISO_8859_1");
				char[] arrayOfChar;
				for (int i = 0;; ++i)
					if (i >= arrayOfByte2.length) {
						MessageDigest localMessageDigest = MessageDigest
								.getInstance("MD5");
						localMessageDigest.update(arrayOfByte2);
						arrayOfChar = new BASE64Encoder().encode(
								localMessageDigest.digest()).toCharArray();
						if (j >= arrayOfChar.length) {
							str = new String(arrayOfChar);
							return str;
						}
						break;
					} else {
						arrayOfByte2[i] = (byte) (arrayOfByte2[i] ^ arrayOfByte1[(i % "3go8&$8*3*3h0k(2)2"
								.length())]);
					}
				for (int t = 0; t < arrayOfChar.length; t++) {
					if (arrayOfChar[t] == '/')
						arrayOfChar[t] = '_';
					if (arrayOfChar[t] == '+')
						arrayOfChar[t] = '-';
				}
				return new String(arrayOfChar);
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
				localUnsupportedEncodingException.printStackTrace();
				str = null;
				break;
			} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
				localNoSuchAlgorithmException.printStackTrace();
				break;
			}
		}
		return null;
	}

	public static String getMp3Url(long paramLong) {
		return "http://m" + (1 + new Random().nextInt(2)) + ".music.126.net/"
				+ base64(String.valueOf(paramLong)) + "/" + paramLong + ".mp3";
	}

	public static String search(String name) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(
				"http://music.163.com/api/search/get");
		method.getParams().setContentCharset("UTF-8");
		method.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		setCookie(method);
		List<NameValuePair> pair = new ArrayList<NameValuePair>();
		pair.add(new NameValuePair("limit", "10"));
		pair.add(new NameValuePair("sub", "false"));
		pair.add(new NameValuePair("s", name));
		pair.add(new NameValuePair("type", "100"));
		pair.add(new NameValuePair("offset", "0"));
		method.setRequestBody(pair.toArray(new NameValuePair[0]));
		int status = client.executeMethod(method);
		System.out.println(status);
		if (status == 200) {
			String resp = method.getResponseBodyAsString();
			Gson gson = new Gson();
			HashMap map = gson.fromJson(resp, HashMap.class);
			Map artist = (((List<Map>) ((Map) map.get("result")).get("artists"))
					.get(0));
			System.out.println("name=" + artist.get("name"));
			return String.valueOf((Double) artist.get("id"));
		}
		return null;
	}

}
