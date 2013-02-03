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
 * Package  : net.solosky.maplefetion
 * File     : FetionConfig.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-23
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

/**
 *
 *	飞信配置
 *
 * @author solosky <solosky772@qq.com> 
 */
public class FetionConfig
{
	private static Properties prop;
	private static boolean initialized;
	
	/**
	 * 初始化，强制初始化
	 */
	public synchronized static void init()
	{
		if(!initialized) {
			loadDefaultConfig();
			loadUserConfig();
			initialized = true;
		}
	}
	
	/**
	 * 返回配置属性
	 */
	public static Properties getProperties()
	{
		return prop;
	}
	
	/**
	 * 返回指定名字的字符串配置
	 * @param name
	 * @return
	 */
	public static String getString(String name)
	{
		if(prop.getProperty(name)!=null){
			return prop.getProperty(name);
		}else{
			throw new IllegalArgumentException("Config value not found in configs. name="+name);
		}
	}
	
	/**
	 * 设置指定名字的字符串配置
	 * @param name
	 * @param value
	 */
	public static void setString(String name, String value)
	{
		prop.setProperty(name, value);
	}
	
	/**
	 * 返回指定名字的整数配置
	 * @param name
	 * @return
	 */
	public static int getInteger(String name)
	{
		return Integer.parseInt(getString(name));
	}
	
	/**
	 * 设置指定名字的整数配置
	 * @param name
	 * @param value
	 */
	public static void setInteger(String name, int value)
	{
		prop.setProperty(name, Integer.toString(value));
	}
	
	/**
	 * 返回指定名字的布尔配置
	 * @param name
	 * @return
	 */
	public static boolean getBoolean(String name)
	{
		return Boolean.parseBoolean(getString(name));
	}
	
	/**
	 * 设置指定名字的布尔配置
	 * @param name
	 * @param value
	 */
	public static void setBoolean(String name, boolean value)
	{
		prop.setProperty(name, Boolean.toString(value));
	}
	
	/**
	 * 加载配置文件
	 * 用户可以手动的加载配置文件，如果用户手动的加载了配置文件，系统将会合并默认配置和用户配置
	 * @param file
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void loadConfig(File file) throws FileNotFoundException, IOException
	{
		Properties userDefinedProperties = new Properties();
		userDefinedProperties.load(new FileInputStream(file));
		Iterator<Object> it = userDefinedProperties.keySet().iterator();
		while(it.hasNext()) {
			String key = (String) it.next();
			prop.setProperty(key, userDefinedProperties.getProperty(key));
			
		}
		
	}
	
	
	/**
	 * 加载默认的配置文件
	 */
	private static void loadDefaultConfig()
	{
		//在类的根目录查找默认的系统配置文件，maplefetionDefault.properties
		//这个文件定义的所有飞信的配置，如果配置都是基于这个配置文件，因为存在惯性配置，所以不会存在配置不存在的错误
		InputStream in = FetionConfig.class.getResourceAsStream("/resources/maplefetion.default.properties");
		if(in!=null) {
			prop = new Properties();
			try {
	            prop.load(in);
            } catch (IOException e) {
            	throw new IllegalStateException("Error occured when reading maplefetionDefault.properties in classpath.");
            }
			
		}else {
			throw new IllegalStateException("Cannot find maplefetionDefault.properties in classpath.");
		}
	}
	
	/**
	 * 加载用户定义配置，默认存放在程序启动目录
	 */
	private static void loadUserConfig()
	{
		File file = new File("maplefetion.properties");
		if(file.exists()&&file.canRead()) {
			try {
				System.out.println("found user specified maplefetion.properties..");
	            loadConfig(file);
            } catch (IOException e) {
	            //忽略掉
            }
		}
	}
}
