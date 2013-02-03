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
 * Package  : net.solosky.maplefetion.client
 * File     : LoginWork.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-2-24
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;


import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import net.solosky.maplefetion.ClientState;
import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Presence;
import net.solosky.maplefetion.bean.StoreVersion;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.client.dialog.DialogException;
import net.solosky.maplefetion.client.dialog.GroupDialog;
import net.solosky.maplefetion.client.dialog.IllegalResponseException;
import net.solosky.maplefetion.client.dialog.ServerDialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.event.action.FutureActionEventListener;
import net.solosky.maplefetion.event.notify.LoginStateEvent;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.LocaleSetting;
import net.solosky.maplefetion.util.ObjectWaiter;

import org.apache.log4j.Logger;

/**
 *
 * 登录过程
 *
 * @author solosky <solosky772@qq.com>
 */
public class LoginWork implements Runnable
{
	/**
	 * 飞信运行上下文
	 */
	private FetionContext context;

	/**
	 * SSI登录对象
	 */
	private SSISign signAction;
	
	/**
	 * 用户状态
	 */
	private int presence;

	/**
	 * 是否用户取消了登录
	 */
	private boolean isCanceledLogin;
	
	/**
	 * 等待用户处理SSI验证码事件
	 */
	private ObjectWaiter<VerifyImage> ssiVerifyWaiter;
	
	/**
	 * 同步登录的处理对象
	 */
	private ObjectWaiter<LoginState> loginWaiter;
	/**
	 * LOGGER
	 */
	private static final Logger logger = Logger.getLogger(LoginWork.class);
	
	/**
	 * 构造函数
	 * @param context
	 */
	public LoginWork(FetionContext context, int presence)
	{
		this.context = context;
		this.presence = presence;
		this.signAction = new SSISignV4();
		this.ssiVerifyWaiter = new ObjectWaiter<VerifyImage>();
		this.loginWaiter     = new ObjectWaiter<LoginState>();
		
		this.signAction.setLocaleSetting(this.context.getLocaleSetting());
		this.signAction.setFetionContext(this.context);
	}
	
	
	/**
	 * 尝试登录
	 * @throws InterruptedException 
	 * @throws SystemException 
	 * @throws DialogException 
	 * @throws RequestTimeoutException 
	 * @throws TransferException 
	 * @throws LoginException 
	 */
	public void login() throws LoginException, TransferException, RequestTimeoutException, DialogException, SystemException, InterruptedException
	{
		this.context.updateState(ClientState.LOGGING);
		this.updateSystemConfig();	//获取自适应配置
		  this.checkCanceledLogin();
		this.SSISign();				//SSI登录 
		  this.checkCanceledLogin();
		this.openServerDialog();	//服务器连接并验证
	 	  this.checkCanceledLogin();
		this.getContactsInfo();		//获取联系人列表和信息
		  this.checkCanceledLogin();
		boolean groupEnabled = FetionConfig.getBoolean("fetion.group.enable");
		if(groupEnabled) {	//启用了群
			this.getGroupsInfo();	 	//获取群信息
			this.openGroupDialogs();	//建立群会话
		}
	
		this.updateLoginState(LoginState.LOGIN_SUCCESS, null);
		
	}
	
	/**
	 * 检查是否取消登录，如果取消登录抛出登录异常，结束登录过程
	 * @throws LoginException
	 */
	private void checkCanceledLogin() throws LoginException {
		if(this.isCanceledLogin)	
			throw new LoginException(LoginState.LOGIN_CANCELED);
	}
	

