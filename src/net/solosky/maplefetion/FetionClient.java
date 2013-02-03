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
 * File     : FetionClient.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-10
 * License  : Apache License 2.0 
 */
/**
 * 
 */
/**
 * 
 */
package net.solosky.maplefetion;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Cord;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.GetScheduleListWork;
import net.solosky.maplefetion.client.HttpApplication;
import net.solosky.maplefetion.client.LoginException;
import net.solosky.maplefetion.client.LoginWork;
import net.solosky.maplefetion.client.RegistrationException;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dialog.ChatDialogProxy;
import net.solosky.maplefetion.client.dialog.ChatDialogProxyFactory;
import net.solosky.maplefetion.client.dialog.DialogException;
import net.solosky.maplefetion.client.dialog.DialogFactory;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.client.dialog.ServerDialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.event.action.FutureActionEventListener;
import net.solosky.maplefetion.event.action.SystemErrorEvent;
import net.solosky.maplefetion.event.action.success.FindBuddySuccessEvent;
import net.solosky.maplefetion.event.notify.ClientStateEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;
import net.solosky.maplefetion.net.AutoTransferFactory;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.TransferFactory;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcMessage;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.store.SimpleFetionStore;
import net.solosky.maplefetion.util.AccountValidator;
import net.solosky.maplefetion.util.CrushBuilder;
import net.solosky.maplefetion.util.FetionExecutor;
import net.solosky.maplefetion.util.FetionTimer;
import net.solosky.maplefetion.util.LocaleSetting;
import net.solosky.maplefetion.util.ParseException;
import net.solosky.maplefetion.util.SingleExecutor;
import net.solosky.maplefetion.util.ThreadTimer;

import org.apache.log4j.Logger;

/**
 *
 * 飞信主客户端
 *
 * @author solosky <solosky772@qq.com>
 */
public class FetionClient implements FetionContext
{
	/**
	 * MapleFetion版本
	 */
	public static final String CLIENT_VERSION = "MapleFetion-2.5.1";
	
	/**
	 * 协议版本
	 */
	public static final String PROTOCOL_VERSION = "4.1.0740";
	
	/**
	 * SIPC版本
	 */
	public static final String SIPC_VERSION = SipcMessage.SIP_VERSION;
	/**
	 * 传输工厂
	 */
	private TransferFactory transferFactory;
	
	/**
	 * 对话框工厂
	 */
	private DialogFactory dialogFactory;
	
	/**
	 * 线程执行池
	 */
	private FetionExecutor executor;
	
	/**
	 * 飞信用户
	 */
	private User user;
	
	/**
	 * 飞信存储对象
	 */
	private FetionStore store;
	
	/**
	 * 全局定时器
	 */
	private FetionTimer timer;
	
	/**
	 * 登录过程
	 */
	private LoginWork loginWork;
	
	/**
	 * 通知监听器
	 */
	private NotifyEventListener notifyEventListener;
	
	/**
	 * 客户端状态，尽量使用简单的同步方法
	 */
	private volatile ClientState state;
	
	/**
	 * 聊天对话代理工厂
	 */
	private ChatDialogProxyFactory proxyFactory;
	
	/**
	 * 区域化配置
	 */
	private LocaleSetting localeSetting;
	
	 /**
	 * 日志记录
	 */
	private static Logger logger = Logger.getLogger(FetionClient.class);
	
	
	/**
	 * 详细的构造函数
	 * @param account			手机号/飞信号/Email
	 * @param password			用户密码
	 * @param notifyListener	通知监听器
	 * @param transferFactory	传输工厂
	 * @param fetionStore		分析存储对象
	 * @param loginListener		登录监听器
	 */
	public FetionClient(String account,
						String password, 
						NotifyEventListener notifyEventListener,
						TransferFactory transferFactory,
						FetionStore fetionStore,
						FetionTimer timer,
						FetionExecutor executor)
	{
		this(new User(account, password, "fetion.com.cn"),
				notifyEventListener,
				transferFactory,
				fetionStore, 
				timer,
				executor);
	}
	
	
	/**
	 * 使用默认的传输模式和存储模式构造
	 * @param account			手机号/飞信号/Email
	 * @param password			密码
	 * @param notifyListener	通知监听器
	 */
	public FetionClient(String account, 
						String password, 
						NotifyEventListener notifyEventListener)
	{
		this(new User(account, password, "fetion.com.cn"), 
				notifyEventListener, 
				new AutoTransferFactory(),
				new SimpleFetionStore(), 
				new ThreadTimer(),
				new SingleExecutor());
	}
	
