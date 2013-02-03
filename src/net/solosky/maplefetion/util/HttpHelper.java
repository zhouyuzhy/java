/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Project  : MapleFetion2
 * Package  : net.solosky.maplefetion.util
 * File     : HttpHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
*
* HTTP工具类
* 因为飞信很多应用都是基于HTTP的，这个HTTP工具可以方便的完成HTTP操作
*
* @author solosky <solosky772@qq.com>
*/

public class HttpHelper
{
	/**
	 * 创建默认的HTTP连接
	 * @param url		URL
	 * @param method	方法 POST|GET
	 * @param ssic		SSIC
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection openConnection(String url, String method, String contentType, String ssic) throws IOException
	{
		URL realURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) realURL.openConnection();
		//conn.setRequestProperty("User-Agent", "IIC2.0/PC "+FetionClient.PROTOCOL_VERSION);
		conn.setRequestProperty("User-Agent", "IIC2.0/PC 4.0.0000");
		conn.setRequestProperty("Cookie", "ssic="+ssic);
		conn.setRequestProperty("Host", realURL.getHost());
		conn.setRequestProperty("Accept", "*/*");
		conn.setRequestProperty("Cache-Control", "no-cache");
		if(contentType!=null)	conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestMethod(method);
		return conn;
	}
	
	/**
	 * 获取一个连接里面数据
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public static byte[] fetchData(HttpURLConnection conn) throws IOException
	{
		InputStream in = conn.getInputStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while((len=in.read(buff, 0, 1024))!=-1){
			out.write(buff, 0, len);
		}
		return out.toByteArray();
	}
	
	/**
	 * 发送数据到这个连接里
	 * @param conn
	 * @param in
	 * @throws IOException
	 */
	public static void sendData(HttpURLConnection conn, InputStream in) throws IOException
	{
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		byte[] buff = new byte[1024];
		int len = 0;
		while((len=in.read(buff, 0, 1024))!=-1){
			out.write(buff, 0, len);
		}
	}
	
	/**
	 * 获取一个连接的文本内容
	 * @param url
	 * @param method
	 * @param user
	 * @return
	 * @throws IOException
	 */
	public static String doFetchString(String url, String method, String ssic, InputStream in) throws IOException
	{
		return new String(doFetchData(url, method, "application/x-www-form-urlencoded", ssic, in));
	}
	
	/**
	 * 获取一个连接的数据
	 * @param url
	 * @param method
	 * @param user
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static byte[] doFetchData(String url, String method, String contentType, String ssic, InputStream in) throws IOException
	{
		HttpURLConnection conn = openConnection(url, method, contentType, ssic);
		if(in!=null)	sendData(conn, in);
		if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
			return fetchData(conn);
		}else{
			throw new IOException("Http response is not OK. code="+conn.getResponseCode());
		}
	}
	
	/**
	 * 尝试获取指定地址的数据，会尝试指定的次数，如果成功马上返回
	 * @param url		地址
	 * @param method	方法
	 * @param ssic 		SSIC
	 * @param in		输入流，如果需要传输数据
	 * @param tryTimes	重试次数
	 * @return
	 */
	public static byte [] tryFetchData(String url, String method, String contentType, String ssic, InputStream in, int tryTimes)
	{
		for(int i=0; i<tryTimes; i++){
			try {
				return doFetchData(url, method, contentType, ssic, in);
			} catch (IOException e) {
				Logger.getLogger(HttpHelper.class).info("Fetch url data failed: "+e.getMessage()+" - doTryTimes:"+i+", maxTryTimes:"+tryTimes);
			}
		}
		return null;
	}
}
