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
 * Package  : net.solosky.maplefetion.client.dialog
 * File     : ChatDialogProxy.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-5-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FutureActionEventListener;
import net.solosky.maplefetion.event.action.SystemErrorEvent;
import net.solosky.maplefetion.event.action.TimeoutEvent;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.TransferException;

import org.apache.log4j.Logger;

/**
 *
 * 聊天对话框代理类
 * 
 * <pre>
 * 因为一个对话的状态发生改变可能是用户主动发生如用户主动关闭对话，
 * 也可能是客户端内部改变，如用户下线了对话框就会关闭
 * 当使用者使用FetionClient，持有一个聊天对话对象的状态的时候就不确定，
 * 可能是打开状态，也可能是关闭状态，并且很多操作都需要在打开状态操作，
 * 比如发消息只能在对话框打开的状态发送，其他状态发送就会抛出异常，这给使用者造成了很大的负担，因为需要维护一个对话
 * 聊天对话框代理类可以负责维护一个可持续的对话，当对话框被关闭的时候重新建立一个新的对话，
 * 并且在当对话框没有建立成功的时候可以缓存发送的消息，等待对话框建立之后再发送
 * </pre>
 * @author solosky <solosky772@qq.com>
 */
public class ChatDialogProxy implements DialogListener
{
	/**
	 * 代理的聊天对话
	 */
	private ChatDialog proxyChatDialog;
	
	/**
	 * 飞信上下文
	 */
	private FetionContext fetionContext;
	
	/**
	 * 参与的好友
	 */
	private Buddy mainBuddy;
	
	/**
	 * 需要发送的消息队列，在对话框还没有建立的时候默认会保存到临时队列，等对话框建立的时候开始发送
	 */
	protected ArrayList<MessageEntry> readyMessageList;
	
	/**
	 * LOGGER
	 */
	private static Logger logger = Logger.getLogger(ChatDialogProxy.class);
	
	
	/**
	 * 以聊天对话框构建代理对象
	 * @param dialog	聊天对话框
	 * @param context	飞信上下文
	 */
	public ChatDialogProxy(ChatDialog dialog, FetionContext client)
	{
		this.proxyChatDialog	= dialog;
		this.fetionContext		= client;
		this.mainBuddy			= dialog.getMainBuddy();
		this.readyMessageList	= new ArrayList<MessageEntry>();
		
		this.proxyChatDialog.setDialogListener(this);
	}
	
	/**
	 * 以好友对象构造代理对象
	 * @param buddy		好友对象
	 * @param client	飞信上下文
	 * @throws DialogException 
	 */
	public ChatDialogProxy(Buddy buddy , FetionContext client) throws DialogException
	{
		this.fetionContext      = client;
		this.mainBuddy          = buddy;
		this.readyMessageList   = new ArrayList<MessageEntry>();
		this.proxyChatDialog    = this.fetionContext.getDialogFactory().findOrCreateChatDialog(buddy);
		this.proxyChatDialog.setDialogListener(this);
	}
	
	
    /**
     * 
     * @see net.solosky.maplefetion.client.dialog.Dialog#closeDialog()
     */
    public void closeDialog()
    {
    	try {
	        this.proxyChatDialog.closeDialog();
        } catch (Exception e) {
        	logger.warn("Close Dialog Failed.",e);
        }
    }

	/**
     * @return
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#getMainBuddy()
     */
    public Buddy getMainBuddy()
    {
	    return proxyChatDialog.getMainBuddy();
    }

	/**
     * @return
     * @see net.solosky.maplefetion.client.dialog.Dialog#getSession()
     */
    public DialogSession getSession()
    {
	    return proxyChatDialog.getSession();
    }

	/**
     * @return
     * @see net.solosky.maplefetion.client.dialog.Dialog#getState()
     */
    public DialogState getState()
    {
	    return proxyChatDialog.getState();
    }

	/**
     * @throws TransferException
     * @throws RequestTimeoutException
     * @throws DialogException
     * @see net.solosky.maplefetion.client.dialog.Dialog#openDialog()
     */
    public void openDialog() throws TransferException, RequestTimeoutException,
            DialogException
    {
    	if(proxyChatDialog.getState()==DialogState.CREATED) {
    		proxyChatDialog.openDialog();
    	}
    }

	/**
     * @param listener
     * @see net.solosky.maplefetion.client.dialog.Dialog#openDialog(net.solosky.maplefetion.event.action.ActionEventListener)
     */
    public void openDialog(ActionEventListener listener)
    {
    	if(this.proxyChatDialog.getState()==DialogState.CREATED)
    		this.proxyChatDialog.openDialog(listener);
    }