    @Override
    public void run()
    {
    	try {
	        this.login();
        } catch (LoginException e) {
	        this.updateLoginState(e.getState(), e);
        } catch (TransferException e) {
        	this.updateLoginState(LoginState.SIPC_CONNECT_FAIL, e);
        } catch (RequestTimeoutException e) {
        	this.updateLoginState(LoginState.SIPC_TIMEOUT, e);
        } catch (DialogException e) {
        	this.updateLoginState(LoginState.OTHER_ERROR, e);
        } catch (SystemException e) {
        	this.updateLoginState(LoginState.OTHER_ERROR, e);
        } catch (InterruptedException e) {
    		this.updateLoginState(LoginState.OTHER_ERROR, e);
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////
    /**
     * 更新自适应配置
     */
    private void updateSystemConfig() throws LoginException
    {
    	LocaleSetting localeSetting = this.context.getLocaleSetting();
    	if(!localeSetting.isLoaded()) {
    		try {
    			logger.debug("Loading locale setting...");
				this.updateLoginState(LoginState.SEETING_LOAD_DOING, null);
				localeSetting.load(this.context.getFetionUser());
				if(!localeSetting.isValid())	//获取配置中如果无效，表明用户输入的账号无效
					throw new LoginException(LoginState.SSI_ACCOUNT_NOT_FOUND);
				
				this.updateLoginState(LoginState.SETTING_LOAD_SUCCESS, null);
			} catch (Exception e) {
				logger.debug("Load localeSetting error", e);
				throw new LoginException(LoginState.SETTING_LOAD_FAIL, e);
			}
    	}
    }
    
    /**
     * SSI登录
     * @throws LoginException 
     */
    private void SSISign() throws LoginException
    {
    	this.updateLoginState(LoginState.SSI_SIGN_IN_DOING, null);
    	
    	//第一次尝试不用验证码登录
    	LoginState state = this.signAction.signIn(this.context.getFetionUser());
    	//如果结果为需要验证或者验证失败，重复验证，直到SSI的结果为其他结果为止
    	while(state==LoginState.SSI_NEED_VERIFY || state==LoginState.SSI_VERIFY_FAIL) {
            try {
            	VerifyImage img = this.ssiVerifyWaiter.waitObject();
            	state = this.signAction.signIn(this.context.getFetionUser(), img);
            } catch (ExecutionException e) {
            	 throw new LoginException(LoginState.OTHER_ERROR, e);
            } catch (TimeoutException e) {
            	 throw new LoginException(LoginState.OTHER_ERROR, e);
            } catch (InterruptedException e) {
	           if(!this.isCanceledLogin) throw new LoginException(LoginState.OTHER_ERROR, e);
            }
    	}
    	
    	//如果SSI登录出错，抛出登录异常，结束登录流程
    	//因为上面判断了SSI_NEED_VERIFY和SSI_VERIFY_FAIL事件，所以这里只需判断是否SSI登录成功即可
    	if(state!=LoginState.SSI_SIGN_IN_SUCCESS)	throw new LoginException(state);
    		
		this.updateLoginState(state, null);
    }
    
    /**
     * 建立服务器会话
     * @throws DialogException 
     * @throws RequestTimeoutException 
     * @throws TransferException 
     * @throws InterruptedException 
     * @throws SystemException 
     */
    private void openServerDialog() throws LoginException, TransferException, RequestTimeoutException, DialogException, SystemException, InterruptedException
    {
    	//判断是否有飞信号
    	if(this.context.getFetionUser().getFetionId()==0){
    		throw new IllegalArgumentException("Invalid fetion id. if disabled SSI sign, you must login with fetion id..");
    	}
    	
    	this.updateLoginState(LoginState.SIPC_REGISTER_DOING, null);
		ServerDialog serverDialog = this.context.getDialogFactory().createServerDialog();
        serverDialog.openDialog();
        
        ActionEventFuture future = new ActionEventFuture();
    	ActionEventListener listener = new FutureActionEventListener(future);
    	
    	//注册服务器
    	serverDialog.register(presence, listener);
    	Dialog.assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
    	
    	//用户验证
    	future.clear();
    	serverDialog.userAuth(presence, listener);
    	ActionEvent event = future.waitActionEventWithoutException();
    	if(event.getEventType()==ActionEventType.SUCCESS){
    		this.updateLoginState(LoginState.SIPC_REGISGER_SUCCESS, null);
    	}else if(event.getEventType()==ActionEventType.FAILURE){
    		FailureEvent evt = (FailureEvent) event;
    		FailureType type =  evt.getFailureType();
    		if(type==FailureType.REGISTER_FORBIDDEN){
    			throw new LoginException(LoginState.SIPC_ACCOUNT_FORBIDDEN); //帐号限制登录，可能存在不安全因素，请修改密码后再登录
    		}else if(type==FailureType.AUTHORIZATION_FAIL){
    			throw new LoginException(LoginState.SIPC_AUTH_FAIL);			//登录验证失败
    		}else{
    			Dialog.assertActionEvent(event, ActionEventType.SUCCESS);
    		}
    	}
    }
    
    /**
     * 获取联系人信息
     * @throws InterruptedException 
     * @throws SystemException 
     * @throws TransferException 
     * @throws RequestTimeoutException 
     * @throws IllegalResponseException 
     */
    private void getContactsInfo() throws IllegalResponseException, RequestTimeoutException, TransferException, SystemException, InterruptedException
    {
    	ActionEventFuture future = new ActionEventFuture();
    	ActionEventListener listener = new FutureActionEventListener(future);
    	ServerDialog dialog = this.context.getDialogFactory().getServerDialog();
		this.updateLoginState(LoginState.GET_CONTACTS_INFO_DOING, null);
		
        
        //订阅异步通知
		if(this.context.getFetionStore().getBuddyList().size()>0){
	        future.clear();
	        dialog.subscribeBuddyNotify(listener);
	        Dialog.assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
		}
        
        this.updateLoginState(LoginState.GET_CONTACTS_INFO_SUCCESS, null);
    }
    
    /**
     * 获取群信息
     * @throws InterruptedException 
     * @throws SystemException 
     * @throws TransferException 
     * @throws RequestTimeoutException 
     * @throws IllegalResponseException 
     */
    private void getGroupsInfo() throws IllegalResponseException, RequestTimeoutException, TransferException, SystemException, InterruptedException
    {
    	ActionEventFuture future = new ActionEventFuture();
    	ActionEventListener listener = new FutureActionEventListener(future);
    	ServerDialog dialog = this.context.getDialogFactory().getServerDialog();
    	StoreVersion storeVersion   = this.context.getFetionStore().getStoreVersion();
    	StoreVersion userVersion    = this.context.getFetionUser().getStoreVersion();

    	this.updateLoginState(LoginState.GET_GROUPS_INFO_DOING, null);
        //获取群列表
        future.clear();
        dialog.getGroupList(listener);
        Dialog.assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
        
		//如果群列表为空，就不发送下面的一些请求了
		FetionStore store = this.context.getFetionStore();
		if(store.getGroupList().size()==0){
			logger.debug("The group list is empty, group dialog login is skipped.");
			return;
		}

        //如果当前存储版本和服务器相同，就不获取群信息和群成员列表，
        //TODO ..这里只是解决了重新登录的问题，事实上这里问题很大，群信息分成很多
        //用户加入的群列表 groupListVersion
        //某群的信息		  groupInfoVersion
        //群成员列表		  groupMemberListVersion
        //暂时就这样，逐步完善中.....
        logger.debug("GroupListVersion: server="+userVersion.getGroupVersion()+", local="+storeVersion.getGroupVersion());
        if(storeVersion.getGroupVersion()!=userVersion.getGroupVersion()) {
			//更新存储版本
			storeVersion.setGroupVersion(userVersion.getGroupVersion());
	        //获取群信息
	        future.clear();
	        dialog.getGroupsInfo(this.context.getFetionStore().getGroupList(), listener);
	        Dialog.assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
        
        	//获取群成员
	        future.clear();
	        dialog.getMemberList(this.context.getFetionStore().getGroupList(), listener);
	        Dialog.assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
	        
	        storeVersion.setGroupVersion(userVersion.getGroupVersion());
        }
        
    	this.updateLoginState(LoginState.GET_GROUPS_INFO_SUCCESS,null);
    }
    
    /**
     * 打开群会话
     * @throws DialogException 
     * @throws RequestTimeoutException 
     * @throws TransferException 
     */
    private void openGroupDialogs() throws TransferException, RequestTimeoutException, DialogException
    {
    	this.updateLoginState(LoginState.GROUPS_REGISTER_DOING, null);
		Iterator<Group> it = this.context.getFetionStore().getGroupList().iterator();
        while (it.hasNext()) {
        	GroupDialog groupDialog = this.context.getDialogFactory().createGroupDialog(it.next());
        	groupDialog.openDialog();
        }
        
        this.updateLoginState(LoginState.GROUPS_REGISTER_SUCCESS, null);
}
    
    /**
     * 更新登录状态
     * @param status
     */
    private void updateLoginState(LoginState state, Throwable t)
    {
    	logger.debug("Login state:"+state+", canceledLogin="+isCanceledLogin, t);
    	
    	if(this.context.getNotifyEventListener()!=null && !this.isCanceledLogin)
    		this.context.getNotifyEventListener().fireEvent(new LoginStateEvent(state));
    		
    	if(state.getValue()>400 && !this.isCanceledLogin) {	//大于400都是登录出错
    		this.loginWaiter.objectArrive(state);
    		this.context.handleException(new LoginException(state));
    	}else if(state==LoginState.LOGIN_SUCCESS) {
    		this.loginWaiter.objectArrive(state);
    		this.context.updateState(ClientState.ONLINE);
    		this.context.getDialogFactory().getServerDialog().startKeepAlive();
    	}else {
    		//logger.warn("Unhandled login state="+state);
    	}
    }
    ////////////////////////////////////////////////////////////////////

	/**
     * @param verifyImage the verifyImage to set
     */
    public void setVerifyImage(VerifyImage verifyImage)
    {
    	this.ssiVerifyWaiter.objectArrive(verifyImage);
    }

	/**
     * @param presence the presence to set
     */
    public void setPresence(int presence)
    {
    	if(Presence.isValidPresenceValue(presence)) {
    		this.presence = presence;
    	}else {
    		throw new IllegalArgumentException("presence "+presence+" is invalid. Presense const is defined in Presence class.");
    	}
    }
    
    /**
     * @return the presence
     */
    public int getPresence()
    {
    	return presence;
    }


	/**
     * 释放资源
     */
    public void dispose()
    {
    }
    
    /**
     * 等待登陆结果通知
     * 事实上这个方法不会永远超时，因为客户端登陆已经包含了超时控制
     * @return
     */
    public LoginState waitLoginState(long timeout)
    {
    	try {
	        return this.loginWaiter.waitObject(timeout);
        } catch (ExecutionException e) {
	        return LoginState.OTHER_ERROR;
        } catch (TimeoutException e) {
	        return LoginState.LOGIN_TIMEOUT;
        } catch (InterruptedException e) {
        	return LoginState.OTHER_ERROR;
        }
    }
    
    /**
     * 取消登录
     */
    public void cancelLogin() {
    	this.isCanceledLogin = true;
    	this.loginWaiter.objectArrive(LoginState.LOGIN_CANCELED);
    }
}