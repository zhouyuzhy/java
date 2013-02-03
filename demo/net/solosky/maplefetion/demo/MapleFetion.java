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
 * Package  : net.solosky.maplefetion
 * File     : FetionDemo.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import net.solosky.maplefetion.ClientState;
import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.NotifyEventAdapter;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.BuddyExtend;
import net.solosky.maplefetion.bean.Cord;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Member;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.client.dialog.DialogException;
import net.solosky.maplefetion.client.dialog.DialogState;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.failure.RequestFailureEvent;
import net.solosky.maplefetion.event.action.success.SendChatMessageSuccessEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;
import net.solosky.maplefetion.net.AutoTransferFactory;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.store.SimpleFetionStore;
import net.solosky.maplefetion.util.SingleExecutor;
import net.solosky.maplefetion.util.StringHelper;
import net.solosky.maplefetion.util.ThreadTimer;
import net.solosky.maplefetion.util.UriHelper;

/**
 * 这个是MapleFetion的演示程序，也提供了一个完整的命令行下的飞信
 * 
 * @author solosky <solosky772@qq.com>
 */
public class MapleFetion extends NotifyEventAdapter
{
	/**
	 * 飞信客户端
	 */
	private FetionClient client;
	
	/**
	 * 读取控制台输入字符
	 */
	private BufferedReader reader;
	
	/**
	 * 写入控制台字符
	 */
	private BufferedWriter writer;
	
	/**
	 * 当前聊天好友
	 */
	private ChatDialogProxy activeChatDialog;
	
	/**
	 * 好友序号到好友飞信地址的映射
	 */
	private Hashtable<String, String> buddymap;
	
	/**
	 * 群序号到群地址的映射
	 */
	private Hashtable<String, String> groupmap;
	
	/**
	 * 是否启动了读取命令线程的标志
	 */
	private boolean isConsoleReadTheadStarted;
	
	/**
	 * 当前需要处理的验证码事件,这里只能处理一个，如果要处理多个，则需建立验证码处理队列
	 */
	private ImageVerifyEvent verifyEvent;
	 
	 /**
	  * 默认构造函数
	  * @param serviceId
	  * @param pass
	  */
	
	public MapleFetion(String serviceId, String pass)
	{
		this.client = new FetionClient(serviceId, pass,
				this, 
				new AutoTransferFactory(),
				new SimpleFetionStore(), 
				new ThreadTimer(),
				new SingleExecutor());
		this.reader = new BufferedReader(new InputStreamReader(System.in));
		this.writer = new BufferedWriter(new OutputStreamWriter(System.out));
		this.buddymap = new Hashtable<String, String>();
		this.groupmap = new Hashtable<String, String>();
		this.isConsoleReadTheadStarted = false;
	}
	
	
	public void login(int presence)
	{
		//this.client.enableGroup(true);
		this.client.login(presence);
	}
	
	
	public static void main(String[] args) throws Exception
	{
		String serviceId    = null;
		String password = null;
		int loginPresence = Presence.ONLINE;
		if(args.length>=2) {
			serviceId   = args[0];
			password = args[1];
			if(args.length>=3)
				loginPresence = "Y".equals(args[3]) ? Presence.HIDEN : Presence.ONLINE;
		}else {
			System.out.println("提示：你可以直接在命令行后加 '手机号/飞信号 密码' 快速登录（不含引号）");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    		System.out.print("请输入手机号/飞信号:");
    		serviceId = reader.readLine();
    		System.out.print("请输入密码:");
    		password = reader.readLine();
    		System.out.print("是否隐身登录(Y/N):");
    		loginPresence = "Y".equals(reader.readLine().toUpperCase()) ? Presence.HIDEN : Presence.ONLINE;
		}
		
		MapleFetion demo = new MapleFetion(serviceId, password);
		demo.welcome();
		demo.login(loginPresence);
	}
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.LoginListener#loginStateChanged(LoginState)
     */
	@Override
    public void loginStateChanged(LoginState state)
    {
	    switch (state)
        {	
        case	SEETING_LOAD_DOING:		//加载自适应配置
        	println("获取自适应系统配置...");
	    	break;
        case	SSI_SIGN_IN_DOING:		//SSI登录
        	println("SSI登录...");
	    	break;
        case	SIPC_REGISTER_DOING:		//注册SIPC服务器
        	println("服务器验证...");
	    	break;
        case	GET_CONTACTS_INFO_DOING:	//获取联系人信息
        	println("获取联系人...");
	    	break;
        case	GET_GROUPS_INFO_DOING:	//获取群消息
        	println("获取群信息...");
	    	break;
        case	GROUPS_REGISTER_DOING:	//注册群
        	println("群登录...");
	    	break;
        	
        //以下是成功信息，不提示
        case	SETTING_LOAD_SUCCESS:
        case	SSI_SIGN_IN_SUCCESS:
        case	SIPC_REGISGER_SUCCESS:
        case	GET_CONTACTS_INFO_SUCCESS:
        case	GET_GROUPS_INFO_SUCCESS:
        case	GROUPS_REGISTER_SUCCESS:
        	break;


        case LOGIN_SUCCESS:
        	println("登录成功");
        	this.loginSuccess();
        	break;
        	
        	
        case SSI_NEED_VERIFY:
        case SSI_VERIFY_FAIL:
        	if(state==LoginState.SSI_NEED_VERIFY)
        		println("需要验证, 请输入目录下的[verify.png]里面的验证码:");
        	else
        		println("验证码验证失败，刷新验证码中...");
	        break;

        case SSI_CONNECT_FAIL:
        	println("SSI连接失败!");
        	break;
        	
        case SIPC_TIMEOUT:
        	println("登陆超时！");
        	break;
        	
        case SSI_AUTH_FAIL:
        	println("用户名或者密码错误!");
        	break;
        	
        case SSI_ACCOUNT_SUSPEND:
        	println("你已经停机，请缴费后再使用飞信。");
        	break;
        	
        default:
        	println("其他状态:"+state.name());
	        break;
        }
	    
    }  
    