    /**
	 * 给好友发送在线消息,如果对话框没有建立，就默认保存到发送队列中
	 * @param message		消息内容
	 * @param listener		操作监听器
	 * @throws TransferException
	 */
	public void sendChatMessage(Message message, ActionEventListener listener)
	{
		//在判断对话框状态并且决定消息处理过程中，必须要进行同步，因为在判断的过程中对话框状态可能发生了改变
		//比如在下面的判断中，对话框从正在打开状态变成了打开状态，而刚刚好判断过打开状态，导致了消息放入队列不会发送出去
		//解决方法就是在判断消息的过程中需获取对话框的锁，在对话框改变状态的回调方法中也去获得对话框锁，这样就可以保证一致
		
		//检查代理对话框状态,如果当前对话框为关闭或者打开失败状态，新建一个对话
		synchronized (this.proxyChatDialog) {
			if( this.proxyChatDialog==null ||
		    		this.proxyChatDialog.getState()==DialogState.CLOSED ||
		    		this.proxyChatDialog.getState()==DialogState.FAILED) {
		    		try {
		                this.proxyChatDialog = this.fetionContext.getDialogFactory().createChatDialog(this.mainBuddy);
		                this.proxyChatDialog.setDialogListener(this);
	                } catch (DialogException e) {
	                	logger.warn("Create ChatDialog failed.", e);
		               listener.fireEevent(new SystemErrorEvent(e));
		               return;			//创建失败就需要马上返回，否则下面还会抛出一个 IllegalStateException异常
	                }
		    	}
		}
		
		
		//为什么这里要新建一个同步块呢？ 因为上面的代码可能更换了代理的对话，所以需要重新同步一次
		synchronized (this.proxyChatDialog) {
			
			DialogState state = this.proxyChatDialog.getState();
			
			//如果当前对话框没有打开，异步打开这个对话框
			if(state==DialogState.CREATED) {
				this.proxyChatDialog.openDialog(null);	//异步打开
			}
			
			//如果当前对话框已经打开，就直接发送，没有打开就放入发送队列，等待对话框建立之后再发送
			//NOTE: 这里仍然可能发生同步问题
			if(state==DialogState.OPENED) {
				this.proxyChatDialog.sendChatMessage(message, listener);
			}else if(state==DialogState.OPENNING || state==DialogState.CREATED) {
		         this.readyMessageList.add(new MessageEntry(message, listener));   
			}else {
				throw new IllegalStateException("Unkown dialog state. State="+this.proxyChatDialog.getState().name());
			}
        }
		
	}
	
	/**
     * @param message
     * @return
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#sendChatMessage(net.solosky.maplefetion.bean.Message)
     */
    public ActionEventFuture sendChatMessage(Message message)
    {
    	ActionEventFuture future = new ActionEventFuture();
	    this.sendChatMessage(message, new FutureActionEventListener(future));
	    return future;
    }

	/**
     * @param message
     * @param listener
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#sendSMSMessage(net.solosky.maplefetion.bean.Message, net.solosky.maplefetion.event.action.ActionEventListener)
     */
    public void sendSMSMessage(Message message, ActionEventListener listener)
    {
	    proxyChatDialog.sendSMSMessage(message, listener);
    }

	/**
     * @param message
     * @return
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#sendSMSMessage(net.solosky.maplefetion.bean.Message)
     */
    public ActionEventFuture sendSMSMessage(Message message)
    {
	    return proxyChatDialog.sendSMSMessage(message);
    }

	/**
	 * 对话框状态改变监听器
	 */
    @Override
    public void dialogStateChanged(DialogState state)
    {
    	//同样，在改变对话框状态时，首先要获得对话锁，防止消息放入队列中而永远不会发送
    	synchronized (this.proxyChatDialog) {
    		switch(state) {
        		case OPENED: this.processReadyMessage(true);  break;
        		case FAILED: this.processReadyMessage(false); break;
    		}
        }
    }
    
    
    /**
     * 发送临时队列中的消息
     * 如果对话框建立成功，就发送消息，否则回调超时处理
     */
    private void processReadyMessage(boolean isSuccess)
    {
        Iterator<MessageEntry> it = this.readyMessageList.iterator();
        while(it.hasNext()) {
        	MessageEntry entry = it.next();
        	if(isSuccess) {
        		this.proxyChatDialog.sendChatMessage(entry.getMessage(), entry.getActionEventListener());
        	}else {
        		entry.getActionEventListener().fireEevent(new TimeoutEvent());
        	}
        }
        this.readyMessageList.clear();
}
    

	/**
     * 一个内部类，用于保存消息的结构
     */
    private class MessageEntry{
    	
    	private Message message;
    	private ActionEventListener listener;
    	
    	public MessageEntry(Message message, ActionEventListener listener)
    	{
    		this.message  = message;
    		this.listener = listener; 
    	}
    	
    	public Message getMessage() 
    	{
    		return this.message;
    	}
    	public ActionEventListener getActionEventListener()
    	{
    		return this.listener;
    	}
    }

}