	/**
	 * 简单的构造函数
	 * @param account			手机号/飞信号/Email
	 * @param password	用户密码
	 */
	public FetionClient(String account, String password)
	{
		this(account, password, null);
	}
	
	
	/**
	 * 完整的构造函数
	 * @param user				登录用户，每一个客户端都只有一个唯一的登录用户对象
	 * @param notifyListener	通知监听器，需实现NotifyEventListener接口，用户设置的通知监听器，处理客户端主动发起的事件
	 * @param transferFactory	连接工厂，需实现TransferFactory接口，用于创建连接对象
	 * @param fetionStore		飞信存储对象，需实现FetionStore接口，存储了飞信好友列表等信息
	 * @param fetionTimer		飞信定时器，需实现FetionTimer接口，用于完成不同的定时任务
	 * @param fetionExecutor	飞信执行器，需实现FetionExecutor接口，可以在另外一个单独的线程中执行任务
	 */
    public FetionClient(User user, 
    					NotifyEventListener notifyEventListener, 
    					TransferFactory transferFactory,
    					FetionStore fetionStore, 
    					FetionTimer fetionTimer,
						FetionExecutor fetionExecutor)
    {
    	FetionConfig.init();
    	
    	this.state           = ClientState.LOGOUT;
    	this.user            = user;
    	this.transferFactory = transferFactory;
    	this.store           = fetionStore;
		this.proxyFactory    = new ChatDialogProxyFactory(this);
		this.timer           = fetionTimer;
		this.executor        = fetionExecutor;
		this.notifyEventListener  = notifyEventListener;
		this.localeSetting   = new LocaleSetting();
    }
	
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getDialogFactory()
     */
	public DialogFactory getDialogFactory()
	{
		return this.dialogFactory;
	}
	

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.FetionContext#getChatDialogProxyFactoy()
	 */
	@Override
	public ChatDialogProxyFactory getChatDialogProxyFactoy()
	{
		return this.proxyFactory;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getTransferFactory()
     */
	public TransferFactory getTransferFactory()
	{
		return this.transferFactory;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getSingleExecutor()
     */
	public FetionExecutor getFetionExecutor()
	{
		return this.executor;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getGlobalTimer()
     */
	public FetionTimer getFetionTimer()
	{
		return this.timer;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getFetionUser()
     */
	public User getFetionUser()
	{
		return this.user;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getFetionStore()
     */
	public FetionStore getFetionStore()
	{
		return this.store;
	}
	
    /* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getNotifyListener()
     */
    public NotifyEventListener getNotifyEventListener()
    {
    	return this.notifyEventListener;
    }
    

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.FetionContext#getLocaleSetting()
	 */
	@Override
	public LocaleSetting getLocaleSetting()
	{
		return this.localeSetting;
	}
    
    /**
     * 设置通知监听器
     * @param notifyListener
     */
	public void setNotifyEventListener(NotifyEventListener notifyEventListener)
	{
		this.notifyEventListener = notifyEventListener;
	}
	
	/**
	 * 设置是否启用群
	 * @param enabled
	 */
	public void enableGroup(boolean enabled)
	{
		FetionConfig.setBoolean("fetion.group.enable", enabled);
	}
	
	/**
	 * 设置飞信执行池
     * @param executor the executor to set
     */
    public void setFetionExecutor(FetionExecutor executor)
    {
    	this.executor = executor;
    }

	/**
	 * 设置飞信定时器
     * @param timer the timer to set
     */
    public void setFetionTimer(FetionTimer timer)
    {
    	this.timer = timer;
    }
    
	/**
	 * 设置传输工厂
	 * @param transferFactory the transferFactory to set
	 */
	public void setTransferFactory(TransferFactory transferFactory)
	{
		this.transferFactory = transferFactory;
	}

	
	/**
	 * 设置飞信存储对象
	 * @param store the store to set
	 */
	public void setFetionStore(FetionStore store)
	{
		this.store = store;
	}


	/**
     * @return the proxyFactory
     */
    public ChatDialogProxyFactory getChatDialogProxyFactory()
    {
    	return proxyFactory;
    }
    


	@Override
	public String toString() {
		return "FetionClient [user=" + user + ", state=" + state + "]";
	}


	/**
     * 初始化
     */
    private void init()
    {
    	this.timer.startTimer();
    	this.executor.startExecutor();
    	this.transferFactory.openFactory();
    	
    	this.dialogFactory   = new DialogFactory(this);
    	this.loginWork       = new LoginWork(this, Presence.ONLINE);
    	
    	this.transferFactory.setFetionContext(this);
    	
		this.store.init(user);
    }
    
    
    /**
     * 系统退出或者发生异常后释放系统资源
     */
    private void dispose()
    {
         this.transferFactory.closeFactory();
         this.dialogFactory.closeFactory();
         this.executor.stopExecutor();
         this.loginWork.dispose();
         this.timer.stopTimer();
    }
    
    /**
     * 在操作之前检测客户端是否在线，如果不在线就抛出异常
     */
    private void ensureOnline()
    {
    	if(this.state!=ClientState.ONLINE){
    		throw new IllegalStateException("client is not online [state="+this.state+"], action failed.");
    	}
    }
    
    /**
     * 在进行Http操作时，确保SSi已经登录
     */
    private void ensureSSISigned()
    {
    	if(this.getFetionUser().getSsiCredential()==null) {
    			throw new IllegalStateException("Empty ssic, please enable ssi login first.");
    	}
    }

    /**
     * 更新客户端状态 
     * 这个方法是不同步的，因为属性state是volatile的
     */
    public void updateState(ClientState state)
    {
    	this.state = state;
    	if(this.notifyEventListener!=null)
    		this.notifyEventListener.fireEvent(new ClientStateEvent(state));
    }
    
    /* (non-Javadoc)
     * @see net.solosky.maplefetion.FetionContext#getStatus()
     */
    public ClientState getState()
    {
    	return this.state;
    }
    
    
    /**
     * 进行图片验证操作
     * 一般的操作流程是在收到ImageVerifyEvent事件后，调用者提示用户识别后，
     * 将用户识别的验证码注入verifyImage.setVerifyCode()， 之后调用processVerify继续操作
     * @param event
     */
    public void processVerify(ImageVerifyEvent event) 
    {
    	if(event.getVerifyAction()==ImageVerifyEvent.SSI_VERIFY) {
    		this.loginWork.setVerifyImage(event.getVerifyImage());
    	}else if(event.getVerifyAction()==ImageVerifyEvent.SIPC_VERIFY) {
    		
    		SipcRequest request = event.getTargetRequest();
    		VerifyImage image   = event.getVerifyImage();
    		
    		//移除之前的A字段
    		Iterator<SipcHeader> it = request.getHeaders().iterator();
    		while(it.hasNext()) {
    			SipcHeader h = it.next();
    			if(h.getName().equals(SipcHeader.AUTHORIZATION) && h.getValue().indexOf("Verify response=")!=-1) {
    				it.remove();
    			}
    		}
    		
    		request.addHeader(SipcHeader.AUTHORIZATION, "Verify response=\""+image.getVerifyCode()+"\",algorithm=\""+image.getAlgorithm()+"\"," +
    				"type=\""+image.getVerifyType()+"\",chid=\""+image.getImageId()+"\"");
    		request.resetReplyTimes();
    		event.getTargetDialog().process(request);
    	}else {
    		throw new IllegalArgumentException("Invalid verify action:"+event.getVerifyAction());
    	}
    }
    
    
    /**
     * 取消验证操作，如果是在登录阶段取消验证操作，就退出登录过程
     * 如果是在其他操作需要验证操作，就取消当前操作，当前操作将会获得一个FailureType.VERIFY_CANCELED事件
     * @param event
     */
    public void cancelVerify(ImageVerifyEvent event) 
    {
    	if(this.state==ClientState.LOGGING) {
    		this.cancelLogin();
    	}else {
    		ActionEventListener listener = event.getTargetListener();
    		if(listener!=null) {
    			listener.fireEevent(new FailureEvent(FailureType.VERIFY_CANCELED));
    		}
    	}
    }
    
    /**
     * 处理客户端异常
     * 通常交给客户端处理的异常都是致命的，也就是说如果调用了这个方法客户端都会主动退出
     */
    public void handleException(FetionException exception)
    {
    	
    	//根据不同的异常类型，来设置客户端的状态
    	if(exception instanceof TransferException) {				//网络错误
    		logger.fatal("Connection error. Please try to login again after several time.");
    		this.state = ClientState.CONNECTION_ERROR;
    	}else if(exception instanceof RegistrationException) {	//注册异常
    		RegistrationException re = (RegistrationException) exception;
    		if(re.getRegistrationType()==RegistrationException.DEREGISTERED) {
    			logger.fatal("You have logined by other client.");
    			this.state = ClientState.OTHER_LOGIN;		//用户其他地方登陆
    		}else if(re.getRegistrationType()==RegistrationException.DISCONNECTED) {
    			logger.fatal("Server closed connecction. Please try to login again after several time.");
    			this.state = ClientState.DISCONNECTED;		//服务器关闭了连接
    		}else {
    			logger.fatal("Unknown registration exception", exception);
    		}
		}else if(exception instanceof SystemException){		//系统错误,为了保证系统的稳定性，这里只是做个记录，并不退出客户端
			logger.fatal("System error, just log it and ignore it. Not exit the client for stablity reason.", exception);
			CrushBuilder.handleCrushReport(exception, ((SystemException) exception).getArgs());		//生成错误报告
			return ;		//返回，不退出客户端
    	}else if(exception instanceof LoginException){		//登录错误
    		logger.fatal("Login error. state="+((LoginException)exception).getState().name());
    		this.state = ClientState.LOGIN_ERROR;
    	}else {
    		logger.fatal("Unknown error, just log it and ignore it. Not exit the client for stablity reason.", exception);
			CrushBuilder.handleCrushReport(exception);			//生成错误报告
			return ;
    	}

    	//尝试关闭所有的对话框，对话框应该判断当前客户端状态然后决定是否进行某些操作
    	try {
	        this.dialogFactory.closeAllDialog();
        } catch (FetionException e) {
        	logger.warn("Close All Dialog error.", e);
        }
        
        //最后才能释放系统资源，因为关闭对话可以使用到了系统资源
    	this.dispose();
    	
    	//上面只是更新了客户端状态，还没有回调状态改变函数，为了防止用户在回调函数里面做一些
    	//可能会影响客户端状态的操作，如马上登陆，所以把回调用户函数放在最后面来完成
    	if(this.notifyEventListener!=null)
    		this.notifyEventListener.fireEvent(new ClientStateEvent(state));
    }

    
    /**
     * 重新获取验证图片
     * @param event 图片验证事件，如果获取成功，将会把获取的验证码图片注入到事件中
     * @throws IOException 
     * @throws ParseException 
     */
    public void flushVerifyImage(ImageVerifyEvent event) throws ParseException, IOException
    {
    	VerifyImage image = HttpApplication.fetchVerifyImage(getFetionUser(), 
    			getLocaleSetting(), event.getVerifyImage().getAlgorithm(), 
    			event.getVerifyImage().getVerifyType());
    	event.setVerifyImage(image);
    }
    
    /**
     * 返回服务器对话
     * @return
     */
    public ServerDialog getServerDialog()
    {
    	return this.dialogFactory.getServerDialog();
    }
    
    /**
     * 返回群对话框
     * @param group		对应的群对象
     * @return
     */
    public GroupDialog getGroupDialog(Group group)
    {
    	return this.dialogFactory.findGroupDialog(group);
    }
    
    /**
     * 返回聊天对话代理
     * @param buddy		对应的好友
     * @return		如果找到，返回聊天对话代理
     * @throws DialogException 如果对话框建立失败则抛出
     */
    public ChatDialogProxy getChatDialogProxy(Buddy buddy) throws DialogException
    {
        return this.proxyFactory.create(buddy);
    }

	/**
     * 退出登录
     * 注意这个方法是同步的
     */
    public void logout()
    {
    	if(this.state==ClientState.ONLINE) {
    		try {
                dialogFactory.closeAllDialog();
                dialogFactory.closeFactory();
                transferFactory.closeFactory();
                loginWork.dispose();
                timer.stopTimer();
                executor.stopExecutor();
            	updateState(ClientState.LOGOUT);
            }catch(Throwable t) {
            	logger.warn("logout error ", t);
            }
    	}
    }



	/**
	 * 客户端登录
	 * 这是个异步操作，会把登录的操作封装在单线程池里去执行，登录结果应该通过NotifyEventListener异步通知结果
	 * @param presence 		在线状态 定义在Presence中 
	 * @param verifyImage	验证图片，如果没有可以设置为null
	 * @param isSSISign		是否启用SSI登录
	 */
	public void login(int presence)
	{
		//为了防止用户在客户端登录过程中或者在线情况下错误的调用，检查下当前客户端状态，如果为登录中或者在线状态就抛出异常
		ClientState state = this.getState();
		if(state==ClientState.LOGGING || state==ClientState.ONLINE){
			throw new IllegalStateException("Client is "+state.toString()+", could not login again.");
		}
		
		//为了便于掉线后可以重新登录，把初始化对象的工作放在登录函数做
		this.init();
		this.loginWork.setPresence(presence);
		this.executor.submitTask(this.loginWork);
	}
	
	
	/**
	 *  客户端异步登陆登录
	 *  登陆结果在NotifyEventListener监听LoginStateEvent事件
     */
    public void login()
    {
		this.login(Presence.ONLINE);
    }
	
	/**
	 * 客户端同步登录
	 * @param presence 		在线状态 定义在Presence中 
	 * @return 登录结果,定义在LoginState中
	 */
	public LoginState syncLogin(int presence, long timeout)
	{
		this.login(presence);
		LoginState state = this.loginWork.waitLoginState(timeout);
		if(state==LoginState.LOGIN_TIMEOUT) {
			this.dispose();
		}
		return state;
	}
	
	/**
	 * 客户端同步登录
	 * 不设置超时时间，超时由客户端控制
	 * @return 登录结果,定义在LoginState中
	 */
	public LoginState syncLogin()
	{
		return this.syncLogin(Presence.ONLINE, 0 );
	}
	
	
	/**
	 * 取消当前登录操作，并释放资源
	 * 只能在登录的过程调用，否则会抛出IllegalStateException
	 */
	public void cancelLogin() 
	{
		if(this.state==ClientState.LOGGING) {
    		this.loginWork.cancelLogin();
    		try {
	            dialogFactory.closeAllDialog();
            } catch (Exception e) {
            	logger.warn("close dialog failed.", e);
            }
    		this.dispose();
		}else {
			throw new IllegalStateException("client is not in logging state(state="+state.toString()+"). if client is online, please call FetionClient.logout().");
		}
	}
	
	/////////////////////////////////////////////用户操作开始/////////////////////////////////////////////
	
	/**
	 * 发送聊天消息，如果好友不在线将会发送到手机
	 * @param toBuddy  发送聊天消息的好友，关系只能是好友或者陌生人才能发送消息，否则会得到FailureType.BUDDY_RELATION_FORBIDDEN的错误
	 * @param message  需发送的消息
	 * @param listener 操作结果监听器
	 */
	public void sendChatMessage(final Buddy toBuddy, final Message message, ActionEventListener listener)
	{
		this.ensureOnline();
		Relation relation = toBuddy.getRelation();
		if( relation==Relation.BANNED||
			relation==Relation.DECLINED||
			relation==Relation.UNCONFIRMED) {
			if(listener!=null) listener.fireEevent(new FailureEvent(FailureType.BUDDY_RELATION_FORBIDDEN));
		}else {
    		try {
    			ChatDialogProxy proxy = this.proxyFactory.create(toBuddy);
    			proxy.sendChatMessage(message, listener);
    		} catch (DialogException e) {
    			listener.fireEevent(new SystemErrorEvent(e));
    		}
		}
	}
	
	/**
	 * 发送聊天消息，如果好友不在线将会发送到手机
	 * @param toBuddy  发送聊天消息的好友
	 * @param message  需发送的消息
	 * @return 操作结果等待对象， 可以在这个对象上调用waitStatus()等待操作结果
	 */
	public ActionEventFuture sendChatMessage(Buddy toBuddy, Message message)
	{
		this.ensureOnline();
		ActionEventFuture future = new ActionEventFuture();
		this.sendChatMessage(toBuddy, message, new FutureActionEventListener(future));
		return future;
	}
	
	/**
	 * 发送短信消息 这个消息一定是发送到手机上
	 * @param toBuddy	发送消息的好友
	 * @param message	需发送的消息
	 * @param listener	操作监听器
	 */
	public void sendSMSMessage(Buddy toBuddy, Message message, ActionEventListener listener)
	{
		this.ensureOnline();
		if(toBuddy.getRelation()==Relation.BANNED) {
			if(listener!=null) listener.fireEevent(new FailureEvent(FailureType.BUDDY_BLOCKED));
		}else {
			this.dialogFactory.getServerDialog().sendSMSMessage(toBuddy, message, listener);
		}
	}
	
	/**
	 * 给自己发送短信到手机上
	 * @param message  需发送的消息
	 * @param listener 操作监听器
	 */
	public void sendSMSMessageToSelf(Message message, ActionEventListener listener)
	{
		this.ensureOnline();
		this.sendSMSMessage(this.getFetionUser(), message, listener);
	}
	
	/**
	 * 通过手机号给好友发送消息，前提是该手机号对应的飞信用户必须已经是好友，否则会发送失败
	 * @param mobile		手机号码
	 * @param message		消息
	 * @param listener 		操作事件监听器
	 */
	public void sendChatMessage(long mobile,final Message message, final ActionEventListener listener)
	{
		this.ensureOnline();
		
		//是否是自己,如果是，只能给自己发送短信
		if(mobile==this.user.getMobile()) {
			this.sendSMSMessageToSelf(message, listener);
		}else {
			//要做的第一件事是找到这个好友，因为通过手机查找好友需要向服务器发起请求，所以这里先建立一个临时的事件监听器
			//当找到好友操作完成之后，判断是否找到，如果找到就发送消息
			ActionEventListener tmpListener = new ActionEventListener() {
				public void fireEevent(ActionEvent event){
					if(event.getEventType()==ActionEventType.SUCCESS){
						//成功的找到了好友，获取这个好友，然后发送消息
						FindBuddySuccessEvent evt = (FindBuddySuccessEvent) event;
						sendChatMessage(evt.getFoundBuddy(), message, listener);
					}else{
						//查找失败直接回调设置的方法
						listener.fireEevent(event);
					}
				}
			};
			//开始查找好友请求
			this.findBuddyByMobile(mobile, tmpListener);
		}
	}
	
	
	/**
	 * 通过手机号给好友发送短信，前提是该手机号对应的飞信用户必须已经是好友，否则会发送失败
	 * @param mobile		手机号码
	 * @param message		消息
	 * @param listener		操作事件监听器
	 */
	public void sendSMSMessage(long mobile, final Message message, final ActionEventListener listener)
	{
		this.ensureOnline();
		
		//注释同上一个方法，这里不赘述了
		ActionEventListener tmpListener = new ActionEventListener() {
			public void fireEevent(ActionEvent event){
				if(event.getEventType()==ActionEventType.SUCCESS){
					FindBuddySuccessEvent evt = (FindBuddySuccessEvent) event;
					sendSMSMessage(evt.getFoundBuddy(), message, listener);
				}else{
					listener.fireEevent(event);
				}
			}
		};
		//开始查找好友请求
		this.findBuddyByMobile(mobile, tmpListener);
		
	}

	 /** 
     * 设置个人信息 
     * @deprecated
     *  
     * <code> 
     * 这是一个强大的API，基本上可以改变自己的任何信息 
     * 建议使用client.setNickName()和client.setImpresa()简单接口 
     * 比如要更改用户昵称和签名可以这样 
     * User user = client.getFetionUser(); 
     * user.setNickName("GoodDay"); 
     * user.setImpresa("I'd love it.."); 
     * client.setPersonalInfo(new ActionEventListener(){ 
     *              public void fireEvent(ActionEvent event){ 
     *                  if(event.getEventType()==ActionEventType.SUCCESS) 
     *                      System.out.println("set personal info success!"); 
     *                  else 
     *                      System.out.println("set personal info failed!"); 
     *              } 
     * }); 
     * </code> 
     * @param listener 
     */  
	private void setPersonalInfo(ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().setPesonalInfo(listener);
	}
	
	
	/**
	 * 以手机号码查找好友
	 * 
	 * 因为飞信权限的原因，直接去遍历手机好友是不行的，因为部分好友设置为对方看不见好友的手机号码，
	 * 这里需要发起一个获取好友信息的请求然后返回user-id，用这个user-id去遍历好友列表就可以查询到好友
	 * @param mobile	手机号码
	 * @param listener  操作结果监听器
	 */
	public void findBuddyByMobile(long mobile, ActionEventListener listener)
	{
		this.ensureOnline();
		
		//先判断是否是合法的移动号码
		if(!AccountValidator.validateMobile(mobile)){
			if(listener!=null){
				listener.fireEevent(new FailureEvent(FailureType.INVALID_ACCOUNT));
			}
		}
		
		//可能部分好友已经获取到了手机号，特别是没有开通飞信的好友手机号码就是已知的
		//为了提高效率，这里先遍历好友，查看是否有好友的手机号码和给定的号码相同
		Iterator<Buddy> it = this.getFetionStore().getBuddyList().iterator();
		while(it.hasNext()){
			Buddy buddy = it.next();
			if(buddy.getMobile()==mobile){	//找到了好友，直接通知监听器
				if(listener!=null){
					listener.fireEevent(new FindBuddySuccessEvent(buddy));
					return;
				}
			}
		}
		
		//仍然没找到，这才向服务器发起查询
		this.dialogFactory.getServerDialog().findBuddyByMobile(mobile, listener);
	}
	
	/**
	 * 设置好友本地姓名
	 * @param buddy		 好友
	 * @param localName	 本地姓名
	 * @param listener
	 */
	public void setBuddyLocalName(Buddy buddy,String localName, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().setBuddyLocalName(buddy, localName, listener);
	}
	
	/**
	 * 移动好友到多个分组
	 * 飞信好友分组有点奇怪，一个好友可以属于多个分组，谁想出的这个需求····
	 * @param buddy			好友对象
	 * @param cordIds		多个好友分组列表
	 * @param listener
	 */
	public void setBuddyCord(Buddy buddy, Collection<Cord> cordList, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().setBuddyCord(buddy, cordList, listener);
	}
	
	/**
	 * 移动好友到单个分组
	 * @param buddy		好友对象
	 * @param cordIds	单个好友分组对象
	 * @param listener
	 */
	public void setBuddyCord(Buddy buddy, Cord cord, ActionEventListener listener)
	{
		this.ensureOnline();
		ArrayList<Cord> list = new ArrayList<Cord>();
		list.add(cord);
		this.dialogFactory.getServerDialog().setBuddyCord(buddy, list, listener);
	}
	
	/**
	 * 添加好友
	 * @param seviceId		手机号码或者飞信号码
	 * @param localName		添加好友后显示的本地名称，如果为null,默认显示好友的昵称
	 * @param cord			好友添加到分组的编号，如果为null，添加到默认分组
	 * @param desc			对自己的说明 ，如 xxx，在对方飞信好友里就会收到：我是xxxx
	 * @param promptId		提示的信息编号,见下面的说明。如果不清楚，请直接传递0
	 * @param localName
	 * @param listener		结果监听器
	 * 
	 * <pre>
	 * 对于promptId的和desc的说明：
	 * 飞信添加好友的提示信息是这样的格式 我是{名字}, {提示}。
	 * 其中desc就是格式中的{名字}(不包含我是)，
	 * 参数中的promptId, 是添加好友时，后面的提示信息
	 * 后面的说明是一个整形数字，为啥不是字符串，解释一下：
	 * 因为飞信预定义了一些提示信息，这些信息会在登陆时获取系统配置那一步获取到，如 
	 * 
	 *   0. 正在用中国移动飞信业务，想加你为好友
	 *   1. 想加你为飞信好友，方便咱们联系。
	 *   2. 飞信挺好用的，我想加你为好友。
	 *   3. 希望你能成为我的飞信好友，常联系。
	 *   4. 成我的飞信好友，能免费的给你发短信。
	 *   
	 *   这些提示信息，会有个编号，这里的参数 promptId就是这里的编号。
	 *   至于飞信为啥不允许用户输入一些描述信息，可能是考虑到防止用户被骚扰吧。
	 *</pre>
	 */
	public void addBuddy(String account, String localName, Cord cord, String desc, int promptId, ActionEventListener listener)
	{
		this.ensureOnline();
		AccountValidator validator = new AccountValidator(account);
		if(validator.isValidMobile()){
			this.dialogFactory.getServerDialog().addBuddy("tel:"+account, localName, cord, desc, promptId,  listener);
		}else if(validator.getFetionId()>0){
			this.dialogFactory.getServerDialog().addBuddy("sip:"+account, localName, cord, desc, promptId, listener);
		}else{
			if(listener!=null){
				listener.fireEevent(new FailureEvent(FailureType.INVALID_ACCOUNT));
			}
		}
	}
	
	/**
	 * 添加好友的简单接口
	 * @param account	手机号码或者飞信号码
	 * @param listener	结果监听器
	 */
	public void addBuddy(String account, ActionEventListener listener)
	{
		this.ensureOnline();
		this.addBuddy(account, null, null, this.user.getDisplayName(), 0, listener);
	}
	
	/**
	 * 删除好友
	 * @param buddy		需删除的好友
	 * @param listener
	 */
	public void deleteBuddy(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().deleteBuddy(buddy, listener);
	}
	
	/**
	 * 获取好友的详细信息
	 * @param buddy		飞信好友
	 * @param listener
	 */
	public void retireBuddyInfo(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().retireBuddyInfo(buddy, listener);
	}
	
	/**
	 * 同意对方添加好友
	 * @param buddy			飞信好友
	 * @param listener
	 */
	public void agreedApplication(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().agreedApplication(buddy, listener);
	}
	
	/**
	 * 拒绝对方添加请求
	 * @param buddy			飞信好友
	 * @param listener
	 */
	public void declinedApplication(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().declinedAppliction(buddy, listener);
	}
	
	
	/**
	 * 设置用户状态
	 * @param presence		用户状态定义在Presence中
	 * @param listener
	 */
	public void setPresence(int presence, ActionEventListener listener)
	{
		this.ensureOnline();
		if(Presence.isValidPresenceValue(presence)){
			this.dialogFactory.getServerDialog().setPresence(presence, listener);
		}else{
			if(listener!=null)	listener.fireEevent(new FailureEvent(FailureType.INVALID_PRESENCE_VALUE));
		}
	}
	
	/**
	 * 创建新的好友分组
	 * @param title		分组名称
	 * @param listener
	 */
	public void createCord(String title, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().createCord(title, listener);
	}
	
	/**
	 * 删除一个分组,删除分组之前请先把属于这个分组的好友移至其他分组再删除这个分组
	 * @param cord		分组对象
	 * @param listener
	 */
	public void deleteCord(Cord cord, ActionEventListener listener)
	{
		this.ensureOnline();
		if(this.store.getBuddyListByCord(cord).size()>0 && listener!=null){
			listener.fireEevent(new FailureEvent(FailureType.CORD_NOT_EMPTY));
		}else{
			this.dialogFactory.getServerDialog().deleteCord(cord, listener);
		}
	}
	
	/**
	 * 设置分组名称
	 * @param cord		分组对象
	 * @param title		分组名称
	 * @param listener
	 */
	public void setCordTitle(Cord cord, String title, ActionEventListener listener)
	{
		this.ensureOnline();
		this.dialogFactory.getServerDialog().setCordTitle(cord, title, listener);
	}
	
	/**
	 * 设置用户昵称
	 * @param nickName	设置用户昵称
	 * @param listener
	 */
	public void setNickName(String nickName, ActionEventListener listener)
	{
		this.ensureOnline();
		 this.user.setNickName(nickName);
		 this.setPersonalInfo(listener);
	}
	
	/**
	 * 设置用户个性签名
	 * @param impresa	个性签名
	 * @param listener
	 */
	public void setImpresa(String impresa, ActionEventListener listener)
	{
		this.ensureOnline();
		this.user.setImpresa(impresa);
		this.setPersonalInfo(listener);
	}
	
	
	/**
	 * 获取定时短信列表
	 * 注意：在登录的过程中并没有获取定时短信这一步，所以在这里需要手动的进行获取短信列表
	 * @param listener
	 */
	public void getScheduleSMSList(final ActionEventListener listener)
	{
		this.ensureOnline();
		this.executor.submitTask(new GetScheduleListWork(this, listener));
	}
	
	/**
	 * 创建定时短信
	 * @param message		定时短信内容
	 * @param sendDate		发送时间	，有效时间是在当前时间后11分钟到一年以内，否则会返回错误
	 * @param receiverList	短信的接收者列表
	 * @param listener		操作监听器
	 */
	public void createScheduleSMS(Message message, Date sendDate, Collection<Buddy> receiverList, ActionEventListener listener)
	{
		this.ensureOnline();
		//检查定时短信的定时时间，最短时间是 当前时间+11分钟-一年后当前时间
		//比如当前时间是 2007.7.1 22:56 有效的时间是 2010.7.1 23:07 - 2011.7.1 22:56，在这个时间之内的才是有效时间，
		Calendar calMin = Calendar.getInstance();
		calMin.add(Calendar.MINUTE, 11);
		Calendar calMax = Calendar.getInstance();
		calMax.add(Calendar.YEAR, 1);
		//判断是否在有效的范围内，如果不在这个范围内则返回发送时间错误
		if( sendDate.after(calMin.getTime()) && sendDate.before(calMax.getTime())){
			ScheduleSMS sc = new ScheduleSMS(-1, message, sendDate,  receiverList);
			this.getServerDialog().createScheduleSMS(sc, listener);
		}else{
			if(listener!=null)	listener.fireEevent(new FailureEvent(FailureType.INVALID_SEND_DATE));
		}
	}
	
	/**
	 * 批量删除定时短信
	 * @param sclist	定时短信列表
	 * @param listener
	 */
	public void deleteScheduleSMS(Collection<ScheduleSMS> sclist, ActionEventListener listener)
	{
		this.getServerDialog().deleteScheduleSMS(sclist, listener);
	}
	
	/**
	 * 删除一条定时短信
	 * @param scheduleSMS	定时短信
	 * @param listener
	 */
	public void deleteScheduleSMS(ScheduleSMS scheduleSMS, ActionEventListener listener)
	{
		this.ensureOnline();
		ArrayList<ScheduleSMS> list = new ArrayList<ScheduleSMS>();
		list.add(scheduleSMS);
		this.getServerDialog().deleteScheduleSMS(list, listener);
	}
	
	/**
	 * 验证定时短信列表，删除掉已经发送的定时短信
	 */
	public void invalidateScheduleSMSList()
	{
		this.ensureOnline();
		synchronized (this.store) {
			Iterator <ScheduleSMS> it = this.store.getScheduleSMSList().iterator();
			Date nowDate = new Date();
			while(it.hasNext()){
				ScheduleSMS sc = it.next();
				if(nowDate.after(sc.getSendDate())){		//如果当前时间大于定时短信发送时间，删除这个定时短信
					it.remove();
				}
			}
		}
	}
	
	/**
	 * 将好友添加到黑名单，也就是无法发送消息给在黑名单的好友
	 * 添加之后和该好友的关系变为Relation.BANNED.
	 * @param buddy			需添加到黑名单的好友
	 * @param listener
	 */
	public void addBuddyToBlackList(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		if(buddy.getRelation()==Relation.BANNED) {
			if(listener!=null) listener.fireEevent(new FailureEvent(FailureType.BUDDY_IN_BLACKLIST));
		}else {
			this.getServerDialog().addBuddyToBlackList(buddy, listener);
		}
	}
	
	/**
	 * 将好友从黑名单中删除
	 * 删除好友的关系变为Relation.BUDDY
	 * @param buddy		需从黑名单中删除的好友
	 * @param listener
	 */
	public void removeBuddyFromBlackList(Buddy buddy, ActionEventListener listener)
	{
		this.ensureOnline();
		if(buddy.getRelation()!=Relation.BANNED) {
			if(listener!=null) listener.fireEevent(new FailureEvent(FailureType.BUDDY_NOT_IN_BLACKLIST));
		}else {
			this.getServerDialog().removeBuddyFromBlackList(buddy, listener);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	// 下面是HTTP操作，所有的HTTP操作都是同步的，如果出错抛出IO异常
	////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 获取好友头像
	 * @param buddy		好友对象
	 * @return			成功返回好友头像，并存储在好友对象中
	 * @throws IOException
	 */
	public BufferedImage httpGetPortrait(Buddy buddy) throws IOException
	{
		this.ensureOnline();
		this.ensureSSISigned();
		BufferedImage portrait = HttpApplication.getPortrait(this, buddy, 120);
		buddy.setPortrait(portrait);
		return portrait;
	}
	
	/**
	 * 设置用户的头像，头像的的高和宽都不能超过120px
	 * @param image			图片对象，可以使用ImageIO.read()来从流中读取
	 * @param listener
	 * @throws IOException 
	 */
	public void httpSetPortrait(BufferedImage image) throws IOException
	{
		this.ensureOnline();
		this.ensureSSISigned();
		if(image.getHeight()>120 || image.getWidth()>120) {
			throw new IllegalArgumentException("portrait height and width should below 120 px.");
		}else {
			long portraitCrc = HttpApplication.setPortrait(this, image);
		}
	}
	
}