	/**
	 * 欢迎信息
	 * @throws Exception 
	 */
	 public void welcome() throws Exception
	{
		println("================================================");
		println("|              "+FetionClient.CLIENT_VERSION +"           |");
		println("|----------------------------------------------|");
		println("| Author:solosky <solosky772@qq.com>           |");
		println("| Home:http://maplefetion.googlecode.com       |");
		println("-----------------------------------------------|");
		println("|这是一个命令行下的飞信，实现了飞信的基本功能。|");
		println("|如果需要帮助，请输入help。欢迎提出BUG和建议。 |");
		println("================================================");
	
	}
	 
	 /**
	  * 输出用户自己信息
	  */
	 public void my()
	 {
		println("--------------------------------------------------"); 
		println("你好，"+client.getFetionUser().getDisplayName()+"! - ["+client.getFetionUser().getImpresa()+"]");
		println("--------------------------------------------------");
	 }
	 
	 /**
	     * 显示帮助信息
	     */
	    public void help()
	    {
	    	println("=========================================");
	    	println("帮助：");
	    	println("=========================================");
	    	println("welcome                    显示欢迎信息");
			println("ls                         显示所有好友列表");
			println("my                         显示我的信息");
	    	println("detail 好友编号            显示好友详细信息");
	    	println("add 手机号码               添加好友（必须是手机号码）");
	    	println("del 好友编号               删除好友");
	    	println("agree 好友编号             同意陌生人添加好友请求");
	    	println("decline 好友编号           拒绝陌生人添加好友请求");
	    	println("to 好友编号 消息内容       给好友发送消息");
	    	println("sms 好友编号 消息内容      给好友发送短信");
	    	println("tel 手机号码 消息内容       通过手机号给好友发送消息（对方必须是好友才行）");
	    	println("enter 好友编号             和好友对话");
	    	println("leave                      离开当前对话");
	    	println("dialog                     显示当前所有会话");
	    	println("group                     显示所有的群列表");
	    	println("say 群编号 消息内容         给群发送消息");
	    	println("nickname 新昵称            修改自己昵称");
	    	println("impresa 个性签名           修改个性签名");
			println("localname 好友编号 新名字  修改好友的显示名字");
			println("cord 好友编号 新组编号     修改好友分组");
			println("newcord 分组标题           创建新的分组");
			println("delcord 分组编号           删除分组");
			println("cordtitle 分组编号 分组标题   修改分组标题");
			println("self 消息内容              给自己发送短信");
			println("presence away/online/busy/hiden   改变自己在线状态");
			println("verify 验证码     验证当前需要验证的操作");
	    	println("exit                       退出登录");
	    	println("help                       帮助信息");
	    	println("=========================================");
	    }
	    
	    /**
	     * 显示所有用户列表
	     */
	    public void list()
	    {
	    	println("\n=================================");
	    	println("所有好友列表");
	    	println("-------------------------------");
	    	println("#ID\t好友昵称\t在线状态\t个性签名");
	    	FetionStore store = this.client.getFetionStore();
	    	Iterator<Cord> it = store.getCordList().iterator();
	    	int id=0;
	    	this.buddymap.clear();
	    	//分组显示好友
	    	while(it.hasNext()) {
	    		Cord cord = it.next();
	    		id = cord(cord.getId(),cord.getTitle(),id, store.getBuddyListByCord(cord));
	    	}
	    	id = cord(-1,"默认分组", id, store.getBuddyListWithoutCord());
	    }
	    
	    /**
	     * 显示一个组的用户
	     */
	    public int cord(int cordId, String name,int startId,Collection<Buddy> buddyList)
	    {
	    	Iterator<Buddy> it = buddyList.iterator();
	    	Buddy buddy = null;
	    	println("\n-------------------------------");
	    	println("【"+cordId+"::"+name+"】");
	    	println("-------------------------------");
	    	if(buddyList.size()==0) {
	    		println("暂无好友。。");
	    	}
			while(it.hasNext()) {
				buddy = it.next();
				this.buddymap.put(Integer.toString(startId), buddy.getUri());
				String impresa = buddy.getImpresa();
				println(Integer.toString(startId)+" "+formatRelation(buddy.getRelation())+" "+fomartString(buddy.getDisplayName(),10)+"\t"
						+buddy.getDisplayPresence()
						+"\t"+(impresa==null?"":impresa));
				startId++;
			}
			return startId;
	    }
	    
