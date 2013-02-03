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
 * File     : MapleSMS.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import net.solosky.maplefetion.demo.robot.app.HelloApp;
import net.solosky.maplefetion.demo.robot.app.WeatherApp;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class FetionRobot implements SMSListener
{
    private ArrayList<App> appList;
    private ArrayList<Gateway> gatewayList;
    public FetionRobot()
    {
    	println("系统初始化");
    	this.appList = new ArrayList<App>();
    	this.gatewayList = new ArrayList<Gateway>();
    }
    
    public void registerApp(App app)
    {
    	this.println("注册应用 - "+app.getName());
    	this.appList.add(app);
    }
    public void registerGateway(Gateway gateway)
    {
    	this.println("注册网关 - "+gateway.getName());
    	gateway.setSMSListener(this);
    	this.gatewayList.add(gateway);
    }

    @Override
    public void smsRecived(String uri, String msg, Gateway gateway)
    {
    	println("收到来自[ "+uri+" ]的消息: ["+msg+"]");
	    Iterator<App> it = this.appList.iterator();
	    while(it.hasNext()) {
	    	App app = it.next();
	    	if(app.accept(msg)) {
	    		try {
	                app.action(msg, uri, gateway);
                } catch (Exception e) {
                	println("处理消息时出错 "+e.getMessage());
                	gateway.sendSMS(uri, "对不起，系统出错，请稍候再试。");
                }
                return;
	    	}
	    }
	    
	    //NOT found a app to action..
	    gateway.sendSMS(uri, this.welcome());
    }
    
    
    public String welcome()
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("欢迎您使用MapleSMS - 短消息服务。\n");
    	buffer.append("可以提供的服务：\n");
    	Iterator<App> it = this.appList.iterator();
    	while(it.hasNext()) {
    		App app = it.next();
    		buffer.append(app.getName()+"-"+app.getFormat()+"-"+app.getIntro()+"\n");
    	}
    	buffer.append("您可以回复上面的格式获取信息，希望这些信息对你有帮助，祝你愉快~\n");
    	
    	return buffer.toString();
    }
    
    public void println(String msg)
    {
    	System.out.println("[MapleSMS] "+msg);
    }
    
    public void start()
    {
    	println("开始启动网关..");
    	this.gatewaysLogin();
    	println("初始化应用..");
    	this.initApps();
    	println("系统启动完成..");
    }
    
    public void stop()
    {
    	println("开始关闭网关..");
    	this.gatewaysLogout();
    	println("系统已经关闭..");
    }
    
    public void gatewaysLogin()
    {
    	Iterator<Gateway> it = this.gatewayList.iterator();
    	while(it.hasNext()) {
    		Gateway g = it.next();
    		println("尝试登陆网关 - "+g.getName());
    		g.login();
    	}
    }
    
    public void gatewaysLogout()
    {
    	Iterator<Gateway> it = this.gatewayList.iterator();
    	while(it.hasNext()) {
    		it.next().logout();
    	}
    }
    
    public void initApps()
    {
    	Iterator<App> it = this.appList.iterator();
    	while(it.hasNext()) {
    		it.next().init();
    	}
    }
    public void readCmd()
    {
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	String line = null;
    	println("你现在可以输入命令。(exit：退出)");
    	try {
	        while((line=reader.readLine())!=null) {
	        	if(line.equals("exit")) {
	        		this.gatewaysLogout();
	        		return;
	        	}
	        }
        } catch (IOException e) {
        	println("等待用户输入命令时出现错误。");
        }
    }
    
    public static void main(String[] args)
    {
    	if(args.length>=2){
	    	FetionRobot sms = new FetionRobot();
	    	sms.registerGateway(new FetionGateway(Long.parseLong(args[0]),args[1]));
	    	sms.registerApp(new HelloApp());
	    	sms.registerApp(new WeatherApp());
	    	sms.start();
	    	sms.readCmd();
    	}else{
    		System.out.println("参数不正确， 请使用 java net.solosky.maplefetion.demo.robot.FetionRobot 机器人手机号 密码");
    	}
    }

}
