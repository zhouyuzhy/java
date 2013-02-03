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
 * File     : LiveV2ChatDialog.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-14
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dialog;

import java.util.ArrayList;
import java.util.Iterator;

import net.solosky.maplefetion.ExceptionHandler;
import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.chain.ProcessorChain;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dispatcher.LiveV2MessageDispatcher;
import net.solosky.maplefetion.client.response.DefaultResponseHandler;
import net.solosky.maplefetion.client.response.SendChatMessageResponseHandler;
import net.solosky.maplefetion.event.ActionEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FutureActionEventListener;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.net.RequestTimeoutException;
import net.solosky.maplefetion.net.Transfer;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.TransferFactory;
import net.solosky.maplefetion.net.TransferService;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcOutMessage;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.sipc.SipcStatus;
import net.solosky.maplefetion.util.BuddyEnterHelper;
import net.solosky.maplefetion.util.CrushBuilder;
import net.solosky.maplefetion.util.ResponseFuture;
import net.solosky.maplefetion.util.SipcLogger;
import net.solosky.maplefetion.util.SipcParser;
import net.solosky.maplefetion.util.TicketHelper;

import org.apache.log4j.Logger;

/**
 * 
 * 第二版和在线好友聊天对话框
 * <pre>
 * 这个就是飞信的Im-relay模式的实现，仅在支持多个独立的连接下和在线用户建立会话有效。
 * 这个对话的建立不是通过飞信主服务器，而是是通过另外一个飞信的聊天服务器
 * 
 * 假如A邀请B建立会话，简单的流程是这样的：
 *    1. A给主服务器说，我要建立会话，请给我聊天服务器的IP和进入凭证，服务器返回了聊天服务器的IP和凭证；
 *    2. A去连接返回的聊天服务器，并交给聊天服务器进入凭证，说，我想开始一个会话，服务器返回OK；
 *    3. A再给聊天服务器说，我要邀请B，这是B的地址，服务器回复说，好的，请稍等下，我去邀请B；
 *    4. 过了一会儿，聊天服务器回复说，我已经成功的邀请了B，请等待B进入对话；
 *    5. 又过了一会儿，聊天服务器通知说，B已经进入了对话，你们可以开始交谈了；
 * ...之后A就可以向聊天服务器发送消息，B就会收到。B发送的消息A也会收到。
 *    6. A说，对不起我有点事，我要离开对话，服务器说OK 
 * 到此对话结束。
 * 
 * 假设A被B邀请进入会话，流程是这样的。
 *   1. 主服务器告诉A，B邀请你对话，这是聊天服务器的IP和凭证，A告诉主服务器，OK，我同意进入会话。
 *   2. A去连接聊天服务器，并给服务器进入凭证，说我要加入一个会话，服务器返回OK；
 *   3. 聊天服务器马上告诉A，B已经进入了对话，你们可以开始交谈了；
 *    ...之后A就可以向聊天服务器发送消息，B就会收到。B发送的消息A也会收到。
 *   4. B说，我下线了，我要离开对话，服务器说OK 
 *   
 * 这里简单的说明了一个用户邀请另外一个用的过程，其实这个对话是支持多方对话的，
 * 任何进入对话的好友都可以邀请在线的好友加入会话，过程都是差不多的。
 * 只要向这个对话发送消息，所有进入这个对话的好友都会收到你发送的消息；
 * </pre>
 * @author solosky <solosky772@qq.com>
 */
public class LiveV2ChatDialog extends ChatDialog implements MutipartyDialog, ExceptionHandler
{

	/**
	 * 处理链
	 */
	private ProcessorChain processorChain;
	
	/**
	 * 消息工厂
	 */
	private MessageFactory messageFactory;
	
	/**
	 * 好友进入等待对象
	 */
	private BuddyEnterHelper buddyEnterHelper;
	
	/**
	 * 邀请通知
	 */
	private SipcNotify inviteNotify;
	
	/**
	 * 所有进入这个对话框的好友列表
	 */
	private ArrayList<Buddy> buddyList;
	
	/**
	 * LOGGER
	 */
	private static Logger logger = Logger.getLogger(LiveV2ChatDialog.class);
	
	/**
	 * @param mainBuddy
	 * @param client
	 */
	public LiveV2ChatDialog(Buddy mainBuddy, FetionContext client)
	{	
		super(mainBuddy, client);
		this.messageFactory   = new MessageFactory(client.getFetionUser());
		this.buddyEnterHelper = new BuddyEnterHelper();
		this.buddyList        = new ArrayList<Buddy>();
	}

	/**
	 * 以一个邀请通知构建
	 * @param inviteNotify
	 * @param client
	 */
	public LiveV2ChatDialog(SipcNotify inviteNotify, FetionContext client)
	{
		super(client);
		this.inviteNotify   = inviteNotify;
		this.mainBuddy      = client.getFetionStore().getBuddyByUri(inviteNotify.getFrom());
		this.messageFactory = new MessageFactory(client.getFetionUser());
		this.buddyEnterHelper = new BuddyEnterHelper();
		this.buddyList        = new ArrayList<Buddy>();
	}
	