	   public void group()
	   {
		   Iterator<Group> it = this.client.getFetionStore().getGroupList().iterator();
		   int groupId = 0;
		   println("===========================群列表=================================");
		   while(it.hasNext()) {
			   Group group = it.next();
			   this.groupmap.put(Integer.toString(groupId), group.getUri());
			   println(groupId+"::"+group.getName()+'\t'+group.getBulletin()+"\t"+ group.getIntro());
			   println("-----------------------------------------------------------------");
			   Iterator<Member> mit = this.client.getFetionStore().getGroupMemberList(group).iterator();
			   while(mit.hasNext()) {
				   Member member = mit.next();
				   println('\t'+member.getDisplayName()+"\t"+member.getUri());
			   }
			   println("-----------------------------------------------------------------");
			   groupId++;
		   }
	   }
	   
	   
	   /**
	    * 处理验证码
	    * @param code
	    */
	   public void verify(String code) {
		   if(this.verifyEvent!=null) {
			   this.verifyEvent.getVerifyImage().setVerifyCode(code);
			   this.client.processVerify(this.verifyEvent);
			   this.verifyEvent = null;
		   }else {
			   println("No verify action to do.");
		   }
	   }
	    
	    /**
	     * 发送消息
	     * @throws Exception 
	     */
	    public void to(String uri,final String message)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		this.send(buddy, message);
	    	}else {
	    		println("找不到这个好友，请检查你的输入！");
	    	}
	    }
	    
	    public void tel(final String tel, String msg)
	    {
	    	long mobile = Long.parseLong(tel);
	    	this.client.sendChatMessage(mobile, new Message(StringHelper.qouteHtmlSpecialChars(msg), Message.TYPE_PLAIN), new ActionEventListener() {
				public void fireEevent(ActionEvent event)
				{
					switch(event.getEventType()){
						
						case SUCCESS:
							SendChatMessageSuccessEvent evt = (SendChatMessageSuccessEvent) event;
							if(evt.isSendToMobile()){
								println("发送成功，消息已通过短信发送到对方手机！");
							}else if(evt.isSendToClient()){
								println("发送成功，消息已通过服务直接发送到对方客户端！");
							}
							break;
							
						case FAILURE:
							FailureEvent evt2 = (FailureEvent) event;
							switch(evt2.getFailureType()){
								case BUDDY_NOT_FOUND:
									println("发送失败, 该用户可能不是你好友，请尝试添加该用户为好友后再发送消息。");
									break;
								case USER_NOT_FOUND:
									println("发送失败, 该用户不是移动用户。");
									break;
								case SIPC_FAIL:
									println("发送失败, 服务器返回了错误的信息。");
									break;
								case UNKNOWN_FAIL:
									println("发送失败, 不知道错在哪里。");
									default:;
							}
							break;
						 
						case SYSTEM_ERROR:
							println("发送失败, 客户端内部错误。");
							break;
						case TIMEOUT:
							println("发送失败, 超时");
							break;
						case TRANSFER_ERROR:
							println("发送失败, 超时");
							
					}
				}
			});
	    	
	    }
	    
	    /**
	     * 发送手机短信消息
	     */
	    public void sms(String uri, final String message)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		this.client.sendSMSMessage(buddy, Message.wrap(message), new ActionEventListener(){
					public void fireEevent(ActionEvent event)
					{
						if(event.getEventType()==ActionEventType.SUCCESS){
							println("提示：发送给"+buddy.getDisplayName()+" 的短信发送成功！");
						}else{
							println("[系统消息]:你发给 "+buddy.getDisplayName()+" 的短信  "+message+" 发送失败！");
						}
					}
				});
	    	}else {
	    		println("找不到这个好友，请检查你的输入！");
	    	}
	    }
	    
	    /**
	     * 给自己发送短信
	     */
	    public void self(String message)
	    {
	    	this.client.sendSMSMessage(this.client.getFetionUser(), Message.wrap(message), new ActionEventListener(){
            	public void fireEevent(ActionEvent event)
				{
					if(event.getEventType()==ActionEventType.SUCCESS){
						println("给自己发送短信成功！");
					}else{
						println("给自己发送短信失败！");
					}
				}
	    	});
	    }
	    
	    /**
	     * 发送群消息
	     */
	    public void say(String groupuri, String message) {
	    	if(groupuri==null)	return;
	    	final Group group = this.client.getFetionStore().getGroup(groupuri);
	    	if(group!=null) {
	    		GroupDialog dialog = this.client.getDialogFactory().findGroupDialog(group);
	    		if(dialog!=null) {
	    			dialog.sendChatMessage(Message.wrap(message), new ActionEventListener() {
	    				public void fireEevent(ActionEvent event)
	    				{
		    				if(event.getEventType()==ActionEventType.SUCCESS){
		    					println("提示：发送给群 "+group.getName()+" 的消息发送成功！");
							}else{
								println("提示：发送给群 "+group.getName()+" 的消息发送失败！");
							}
	    			}});
	    		}
	    	}
	    }
	    
	    /**
	     * 获取好友详细信息
	     */
	    public void detail(String uri)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy==null) {
	    		println("找不到好友，请重新输入好友信息");
	    	}else if(UriHelper.isMobile(buddy.getUri())) {
	    		printBuddyInfo(buddy);
	    	}else {
	    		this.client.retireBuddyInfo(buddy, new ActionEventListener() {
					public void fireEevent(ActionEvent event)
					{
						if(event.getEventType()==ActionEventType.SUCCESS){
							printBuddyInfo(buddy);
						}else{
							println("获取好友信息失败~");
						}
					}
	    		});
	    	}
	    }
	    
	    /**
	     * 打印好友信息
	     */
	    public void printBuddyInfo(Buddy buddy)
	    {
	    	System.out.println("----------------------------------");
    	    System.out.println("好友详细信息");
    	    System.out.println("----------------------------------");
    	    System.out.println("URI:"+buddy.getUri());
    	    System.out.println("昵称:"+buddy.getDisplayName());
    	    System.out.println("所在分组:"+buddy.getCordId());
    	    System.out.println("状态:"+fomartPresence(buddy));
    	    System.out.println("备注:"+buddy.getLocalName());
    	    System.out.println("手机号码:"+buddy.getMobile());
    	    
    	    BuddyExtend extend = buddy.getExtend();
    	    if(extend!=null) {
        	    System.out.println("个性签名:"+buddy.getImpresa());
        	    System.out.println("国家:"+extend.getNation());
        	    System.out.println("省份:"+extend.getProvince());
        	    System.out.println("城市:"+extend.getCity());
        	    System.out.println("EMAIL:"+buddy.getEmail());
    	    }
    	    System.out.println("----------------------------------");
	    }
	    
	    /**
	     * 改变自己昵称
	     * @throws Exception 
	     */
	    public void nickname(String nickName)
	    {
	    	this.client.setNickName(nickName, new ActionEventListener() {
                public void fireEevent(ActionEvent event)
				{
					if(event.getEventType()==ActionEventType.SUCCESS){
						println("更改昵称成功！");
					}else{
						println("更改昵称失败！");
					}
				}
	    	});
	    }
	    
	    /**
	     * 改变自己的个性签名
	     * @throws Exception 
	     */
	    public void impresa(String impresa) throws Exception
	    {
	    	this.client.setImpresa(impresa, new ActionEventListener() {
                public void fireEevent(ActionEvent event)
				{
					if(event.getEventType()==ActionEventType.SUCCESS){
						println("更改个性签名成功！");
					}else{
						println("更改个性签名失败！");
					}
				}
	    	});
	    }
	    
	    /**
	     * 改变好友的显示姓名
	     * @param uri
	     * @param localName
	     */
	    public void localname(String uri, String localName)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		this.client.setBuddyLocalName(buddy, localName, new ActionEventListener() {
	    			 public void fireEevent(ActionEvent event)
	 				{
	 					if(event.getEventType()==ActionEventType.SUCCESS){
	 						println("更改好友显示姓名成功！");
	 					}else{
	 						println("更改好友显示姓名失败！");
	 					}
	 				}
	    		});
	    	}else {
	    		println("找不到这个好友，请检查你的输入！");
	    	}
	    }
	    
	    
	    /**
	     * 设置好友分组
	     */
	    public void cord(String uri, String cordId)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	Collection<Cord> cordList = new ArrayList<Cord>();
	    	//本来一个好友可以分到多个组的，为了简单这里只实现了修改一个分组
	    	int cid = -1;
            try {
	            cid = Integer.parseInt(cordId);
            } catch (NumberFormatException e) {
            	println("分组编号不是数字，请检查后重新输入。");
            	return;
            }
	    	if(cid!=-1) {
	    		Cord cord = this.client.getFetionStore().getCord(cid);
	    		if(cord==null) {
	    			println("分组编号不存在，请检查后重新输入。");
	    			return;
	    		}else {
	    			cordList.add(cord);
	    		}
	    	}
	    	
	    	if(buddy!=null) {
	    		this.client.setBuddyCord(buddy, cordList,  new ActionEventListener() {
	    			 public void fireEevent(ActionEvent event)
		 				{
		 					if(event.getEventType()==ActionEventType.SUCCESS){
		 						println("更改好友分组成功！");
		 					}else{
		 						println("更改好友分组失败！");
		 					}
		 				}
	    		});
	    	}else {
	    		println("找不到这个好友，请检查你的输入！");
	    	}
	    }
	    
	    /**
	     * 创建新的分组
	     * @param title
	     */
	    public void newcord(String title)
	    {
	    	client.createCord(title, new ActionEventListener() {
				public void fireEevent(ActionEvent event)
 				{
 					if(event.getEventType()==ActionEventType.SUCCESS){
 						println("创建新的分组成功！");
 					}else{
 						println("创建新的分组失败！");
 					}
 				}
	    		
	    	});
	    }
	    
	    /**
	     * 设置分组标题
	     * @param cordid
	     * @param title
	     */
	    public void cordtitle(String cordId, String title)
	    {
	    	Cord cord = this.getCord(cordId);
	    	if(cord!=null) {
	    		this.client.setCordTitle(cord, title, new ActionEventListener() {
                    public void fireEevent(ActionEvent event)
     				{
     					if(event.getEventType()==ActionEventType.SUCCESS){
     						println("设置分组标题成功！");
     					}else{
     						println("设置分组标题失败！");
     					}
     				}
	    		});
	    	}
	    }
	    
	    /**
	     * 删除分组
	     * @param cordid
	     */
	    public void delcord(String cordId)
	    {
	    	Cord cord = this.getCord(cordId);
	    	if(cord!=null) {
	    		Collection<Buddy> list = this.client.getFetionStore().getBuddyListByCord(cord);
	    		if(list!=null && list.size()>0) {
	    			println("分组编号 "+cordId+" 中好友不为空，请移除该组的好友后再尝试删除。");
	    			return;
	    		}
	    		this.client.deleteCord(cord, new ActionEventListener() {
                    public void fireEevent(ActionEvent event)
     				{
     					if(event.getEventType()==ActionEventType.SUCCESS){
     						println("删除分组成功！");
     					}else{
     						println("删除分组失败！");
     					}
     				}
	    		});
	    	}
	    }
	    
	    /*
	     * 添加好友
	     * @throws Exception 
	     */
	    public void add(String mobile)
	    {
	    	client.addBuddy(mobile, new ActionEventListener() {
	    	public void fireEevent(ActionEvent event)
 				{
 					if(event.getEventType()==ActionEventType.SUCCESS){
 						println("发出添加好友请求成功！请耐性地等待用户回复。");
 					}else{
 						println("发出添加好友请求失败！"+event.toString());
 					}
 				}
	    		
	    	});
	    }
	    
	    /**
	     * 删除好友
	     */
	    public void del(String uri)
	    {
	    	Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		client.deleteBuddy(buddy, new ActionEventListener() {
	    			public void fireEevent(ActionEvent event)
	 				{
	 					if(event.getEventType()==ActionEventType.SUCCESS){
	    					println("删除好友成功！");
	 					}else{
	 						println("删除好友失败！");
	 					}
	 				}
	    		});
	    	}else {
	    		println("对不起，好友"+uri+"不存在！");
	    	}
	    }
	    
	    /**
	     * 同意对方请求
	     */
	    public void agree(String uri)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		this.client.agreedApplication(buddy,  new ActionEventListener() {
	    			public void fireEevent(ActionEvent event)
	 				{
	 					if(event.getEventType()==ActionEventType.SUCCESS){
	 						println("你已经同意"+buddy.getDisplayName()+"的添加你为好友的请求。");
	 					}else{
	 						println("同意对方请求失败！");
	 					}
	 				}
	    		});
	    	}
	    }
	    
	    
	    /**
	     * 拒绝陌生人添加好友请求
	     * @param uri
	     */
	    public void decline(String uri)
	    {
	    	final Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(buddy!=null) {
	    		this.client.declinedApplication(buddy,  new ActionEventListener() {
	    			public void fireEevent(ActionEvent event)
	 				{
	 					if(event.getEventType()==ActionEventType.SUCCESS){
	 						println("你已经拒绝"+buddy.getDisplayName()+"的添加你为好友的请求。");
	 					}else{
	 						println("拒绝对方请求失败！");
	 					}
	 				}
	    		});
	    	}
	    }
	    
	    /**
	     * 进入对话框
	     */
	    public void enter(String uri)
	    {
	    	Buddy buddy = this.client.getFetionStore().getBuddyByUri(uri);
	    	if(this.activeChatDialog==null) {
                try {
	                this.activeChatDialog = this.client.getChatDialogProxy(buddy);
	                println("提示：你现在可以和 "+ buddy.getDisplayName()+" 聊天了。");
                } catch (DialogException e) {
	              println("创建对话框失败:"+e);
                }
	    	}else {
	    		
	    	}
	    }
	    
	    /**
	     * 离开对话框
	     */
	    public void leave()
	    {
	    	try {
	    		this.client.getChatDialogProxyFactory().close(activeChatDialog);
            } catch (Exception e) {
            	println("关闭对话框失败~"+e.getMessage());
            }
	    }
	    
	    /**
	     * 发送短信
	     * @param dialog
	     * @param message
	     */
	    private void send(final Buddy buddy,final String message)
	    {
	    	ChatDialogProxy proxy = null;
            try {
	            proxy = this.client.getChatDialogProxyFactory().create(buddy);
            } catch (DialogException e) {
	          println("建立对话失败！！");
	          return;
            }
	    	proxy.sendChatMessage( Message.wrap(message), new ActionEventListener(){
				public void fireEevent(ActionEvent event)
 				{
 					if(event.getEventType()==ActionEventType.SUCCESS){
						SendChatMessageSuccessEvent evt = (SendChatMessageSuccessEvent) event;
						if(evt.isSendToMobile()){
							println("发送成功，消息已通过短信发送到对方手机！");
						}else if(evt.isSendToClient()){
							println("发送成功，消息已通过服务直接发送到对方客户端！");
						}
 					}else if(event.getEventType()==ActionEventType.FAILURE){
 						FailureEvent evt2 = (FailureEvent) event;
						switch(evt2.getFailureType()){
							case BUDDY_NOT_FOUND:
								System.out.println("发送失败, 该用户可能不是你好友，请尝试添加该用户为好友后再发送消息。");
								break;
							case USER_NOT_FOUND:
								System.out.println("发送失败, 该用户不是移动用户。");
								break;
							case SIPC_FAIL:
								System.out.println("发送失败, 服务器返回了错误的信息。");
								break;
							case UNKNOWN_FAIL:
								System.out.println("发送失败, 不知道错在哪里。");
								
							case REQEUST_FAIL:
								RequestFailureEvent evt3 = (RequestFailureEvent) event; 
								System.out.println("提示:"+evt3.getReason()+", 更多信息请访问:"+evt3.getReffer());
						
							default:
								println("发送消息失败！"+event.toString());
						}
 					}else{
 						println("发送消息失败！"+event.toString());
 					}
 				}
			});
	    }
	    
	    /**
	     * 设置登录用户的状态
	     * @param presence
	     * @throws Exception 
	     */
	    public void presence(String presence) throws Exception
	    {
	    	int to = -1; 
	    	if(presence.equals("away")) {
	    		to = Presence.AWAY;
	    	}else if(presence.endsWith("online")) {
	    		to = Presence.ONLINE;
	    	}else if(presence.equals("busy")) {
	    		to = Presence.BUSY;
	    	}else if(presence.equals("hiden")) {
	    		to = Presence.HIDEN;
	    	}else {
	    		println("未知状态:"+presence);
	    	}
	    	if(to!=-1) {
	    			this.client.setPresence(to, new ActionEventListener() {
	    			public void fireEevent(ActionEvent event)
	 				{
	 					if(event.getEventType()==ActionEventType.SUCCESS){
	 						println("改变状态成功！");
	 					}else{
	 						println("改变状态失败！");
	 					}
	 				}
	    		});
	    	}
	    }
	    
	    /**
	     * 退出程序
	     * @throws Exception 
	     */
	    public void exit() throws Exception
	    {
	    	this.client.logout();
	    	println("你已经成功的退出！");
	    }
	    
	    /**
	     * 格式化状态
	     * @param pre
	     * @return
	     */
	    public String fomartPresence(Buddy buddy)
		{
	    	return buddy.getDisplayPresence();
		}
	    
	    /**
	     * 格式化字符串
	     */
	    public String fomartString(String str, int len)
	    {
	    	if(str!=null) {
	    		if(str.length()>len)
	    			return str.substring(0,len)+".";
	    		else
	    			return str;
	    	}else {
	    		return "";
	    	}
	    }
	    
	    /**
	     * 格式化关系
	     */
	    public String formatRelation(Relation relation)
	    {
	    	switch(relation) {
	    	case BUDDY: return "B";
	    	case UNCONFIRMED: return "W";
	    	case DECLINED: return "X";
	    	case STRANGER: return "？";
	    	case BANNED: return "@";
	    	default: return "-";
	    	}
	    	
	    }
	    
	    /**
	     * 登录成功之后，启动主循环
	     */
	    public void loginSuccess()
	    {
	    	if(this.isConsoleReadTheadStarted)
	    		return;

	    	Runnable r = new Runnable() {
	    		public void run()
	    		{
	    			try {
	    				welcome();
		    			my();
		    			list();
	                    mainloop();
                    } catch (Exception e) {
	                    println("程序运行出错");
                    	e.printStackTrace();
                    	if(client.getState()==ClientState.ONLINE) {
                    		client.logout();
                    	}
                    }
	    		}
	    	};
	    	
	    	Thread t = new Thread(r);
	    	t.setName("MapleFetionConsoleReaderThead");
	    	t.start();
	    	this.isConsoleReadTheadStarted = true;
	    }
	 
    /**
     * 主循环
     * @throws Exception 
     */
    public void mainloop() throws Exception
    {
    	String line = null;
    	this.prompt();
    	while(true) {
    		line = reader.readLine();
    		if(!this.dispatch(line)) {
    			break;
    		}
    		this.prompt();
    	}
    }
    
    
    /**
     * 解析用户输入的命令，并调用不同的程序
     * @throws Exception 
     */
    public boolean dispatch(final String line) throws Exception
    {
    	String[] cmd = line.split(" ");
    	if(cmd[0].equals("welcome")) {
			this.welcome();
		}else if(cmd[0].equals("ls")) {
			this.list();
		}else if(cmd[0].equals("my")) {
			this.my();
		}else if(cmd[0].equals("group")) {
			this.group();
		}else if(cmd[0].equals("exit")) {
			this.exit();
			return false;
		}else if(cmd[0].equals("detail")) {
			if(cmd.length>=2)
				this.detail(this.buddymap.get(cmd[1]));
		}else if(cmd[0].equals("enter")) {
			if(cmd.length>=2)
				this.enter(this.buddymap.get(cmd[1]));
		}else if(cmd[0].equals("leave")) {
			this.leave();
		}else if(cmd[0].equals("to")) {
			if(cmd.length>=3)
				this.to(this.buddymap.get(cmd[1]),line.substring(line.indexOf(cmd[2])));
		}else if(cmd[0].equals("say")) {
			if(cmd.length>=3)
				this.say(this.groupmap.get(cmd[1]),line.substring(line.indexOf(cmd[2])));
		}else if(cmd[0].equals("sms")) {
			if(cmd.length>=3)
				this.sms(this.buddymap.get(cmd[1]),line.substring(line.indexOf(cmd[2])));
		}else if(cmd[0].equals("tel")) {
			if(cmd.length>=3)
				this.tel(cmd[1], cmd[2]);
		}else if(cmd[0].equals("nickname")) {
			if(cmd.length>=2)
				this.nickname(cmd[1]);
		}else if(cmd[0].equals("impresa")) {
			if(cmd.length>=2)
				this.impresa(cmd[1]);
		}else if(cmd[0].equals("del")) {
			if(cmd.length>=2)
				this.del(this.buddymap.get(cmd[1]));
		}else if(cmd[0].equals("newcord")) {
			if(cmd.length>=2)
				this.newcord(cmd[1]);
		}else if(cmd[0].equals("delcord")) {
			if(cmd.length>=2)
				this.delcord(cmd[1]);
		}else if(cmd[0].equals("cordtitle")) {
			if(cmd.length>=3)
				this.cordtitle(cmd[1], cmd[2]);
		}else if(cmd[0].equals("add")) {
			if(cmd.length>=2)
				this.add(cmd[1]);
		}else if(cmd[0].equals("agree")) {
			if(cmd.length>=2)
				this.agree(this.buddymap.get(cmd[1]));
		}else if(cmd[0].equals("decline")) {
			if(cmd.length>=2)
				this.decline(this.buddymap.get(cmd[1]));
		}else if(cmd[0].equals("self")) {
			if(cmd.length>=2)
				this.self(line.substring(line.indexOf(cmd[1])));
		}else if(cmd[0].equals("localname")) {
			if(cmd.length>=3)
				this.localname(this.buddymap.get(cmd[1]), cmd[2]);
		}else if(cmd[0].equals("cord")) {
			if(cmd.length>=3)
				this.cord(this.buddymap.get(cmd[1]), cmd[2]);
		}else if(cmd[0].equals("presence")) {
			if(cmd.length>=2)
				this.presence(cmd[1]);
		}else if(cmd[0].equals("verify")) {
			if(cmd.length>=2)
				this.verify(cmd[1]);
		}else if(cmd[0].equals("help")) {
			this.help();
		}
		else {
			if( line!=null && line.length()>0 ){
				if(this.activeChatDialog!=null) {
					this.activeChatDialog.sendChatMessage(Message.wrap(line), new ActionEventListener(){
						public void fireEevent(ActionEvent event)
		 				{
		 					if(event.getEventType()==ActionEventType.SUCCESS){
								SendChatMessageSuccessEvent evt = (SendChatMessageSuccessEvent) event;
								if(evt.isSendToMobile()){
									System.out.println("发送成功，消息已通过短信发送到对方手机！");
								}else if(evt.isSendToClient()){
									System.out.println("发送成功，消息已通过服务直接发送到对方客户端！");
								}
		 					}else{
		 						println("发送消息失败！");
		 					}
		 				}
					});
				}else{
					println("未知命令："+cmd[0]+"，请检查后再输入。如需帮助请输入help。");
				}

			}
		}
		return true;
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
    
    /**
     * 输入提示符
     */
    public void prompt()
    {
    	try {
    	if(this.activeChatDialog!=null){
    		DialogState state = this.activeChatDialog.getState();
    		if(state==DialogState.CREATED|| state==DialogState.OPENNING|| state==DialogState.OPENED){
    			writer.append(this.client.getFetionUser().getDisplayName()+"@maplefetion^["+this.activeChatDialog.getMainBuddy().getDisplayName()+"]>>");
    		}else{
    			writer.append(this.client.getFetionUser().getDisplayName()+"@maplefetion>>");
    		}
    	}else{
			writer.append(this.client.getFetionUser().getDisplayName()+"@maplefetion>>");
    	}
    	
    	writer.flush();
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    }
    
    
    public void dispose()
    {
    }
    
    /**
     * 保存验证图片
     * @param img
     */
    private void saveImage(byte[] img)
    {
    	try {
    		FileOutputStream out = new FileOutputStream(new File("verify.png"));
        	out.write(img);
        	out.close();
        } catch (Exception e) {
        }
    }
    
    private Cord getCord(String cordId)
    {
    	int cid = -1;
        try {
            cid = Integer.parseInt(cordId);
        } catch (NumberFormatException e) {
        	println("分组编号不是数字，请检查后重新输入。");
        	return null;
        }
		Cord cord = this.client.getFetionStore().getCord(cid);
		if(cord==null) {
			println("分组编号不存在，请检查后重新输入。");
			return null;
		}else {
			return cord;
		}
    }

    
    private String readLine()
    {
    	try {
	        return this.reader.readLine();
        } catch (IOException e) {
	        e.printStackTrace();
        }
        return null;
    }
    
    /**
	 * 接受到系统消息
	 */
    @Override
    public void systemMessageRecived(String m)
    {
    	println("[系统消息]:"+m);
    	prompt();
    }
    


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#buddyApplication(net.solosky.maplefetion.bean.Buddy, java.lang.String)
     */
    @Override
    public void buddyApplication(Buddy buddy, String desc)
    {
    	println("[好友请求]:"+desc+" 想加你为好友。请输入 【agree/decline 好友编号】 同意/拒绝添加请求。");
    	prompt();
	    
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#buddyConfirmed(net.solosky.maplefetion.bean.Buddy, boolean)
     */
    @Override
    public void buddyConfirmed(Buddy buddy, boolean isAgreed)
    {
    	if(isAgreed)
    		println("[系统通知]:"+buddy.getDisplayName()+" 同意了你的好友请求。");
    	else 
    		println("[系统通知]:"+buddy.getDisplayName()+" 拒绝了你的好友请求。");
    	
    	prompt();
	    
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#buddyMessageRecived(net.solosky.maplefetion.bean.Buddy, java.lang.String, net.solosky.maplefetion.client.dialog.ChatDialog)
     */
    @Override
    public void buddyMessageRecived(Buddy from, Message message, ChatDialogProxy dialog)
    {
    	if(from.getRelation()==Relation.BUDDY)
    		println("[好友消息]"+from.getDisplayName()+" 说:"+message.getText());
    	else 
    		println("[陌生人消息]"+from.getDisplayName()+" 说:"+message.getText());
    	prompt();
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#presenceChanged(net.solosky.maplefetion.bean.Buddy)
     */
    @Override
    public void buddyPresenceChanged(Buddy b)
    {
    	if(b.getPresence().getValue()==Presence.ONLINE) {
    		println("[系统通知]:"+b.getDisplayName()+" 上线了。");
        	prompt();
    	}else if(b.getPresence().getValue()==Presence.OFFLINE){
    		println("[系统通知]:"+b.getDisplayName()+" 下线了。");
        	prompt();
    	}
	    
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#groupMessageRecived(net.solosky.maplefetion.bean.Group, net.solosky.maplefetion.bean.Member, java.lang.String, net.solosky.maplefetion.client.dialog.GroupDialog)
     */
    @Override
    public void groupMessageRecived(Group group, Member from, Message message, GroupDialog dialog)
    {
	    println("[群消息] 群 "+group.getName()+" 里的 "+from.getDisplayName()+" 说："+message.getText());
	    prompt();
    }


	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyListener#statusChanged(ClientState)
     */
    @Override
    public void clientStateChanged(ClientState state)
    {
	    switch (state)
        {
        case OTHER_LOGIN:
        	println("你已经从其他客户端登录。");
        	println("30秒之后重新登录..");
//        	//新建一个线程等待登录，不能在这个回调函数里做同步操作
        	new Thread(new Runnable() {
        		public void run() {
        			try {
	                    Thread.sleep(30000);
                    } catch (InterruptedException e) {
                    	System.out.println("重新登录等待过程被中断");
                    }
                    client.login();
        		}
        	}).start();
	        break;
        case CONNECTION_ERROR:
        	println("客户端连接异常");
	        break;
        case DISCONNECTED:
        	println("服务器关闭了连接");
        	break;
        case LOGOUT:
        	println("已经退出。。");
        	break;
        case ONLINE:
        	println("当前是在线状态。");
        	break;
        	
        default:
	        break;
        }
    }

    

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.NotifyEventAdapter#imageVerify(net.solosky.maplefetion.bean.VerifyImage, java.lang.String, java.lang.String)
     */
    @Override
    protected void imageVerify(final VerifyImage verifyImage, final String verifyReason,
            final String verifyTips, final ImageVerifyEvent event)
    {
    	
    	saveImage(verifyImage.getImageData());
    	
    	if(client.getState()==ClientState.LOGGING) {
			System.out.print("当前登录过程需要验证，原因【"+verifyReason+"】,请输入当前目录下图片[verify.jpg]中的验证码：");
			String line = this.readLine();
			verifyImage.setVerifyCode(line);
			client.processVerify(event);
    	}else {
    		println("当前操作需要验证,原因:【"+verifyReason+"】。\n请使用verify命令输入当前目录下图片[verify.jpg]中的验证码(如verify 123abc).");
    		this.verifyEvent = event;
    		prompt();
    	}
    }


	@Override
	protected void inviteReceived(ChatDialogProxy dialog) {
	}
    
    
}
