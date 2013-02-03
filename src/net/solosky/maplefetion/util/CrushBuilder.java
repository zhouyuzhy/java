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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.util
 * File     : BugReporter.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-1
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.sipc.SipcMessage;

import org.apache.log4j.Logger;

/**
 *
 * 错误报告工具
 * 如果客户端发生错误了，可以使用这个工具把报告发送到指定的接口
 * （接口还未实现）
 *
 * @author solosky <solosky772@qq.com>
 */
public class CrushBuilder
{
	/**
	 * 字符缓冲对象
	 */
	private StringBuffer buffer;
	
	/**
	 * LOGGER
	 */
	private static Logger logger = Logger.getLogger(CrushBuilder.class);
	
	/**
	 * 默认构造函数
	 */
	public CrushBuilder()
	{
		buffer = new StringBuffer();
	}
	
	/**
	 * 构建异常报告
	 * @param exception
	 * @return
	 */
	public void dumpException(Throwable exception)
	{
		buffer.append("------------------------------Exception-----------------------------------\r\n");
		StringWriter writer = new StringWriter();
		exception.printStackTrace(new PrintWriter(writer));
		buffer.append(writer.toString());
		buffer.append("\r\n");
	}
	
	/**
	 * 构建消息报告
	 * @param message
	 */
	public void dumpSipcMessage(SipcMessage message)
	{
		buffer.append("-------------------------------SipcMessage----------------------------------\r\n");
		buffer.append("Class:"+message.getClass().toString()+"\r\n");
		buffer.append("----------\r\n");
		buffer.append(message.toSendString());
		buffer.append("\r\n");
	}
	
	/**
	 * 构建报告的头部
	 */
	public void buildHeader()
	{
		buffer.append("=============================MapleFetion CrushReport=================================\r\n");
		buffer.append("Thank you for working with MapleFetion!!! \r\n");
		buffer.append("ProjectHome : http://maplefetion.googlecode.com \r\n");
		buffer.append("Author      : solosky <solosky772@qq.com> \r\n");
		buffer.append("AuthorBlog  : http://www.solosky.net \r\n");
		buffer.append("-------------------------------------------------------------------------------------\r\n");
		buffer.append("\r\n");
		buffer.append("Ops, if you saw this report, FetionClient was crushed by some exceptions. Sorry for that.\r\n");
		buffer.append("Please Email this crush file or log file to author to find and fix bugs, thank you sincerely.\r\n");
		buffer.append("\r\n");
		buffer.append("CrushedDate:"+(new Date()).toString());
		buffer.append("\r\n\r\n");
	}
	
	/**
	 * 建立报告的尾部
	 */
	public void buildFooter()
	{
		  buffer.append("===================================================================================");
          buffer.append("\r\n\r\n\r\n");
	}
	
	/**
	 * 打印版本信息
	 */
	public void dumpVersion()
	{
		buffer.append("------------------------------------Version---------------------------------------\r\n");
		buffer.append("ClientVersion   : "+FetionClient.CLIENT_VERSION+"\r\n");
		buffer.append("ProtocolVersion : "+FetionClient.PROTOCOL_VERSION+"\r\n");
		buffer.append("\r\n");
	}
	
	/**
	 * 打印配置
	 */
	public void dumpConfig()
	{
		buffer.append("-------------------------------------Config--------------------------------------\r\n");
		
		Properties prop = FetionConfig.getProperties();
		Iterator<Object> it = prop.keySet().iterator();
		while(it.hasNext()) {
			String key = (String)  it.next();
			buffer.append(key+" = " + prop.getProperty(key)+"\r\n");
		}
		
		buffer.append("\r\n");
	}
	
	/**
	 * 打印一个对象
	 * @param o
	 */
	public void dumpObject(Object o)
	{
		buffer.append("------------------"+o.getClass().getName()+"-----------------------\r\n");
		buffer.append(o.toString());
		buffer.append("\r\n");
	}
	
