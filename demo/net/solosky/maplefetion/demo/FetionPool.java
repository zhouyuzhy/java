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
 * Package  : net.solosky.maplefetion.demo
 * File     : FetionPool.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;

import net.solosky.maplefetion.ClientState;
import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.NotifyEventListener;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.net.mina.MinaTransferFactory;
import net.solosky.maplefetion.util.SharedExecutor;
import net.solosky.maplefetion.util.SharedTimer;

/**
 *
 *  飞信实例池，可以在这个实例池上运行多个飞信客户端，各个客户端运行互不干扰
 *
 * @author solosky <solosky772@qq.com>
 */
public class FetionPool
{
	/**
	 * 飞信实例列表
	 */
	private ArrayList<FetionClient> clientList;
	
	/**
	 * 共享的传输工厂
	 */
	private MinaTransferFactory transferFactory;
	
	/**
	 * 共享的timer
	 */
	private SharedTimer sharedTimer;
	
	/**
	 * 共享的线程池
	 */
	private SharedExecutor shareExecutor;
	
	/**
	 * 读取控制台输入字符
	 */
	private BufferedReader reader;
	
	/**
	 * 写入控制台字符
	 */
	private BufferedWriter writer;
	
	public FetionPool()
	{
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
		this.sharedTimer = new SharedTimer();
		this.shareExecutor = new SharedExecutor();
		this.clientList = new ArrayList<FetionClient>();
		this.transferFactory = new MinaTransferFactory(Executors.newCachedThreadPool());
	}
	
	
	public void mainloop() throws Exception
	{
		this.help();
		while(true) {
    		String line = reader.readLine();
    		if(!this.dispatch(line)) {
    			Iterator<FetionClient> it = this.clientList.iterator();
    			while(it.hasNext()) {
    				FetionClient client = it.next();
    				if(client.getState()==ClientState.ONLINE) {
    					client.logout();
    				}
    			}
    			this.sharedTimer.reallyStopTimer();
    			this.shareExecutor.reallyStopExecutor();
    			break;
    		}
    	}
	}
	
	public void ls()
	{
		println("当前飞信池的客户端：");
		Iterator<FetionClient> it = this.clientList.iterator();
		int i = 0;
		while(it.hasNext()) {
			FetionClient client = it.next();
			println("["+(i++)+"]	"+client.getFetionUser().getDisplayName()+"("+client.getFetionUser().getMobile()+")		" + client.getState().name());
		}
	}
	
	 /**
     * 解析用户输入的命令，并调用不同的程序
     * @throws Exception 
     */
    public boolean dispatch(final String line) throws Exception
    {
    	String[] cmd = line.split(" ");
    	if(cmd[0].equals("exit")) {
			return false;
    	}else if(cmd[0].equals("ls")) {
    		this.ls();
    	}else if(cmd[0].equals("login")) {
    		this.login(cmd[1]);
    	}else if(cmd[0].equals("logout")) {
    		this.logout(cmd[1]);
    	}else if(cmd[0].equals("add")) {
    		if(cmd.length>2)
    		this.add(cmd[1], cmd[2]);
    	}else if(cmd[0].equals("del")) {
    		this.del(cmd[1]);
    	}else if(cmd[0].equals("help")) {
    		this.help();
    	}else {
    		println("未知命令");
    	}
    	
    	return true;
    	
    }
	/**
     * 
     */
    private void help()
    {
    	println("这是一个飞信实例池，里面可以运行多个飞信实例而不互相干扰。");
    	println("并且使用共享的Timer和Executor，可以减少资源占用，提高效率。");
    	println("可用命令如下：");
    	println("ls					查看当前已经添加的客户端");
    	println("add 手机号 密码 	添加客户端到飞信池");
    	println("del 客户端编号 		从飞信池删除客户端");
    	println("login 客户端编号	开始登陆指定的客户端");
    	println("logout 客户端编号	退出指定的客户端");
    	println("exit 				退出飞信池");
    	println("help				帮助信息");
    }


	/**
     * @param string
     */
    private void del(String s)
    {
    	Integer i = Integer.parseInt(s);
	    FetionClient client = this.clientList.get(i);
	    if(client!=null) {
	    	if(client.getState()==ClientState.ONLINE) {
	    		println("正在退出 "+client.getFetionUser().getDisplayName()+"..");
	    		client.logout();
	    	}
	    	this.clientList.remove(i);
	    }
	    
    }


	/**
     * @param string
     * @param string2
     */
    private void add(String mobile, String password)
    {
    	FetionClient client = new FetionClient(mobile, password);
    	FetionPoolListener listener = new FetionPoolListener(client);
    	client.setNotifyEventListener(listener);
    	client.setFetionExecutor(this.shareExecutor);
    	client.setFetionTimer(this.sharedTimer);
    	client.setTransferFactory(this.transferFactory);
    	this.clientList.add(client);
    	println("已经添加 "+mobile +"..");
    }


	/**
     * @param string
     */
    private void logout(String s)
    {
    	Integer i = Integer.parseInt(s);
	    FetionClient client = this.clientList.get(i);
	    if(client!=null && client.getState()==ClientState.ONLINE) {
	    	println("正在退出 "+client.getFetionUser().getDisplayName()+"..");
	    	client.logout();
	    }
    }


	/**
     * @param string
     */
    private void login(String s)
    {
	    Integer i = Integer.parseInt(s);
	    FetionClient client = this.clientList.get(i);
	    if(client!=null && client.getState()!=ClientState.ONLINE) {
	    	println("正在登陆 "+client.getFetionUser().getDisplayName()+"..");
	    	client.login();
	    }
    }


	/**
	 * @param args
	 * @throws Exception 
	 */
	
	public static void main(String[] args) throws Exception
	{
		new FetionPool().mainloop();
	}
	
	
	/**
     * 打印一行字符
     */
    public void println(String s)
    {
    	
    	try {
    		this.writer.append(s);
    		this.writer.append('\n');
	        this.writer.flush();
        } catch (IOException e) {
	        e.printStackTrace();
        }
    }
	
	
	private class FetionPoolListener implements NotifyEventListener
	{
		private FetionClient client;
		public FetionPoolListener(FetionClient client)
		{
			this.client = client;
		}
		/* (non-Javadoc)
		 * @see net.solosky.maplefetion.NotifyEventListener#fireEvent(net.solosky.maplefetion.event.NotifyEvent)
		 */
		@Override
		public void fireEvent(NotifyEvent event)
		{
			println("["+client.getFetionUser().getDisplayName()+"] "+event.toString());
		}
	}
}