	/**
	 * 建立处理链
	 * 
	 * @param host
	 * @param port
	 * @throws FetionException 
	 * @throws TransferException
	 */
	private void buildProcessorChain(Transfer transfer) throws FetionException
	{

		this.processorChain = new ProcessorChain();
		this.processorChain.addLast(new LiveV2MessageDispatcher(context, this, this)); 				// 消息分发服务
		if(FetionConfig.getBoolean("log.sipc.enable"))
			this.processorChain.addLast(new SipcLogger("LiveV2ChatDialog-" + mainBuddy.getFetionId())); 								// 日志记录
		this.processorChain.addLast(new TransferService(this.context)); 													// 传输服务
		this.processorChain.addLast(new SipcParser());													//信令解析器
		this.processorChain.addLast(transfer);															//传输对象
		
		this.processorChain.startProcessorChain();
	}
	
	/**
	 * 尝试建立连接，在可用的端口建立连接，直到建立一个可用的连接
	 * @param portList
	 * @return
	 */
	private Transfer buildTransfer(ArrayList<Port> portList)
	{
		TransferFactory factory = this.context.getTransferFactory();
		Transfer transfer = null;
		Port     port     = null;
		Iterator<Port> it = portList.iterator();
		while(it.hasNext()) {
			port = it.next();
			//尝试建立连接
			try {
				logger.trace("try to connect to port = "+port+" ..");
	            transfer = factory.createTransfer(port);
            } catch (TransferException e) {
            	logger.trace("Connect to port failed - Port = "+port, e);
            }
            
            //如果建立成功就跳出循环，否则继续尝试建立下一个端口的连接
			if(transfer!=null) {
				logger.trace("Transfer created success. - Transfer="+transfer.getTransferName());
				break;
			}
		}
		return transfer;
	}
	 /**
     * 发送数据包
     */
    @Override
    public void process(SipcOutMessage out)
    {
    	try {
	        this.processorChain.getFirst().processOutcoming(out);
        } catch (FetionException e) {
        	this.handleException(e);
        	if(out instanceof SipcRequest) {
        		SipcRequest request = (SipcRequest) out;
        		if(request.getResponseHandler()!=null)
        			request.getResponseHandler().ioerror(request);
        	}
        }
    }
	/**
	 * 返回处理链
     * @return
     */
    public ProcessorChain getProcessorChain()
    {
	    return this.processorChain;
    }
    