	/**
	 * 打印一行字符
	 */
	public void dumpString(String s)
	{
		buffer.append("------------------String Message-----------------------\r\n");
		buffer.append(s);
		buffer.append("\r\n");
	}
	
	/**
	 * 返回建立的错误报告
	 */
	public String toString()
	{
		return this.buffer.toString();
	}

	/**
	 * 建立并保存报告
	 * @param t
	 * @param f
	 * @throws IOException 
	 */
	private static void buildAndSaveCrushReport(Object ...args)
	{
		File dir = new File(FetionConfig.getString("crush.log.dir"));
		if(dir.isDirectory()) {
		}else if(dir.isFile()) {
			logger.warn("Crush log directory is a file...");
		}else {
			if(!dir.mkdir()) {
				logger.warn("Create crush log dir failed."+dir.getAbsolutePath());
				return;
			}
		}
		DateFormat df = new SimpleDateFormat("yyyy-M-d");
		String name = dir.getAbsolutePath()+File.separator+"CrushReport_"+df.format(new Date())+".txt";
		try {
			FileWriter writer = new FileWriter(new File(name), true);
            writer.append(CrushBuilder.buildCrushReport(args));
            writer.close();
            logger.debug("Crush report saved to "+name);
        } catch (IOException e) {
        	logger.warn("Save crush report failed.");
        }
	}
	
	/**
	 * 把错误报告发送到指定的URL
	 * @param args
	 */
	private static void buildAndSendCrushReport(Object ...args)
	{
		String url    = FetionConfig.getString("crush.send.url");
		String report = buildCrushReport(args);
		if(url!=null) {
			try {
	            url = url.replace("{report}", URLEncoder.encode(report, "utf8"));
            } catch (UnsupportedEncodingException e1) {
	           throw new RuntimeException(e1);
            }
			System.out.println(url);
			try {
	            URL trueURL = new URL(url);
	            logger.info("Sending crush report...");
	            HttpURLConnection conn = (HttpURLConnection) trueURL.openConnection();
	            if(conn.getResponseCode()==200) {
	            	logger.info("Send crush report OK.");
	            }else {
	            	logger.info("Send crush report Failed. status="+conn.getResponseCode()+" "+conn.getResponseMessage());
	            }
            } catch (MalformedURLException e) {
            	logger.info("send crush report Failed",e);
            } catch (IOException e) {
            	logger.info("send crush report Failed",e);
            }
		}
	}
	
	/**
	 * 建立错误报告
	 * @param args 变长参数，类型可以是Throwable和SipcMessage
	 * @return
	 */
	private static String buildCrushReport(Object ...args)
	{
		CrushBuilder cb = new CrushBuilder();
		cb.buildHeader();
		cb.dumpVersion();
		cb.dumpConfig();
		dumpObjectArray(cb, args);
		cb.buildFooter();
		
		return cb.toString();
	}
	
	/**
	 * 递归的打印数组对象
	 * @param cb
	 * @param args
	 */
	private static void dumpObjectArray(CrushBuilder cb, Object[] args)
	{
		if(args!=null){
			for(Object o:args) {
				if(o instanceof SipcMessage) {
					cb.dumpSipcMessage((SipcMessage) o);
				}else if(o instanceof Throwable) {
					cb.dumpException((Throwable) o);
				}else if(o instanceof String) {
					cb.dumpString((String) o);
				}else if(o.getClass().isArray()){
					dumpObjectArray(cb, (Object[]) o);
				}else if(o!=null){
					cb.dumpObject(o);
				}else{}
			}
		}
	}
	
	/**
	 * 处理错误报告
	 * @param args
	 */
	public static void handleCrushReport(Object ...args)
	{
		if(FetionConfig.getBoolean("crush.send.enable")) {
			buildAndSendCrushReport(args);
		}else if(FetionConfig.getBoolean("crush.log.enable")) {
			buildAndSaveCrushReport(args);
		}else {
			//do nothing...
		}
	}

}
