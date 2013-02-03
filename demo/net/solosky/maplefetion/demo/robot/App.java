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
 * Project  : MapleSMS
 * Package  : net.solosky.maplesms
 * File     : App.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot;


/**
 *
 * 机器人应用接口
 *
 * @author solosky <solosky772@qq.com>
 */
public interface App
{
	/**
	 * 返回这个应用的名字
	 * @return
	 */
	public String getName();
	/**
	 * 返回这个应用接受的命令格式
	 * @return
	 */
	public String getFormat();
	
	/**
	 * 返回这个应用的简单介绍
	 * @return
	 */
	public String getIntro();
	
	/**
	 * 初始化
	 */
	public void init();
	
	/**
	 * 是否接受用户当前输入的命令
	 * @param msg
	 * @return
	 */
	public boolean accept(String msg);
	
	/**
	 * 执行当前用户输入的命令
	 * @param msg
	 * @param uri
	 * @param gateway
	 * @throws Exception
	 */
	public void action(String msg, String uri, Gateway gateway) throws Exception;
}