    /**
     * 用户是否被邀请进入这个对话框的
     * @return
     */
    public boolean isBeenInvited()
    {
    	return this.inviteNotify!=null;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#isMutipartySupported()
     */
    @Override
    public boolean isMutipartySupported()
    {
	    return false;
    }
    

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.ChatDialog#sendChatMessage(java.lang.String, net.solosky.maplefetion.client.dialog.ActionEventListener)
     */
    @Override
    public void sendChatMessage(Message message, ActionEventListener listener)
    {
    	 this.ensureOpened();
    	 SipcRequest request = this.messageFactory.createSendChatMessageRequest(this.mainBuddy.getUri(), message);
  	   	 request.setResponseHandler(new SendChatMessageResponseHandler(context, this, listener));
  	   	 this.process(request);
  	   	 this.updateActiveTime();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#doCloseDialog()
     */
    @Override
    protected void doCloseDialog() throws Exception
    {
		//TODO NOTE:如果发生了传输异常这里就不应该发送离开消息，否则会抛出第二个TransferException
    	if(this.processorChain!=null && !this.processorChain.isChainClosed()) {
    		this.bye();
    		this.processorChain.stopProcessorChain();
    	}
    }

	/**
	 * 打开第二版聊天对话框，这个比较麻烦
	 */
    @Override
    protected void doOpenDialog() throws Exception
    {
    		//首先要获取进入聊天服务器的凭证
    		String ticket = null;
    		if(this.isBeenInvited()) {
    			//如果被邀请，进入凭证是在邀请通知里
    			ticket = this.inviteNotify.getHeader(SipcHeader.AUTHORIZATION).getValue();
    		}else {
    			//如果不是，用户主动发起会话请求，需要向服务器获取进入凭证
    			ticket = this.context.getDialogFactory().getServerDialog().startChat();
    		}
    		TicketHelper helper = new TicketHelper(ticket);
    		
    		//然后连接聊天服务器，建立处理链
    		Transfer transfer = this.buildTransfer(helper.getPortList());
    		if(transfer==null) throw new TransferException("Cannot connect to chat server.");
    		this.buildProcessorChain(transfer);
    		
    		//发送注册信息
    		this.register(helper.getCredential());
    		
    		//如果是主动邀请,邀请好友进入对话框
    		if(!this.isBeenInvited())
    			this.invite();
    		
    		//等待好友进入对话框
    		int waitTimeout = FetionConfig.getInteger("fetion.dialog.wait-buddy-enter-timeout");
    		this.buddyEnterHelper.waitBuddyEnter(this.mainBuddy, waitTimeout*1000);
 
    }
    
    
    /**
     * 注册服务器
     * @throws InterruptedException 
     * @throws RequestTimeoutException 
     * @throws IllegalResponseException 
     * @throws TransferException 
     */
    private void register(String credential) throws RequestTimeoutException, InterruptedException, IllegalResponseException, TransferException
    {
    	//发送注册信息
		SipcRequest request = this.messageFactory.createRegisterChatRequest(credential);
		ResponseFuture future = ResponseFuture.wrap(request);
		this.process(request);
		SipcResponse response = future.waitResponse();
		assertStatus(response.getStatusCode(), SipcStatus.ACTION_OK);
    }
    
    
    /**
     * 邀请好友进入对话框
     * @param buddy
     * @throws IllegalResponseException
     * @throws RequestTimeoutException
     * @throws InterruptedException
     * @throws TransferException 
     * @throws SystemException 
     */
    private void invite() throws IllegalResponseException, RequestTimeoutException, InterruptedException, TransferException, SystemException
    {
    	ActionEventFuture future = new ActionEventFuture();
    	this.inviteBuddy(this.mainBuddy, new FutureActionEventListener(future));
    	assertActionEvent(future.waitActionEventWithException(), ActionEventType.SUCCESS);
    }
    
    /**
     * 离开对话
     * @throws TransferException 
     */
    private void bye() throws TransferException
    {
    	SipcRequest request = this.getMessageFactory().createLogoutRequest(this.mainBuddy.getUri());
    	this.process(request);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.MutipartyDialog#getBuddyByUriList()
     */
    @Override
    public ArrayList<Buddy> getBuddyList()
    {
    	return this.buddyList;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.MutipartyDialog#inviteBuddy(net.solosky.maplefetion.bean.FetionBuddy, net.solosky.maplefetion.client.dialog.ActionEventListener)
     */
    @Override
    public void inviteBuddy(Buddy buddy, ActionEventListener listener)
    {
	   SipcRequest request = this.messageFactory.createInvateBuddyRequest(this.mainBuddy.getUri());
	   request.setResponseHandler(new DefaultResponseHandler(listener));
   	   this.process(request);
    }

	/**
	 * 异常回调函数
	 * 注意：这里的异常不是在调用者调用方法的时候发生的，而是在传输对象读取一个接受消息时产生的异常，是由客户端内部产生的
	 * 比如读取数据时产生传输异常，或者处理异步通知的时发生的异常均由这个方法处理，如果调用者调用某个方法，一般只发生传输异常
	 */
    @Override
    public void handleException(FetionException e)
    {
    	//如果是网络错误，关闭这个对话，其他错误可以忽略掉
    	if(e instanceof TransferException) {
    		try {
    			this.processorChain.stopProcessorChain(e);
	            this.closeDialog();
	            logger.warn("LiveV2ChatDialog connection error, closed this dialog.");
            } catch (FetionException e1) {
            	logger.warn("close LiveV2ChatDialog failed.", e1);
            }
    	}else{
    		//记录这个错误
        	logger.warn("LiveV2ChatDialog exception caught, just ignore it..", e);
    	}
    	
    	//如果是系统异常，报告这个错误
    	if(e instanceof SystemException) {
    		CrushBuilder.handleCrushReport(e, ((SystemException) e).getArgs());
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.Dialog#getMessageFactory()
     */
    @Override
    public MessageFactory getMessageFactory()
    {
	    return this.messageFactory;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.MutipartyDialog#buddyEntered(net.solosky.maplefetion.bean.Buddy)
     */
    @Override
    public void buddyEntered(Buddy buddy)
    {
		this.buddyEnterHelper.buddyEntered(buddy);
		this.buddyList.add(buddy);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.dialog.MutipartyDialog#buddyLeft(net.solosky.maplefetion.bean.Buddy)
     */
    @Override
    public void buddyLeft(Buddy buddy)
    {
    	//如果是当前对话框的所有者离开，关闭这个对话框
    	if(buddy.getUri().equals(this.mainBuddy.getUri())) {
	            this.closeDialog();
    	}else {		//移除对应离开的好友
    		Iterator<Buddy> it = this.buddyList.iterator();
	    	while(it.hasNext()) {
	    		Buddy b = it.next();
	    		if(b.getUri().equals(buddy.getUri())) {
	    			it.remove();
	    			break;
	    		}
	    	}
    	}
    }
    
	@Override
	public void buddyFailed(Buddy buddy) {
		//如果是当前对话框的所有者离开，关闭这个对话框
    	if(buddy.getUri().equals(this.mainBuddy.getUri())) {
	            this.closeDialog();
    	}
	}
    
    public String toString()
    {
    	return "[LiveV2ChatDialog - " +
    			"MainBuddy="+mainBuddy.getDisplayName()+", "+mainBuddy.getUri()+" ]";
    }
}
