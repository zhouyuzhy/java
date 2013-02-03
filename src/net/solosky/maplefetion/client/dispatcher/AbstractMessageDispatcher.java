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
 * Package  : net.solosky.maplefetion.client.dispatcher
 * File     : AbstractMessageDispatcher.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-13
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client.dispatcher;

import java.util.Hashtable;

import net.solosky.maplefetion.ExceptionHandler;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.chain.AbstractProcessor;
import net.solosky.maplefetion.client.NotifyHandler;
import net.solosky.maplefetion.client.ResponseHandler;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.sipc.SipcInMessage;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.sipc.SipcResponse;

import org.apache.log4j.Logger;


/**
 * 
 * 消息分发器，这个才是真正的处理消息
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class AbstractMessageDispatcher extends AbstractProcessor implements MessageDispatcher
{
	/**
	 * 日志记录
	 */
	protected static Logger logger = Logger.getLogger(AbstractMessageDispatcher.class);
	/**
	 * 通知处理器缓存
	 */
	private Hashtable<String, NotifyHandler> notifyHandlers;
	
	/**
	 * 飞信客户端
	 */
	private FetionContext context;
	
	/**
	 * 对话
	 */
	private Dialog dialog;
	/**
	 * 异常处理器
	 */
	private ExceptionHandler exceptionHandler;
	
	/**
	 * 默认构造函数
	 * @param client		飞信客户端对象
	 * @param dialog		当前duihk
	 */
	public AbstractMessageDispatcher(FetionContext client, Dialog dialog, ExceptionHandler exceptionHandler)
	{
		this.context = client;
		this.dialog = dialog;
		this.exceptionHandler = exceptionHandler;
		this.notifyHandlers = new Hashtable<String, NotifyHandler>();
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.Processor#getProcessorName()
     */
    @Override
    public String getProcessorName()
    {
    	return MessageDispatcher.class.getName();
    }
	
	/**
	 * 处理接收的包 循环所有的处理器，判断是否可以接受，如果可以就交给分发器分发
	 */
	@Override
	protected Object doProcessIncoming(Object o) throws FetionException
	{
		try {
    		SipcInMessage in = (SipcInMessage) o;
    		if (in instanceof SipcNotify) {
    			this.dispatch((SipcNotify) in);
    		} else if (in instanceof SipcResponse) {
    			this.dispatch((SipcResponse) in);
    		} else {
    			throw new DispatcherException("Unkown SipMessage type.."+in.getClass().getName());
    		}
		}catch(FetionException e) {
			this.exceptionHandler.handleException(e);
		}

		return super.doProcessIncoming(o);
	}

	/**
	 * 处理异常， 交给设置的异常处理器处理
	 */
	@Override
	public void raiseException(FetionException e)
	{
		this.exceptionHandler.handleException(e);
	}

	/**
	 * 分发回复 因为每一个回复都有一个对应的处理器，所以这里直接调用回复对应的处理器即可
	 * 
	 * @param response
	 * @throws FetionException 
	 * @throws FetionException
	 */
	public void dispatch(SipcResponse response) throws FetionException
	{
		SipcRequest request = response.getRequest();

		// 检查回复对应的请求
		if (request == null) {
			logger.warn("Request not found in response - response=" + response);
			return;
		}

		// 检查请求设置的处理器
		ResponseHandler handler = response.getRequest().getResponseHandler();
		
		if (handler == null) {
			logger.warn("ResponseHandler not found in request - response=" + response);
			return;
		}

		// 检查通过，处理这个回复
		try {
	        handler.handle(response);
        } catch (FetionException e) {
        	throw e;
        }catch(Throwable t) {
        	throw new SystemException(t, response.getRequest(), response);
        }
	}
    
    /**
     * 分发处理异步通知
     * @param notify
     * @throws FetionException 
     */
    public void dispatch(SipcNotify notify) throws FetionException
    {
    	//查找处理这个通知的类
    	String clazz = this.findNotifyHandlerClass(notify);
    	if(clazz==null) {
    		throw new DispatcherException("Cannot find a class to handle this notify - notify:\n"+notify.toSendString());
    	}
    	//加载这个类
    	NotifyHandler handler = loadNotifyHandler(clazz);
    	
    	//处理通知
    	if(handler!=null) {
    		try {
    	        handler.handle(notify);
            } catch (FetionException e) {
            	throw e;
            }catch(Throwable t) {
            	throw new SystemException(t,notify);
            }
    	}
    }
    
    
    /**
	 * 查找对象，如果没有找到就实例化
	 * 
	 * @param clazz
	 * @return
	 */
	private NotifyHandler loadNotifyHandler(String clazz) throws DispatcherException
	{
		NotifyHandler handler = null;
		handler = this.notifyHandlers.get(clazz);
		if (handler == null) {
			try {
				handler = (NotifyHandler) Class.forName(clazz)
				        .newInstance();
				handler.setDailog(dialog);
				handler.setContext(context);
			} catch (Exception e) {
				throw new DispatcherException(e);
			}
		}
		return handler;
	}
	

	/**
     * 查找处理这个通知的类
     * @param notify	通知
     * @return			类名
     */
    protected abstract String findNotifyHandlerClass(SipcNotify notify);
}
