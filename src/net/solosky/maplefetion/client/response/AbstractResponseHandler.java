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
 * Package  : net.solosky.maplefetion.client.response
 * File     : AbstractResponseHandler.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-18
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.NotifyEventListener;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.HttpApplication;
import net.solosky.maplefetion.client.ResponseHandler;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.event.action.SuccessEvent;
import net.solosky.maplefetion.event.action.SystemErrorEvent;
import net.solosky.maplefetion.event.action.TimeoutEvent;
import net.solosky.maplefetion.event.action.TransferErrorEvent;
import net.solosky.maplefetion.event.action.failure.RequestFailureEvent;
import net.solosky.maplefetion.event.action.failure.SipcFailureEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.sipc.SipcStatus;
import net.solosky.maplefetion.util.XMLHelper;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * 所有回复处理器的基类
 * 
 * 提供了回复的处理的模板，根据不同回复状态来回调不同的处理函数
 * 子类可以重载自己所需要处理的状态回调来完成不同的处理逻辑
 * 默认1xx，2xx的回复状态返回的是成功事件
 * 4xx，5xx的回复返回的是失败事件
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class AbstractResponseHandler implements ResponseHandler
{
	//客户端
	protected FetionContext context;
	//对话框
	protected Dialog dialog;
	//监听器
	protected ActionEventListener listener;
	//logger
	protected static Logger logger = Logger.getLogger(ResponseHandler.class);
	
	/**
	 * 构造函数
	 * @param client
	 * @param dialog
	 * @param listener
	 */
	public AbstractResponseHandler(FetionContext context, Dialog dialog, ActionEventListener listener)
	{
		this.context = context;
		this.dialog = dialog;
		this.listener = listener;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.ResponseHandler#handle(net.solosky.maplefetion.sipc.SipcResponse)
     */
    @Override
    public void handle(SipcResponse response) throws FetionException
    {
    	try {
    		//交给子类处理这个回复，并捕获所有异常
    		ActionEvent event = this.doHandle(response);
    		if(event!=null) {
    			this.fireEvent(event);
    		}
		} catch (FetionException e) {
			this.fireEvent(new SystemErrorEvent(e));
			throw e;							//重新抛出已知异常
		} catch(Throwable t){
			this.fireEvent(new SystemErrorEvent(t));
			throw new SystemException(t, response.getRequest(), response);		//包装下未知异常为系统异常，重新抛出
		}
    }

    
    /**
     * 超时错误
     */
    public void timeout(SipcRequest request)
    {
		this.fireEvent(new TimeoutEvent());
    }
    
    
    /**
     * 发生了网络错误
     */
    public void ioerror(SipcRequest request)
    {
    	this.fireEvent(new TransferErrorEvent());
    }
    
    
    
    /* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.ResponseHandler#syserror(net.solosky.maplefetion.sipc.SipcRequest)
	 */
	@Override
	public void syserror(SipcRequest request, Throwable t)
	{
		this.fireEvent(new SystemErrorEvent(t));
	}

	/**
     * 触发操作事件
     */
    private void fireEvent(ActionEvent event)
    {
    	//用户设置的回调函数调用时可能会出现异常，这里捕获掉所有的异常并记录，防止异常传递到处理链而引起客户端退出
    	if(this.listener!=null){
	    	try {
					this.listener.fireEevent(event);
			} catch (Throwable e) {		
				logger.warn("FireActionEvent error.", e );
			}
    }
    }
    /**
     * 处理这个回复，根据不同的状态回调不同的方法，子类可以重载这个
     * @param response	回复对象
     * @return 处理后会产生一个结果事件
     * @throws FetionException
     */
    protected ActionEvent doHandle(SipcResponse response) throws FetionException
    {
    	switch(response.getStatusCode()){
    		case SipcStatus.TRYING:			    return this.doTrying(response);
    		case SipcStatus.ACTION_OK:          return this.doActionOK(response);
    		case SipcStatus.SEND_SMS_OK:        return this.doSendSMSOK(response);
    		case SipcStatus.NOT_AUTHORIZED:     return this.doNotAuthorized(response);
    		case SipcStatus.NOT_FOUND:          return this.doNotFound(response);
    		case SipcStatus.EXTENSION_REQUIRED:	return this.doExtensionRequired(response);
    		case SipcStatus.BAD_EXTENSION:		return this.doBadExtension(response);
    		case SipcStatus.TA_EXIST:           return this.doTaExsit(response);
    		case SipcStatus.NO_SUBSCRIPTION:    return this.doNoSubscription(response);
    		case SipcStatus.TIME_OUT:           return this.doTimeout(response);
    		case SipcStatus.BUSY_HERE:          return this.doBusyHere(response);
    		case SipcStatus.FORBIDDEN:          return this.doForbidden(response);
    		case SipcStatus.REQUEST_FAILURE:    return this.doRequestFailure(response);
    		case SipcStatus.REQUEST_FAILUREV4:	return this.doRequestFailureV4(response);
    		case SipcStatus.SERVER_UNAVAILABLE: return this.doServerUnavaliable(response);
    		
    		default:	
				logger.warn("Unhandled sipc response status, default make action fail. status="
						+response.getStatusCode()+", response="+response);
				return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    	}
    }

	//100
    protected ActionEvent doTrying(SipcResponse response) throws FetionException {
    	return new SuccessEvent();
    }
    
    //200
    protected ActionEvent doActionOK(SipcResponse response) throws FetionException{
    	return new SuccessEvent();
    }
    
    //208
    protected ActionEvent doSendSMSOK(SipcResponse response) throws FetionException{
    	return new SuccessEvent();
    }
    
    //401
    protected ActionEvent doNotAuthorized(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //403
    protected ActionEvent doForbidden(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //404
    protected ActionEvent doNotFound(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //420
    protected ActionEvent doBadExtension(SipcResponse response) throws FetionException{
    	
    	//解析提示信息
    	Element root = XMLHelper.build(response.getBody().toSendString());
		Element reason = root.getChild("reason");
		String text = reason.getAttributeValue("text");	//原因
		String tips = reason.getAttributeValue("tips");	//一些提示
		
		//解析一些获取验证图片的参数
		SipcHeader wwwHeader = response.getHeader(SipcHeader.WWWAUTHENTICATE);
		Pattern pt = Pattern.compile("Verify algorithm=\"(.*?)\",type=\"(.*?)\"");
		Matcher mc = pt.matcher(wwwHeader.getValue());
		if(mc.matches()) {
			String alg  = mc.group(1);		//验证图片算法
			String type = mc.group(2);		//类型？？？啥意思。。。
			VerifyImage verifyImage = null;
            try {
	            verifyImage = HttpApplication.fetchVerifyImage(context.getFetionUser(),
	            			context.getLocaleSetting(), alg, type);
            } catch (IOException e) {
            	throw new IllegalStateException("fetch verify image failed.", e);
            }
			
            //通知设置的通知监听器处理这个验证码事件，如果没有设置监听器，将抛出异常
			NotifyEventListener listener = this.context.getNotifyEventListener();
			if(listener!=null) {
				listener.fireEvent(new ImageVerifyEvent(ImageVerifyEvent.SIPC_VERIFY, 
						verifyImage, text, tips, response.getRequest(),
						this.dialog, this.listener));
			}else{
				throw new IllegalArgumentException("action need verify, but found no NotifyEventListener" +
    			" to handle verify action, please set NotifyEventListener first.");
			}
		}else {
			throw new IllegalStateException("parse verify info failed. wwwHeader="+wwwHeader.getValue());
		}
		
    	return null;	//返回null，表明处理这个回复事件
    }

	//421
    protected ActionEvent doExtensionRequired(SipcResponse response) throws FetionException{
    	return this.doBadExtension(response);
    }
    
    //444
    protected ActionEvent doRequestFailureV4(SipcResponse response){
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //486
    protected ActionEvent doBusyHere(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
	}
    
    //494
	protected ActionEvent doRequestFailure(SipcResponse response) throws FetionException {
		if(response.getBody()!=null){
			Element root = XMLHelper.build(response.getBody().toSendString());
			Element reason = root.getChild("reason");
			if(reason!=null){
				return new RequestFailureEvent(FailureType.REQEUST_FAIL,
						reason.getAttributeValue("text"),
						reason.getAttributeValue("refer-url"));
			}else{
				return new RequestFailureEvent(FailureType.REQEUST_FAIL, null, null);
			}
		}else{
			return new RequestFailureEvent(FailureType.REQEUST_FAIL, null, null);
		}
	}
    
    //521
    protected ActionEvent doTaExsit(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //522
    protected ActionEvent doNoSubscription(SipcResponse response) throws FetionException{
    	return new SipcFailureEvent(FailureType.SIPC_FAIL, response);
    }
    
    //504
    protected ActionEvent doTimeout(SipcResponse response) throws FetionException{
    	return new TimeoutEvent();
    }
    
    //503
	private ActionEvent doServerUnavaliable(SipcResponse response) {
		return new FailureEvent(FailureType.SERVER_BUSY);
	}
}
