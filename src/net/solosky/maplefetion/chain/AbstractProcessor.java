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
 * Package  : net.solosky.net.maplefetion.chain
 * File     : AbstractProcessor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.chain;

import net.solosky.maplefetion.FetionException;

/**
 *
 * 这是一个抽象的处理器
 * 完成了设置和返回前后处理方法，可以继承自这个类，然后添加具体的处理方法
 *
 * @author solosky <solosky772@qq.com>
 */
public abstract class AbstractProcessor implements Processor
{

	/**
	 * 前一个处理器
	 */
	protected Processor previousProcessor;
	
	/**
	 * 后一个处理器
	 */
	protected Processor nextProcessor;
	
	
	/**
	 * 子类处理前面转发的对象
	 * @param o		对象
	 * @return		返回处理后的对象，如果为null,停止处理链
	 * @throws FetionException
	 */
	protected Object doProcessIncoming(Object o) throws FetionException
	{
		return o;
	}
	
	/**
	 * 子类处理后面转发的对象
	 * @param o		对象
	 * @return		返回处理后的对象，如果为null,停止处理链
	 * @throws FetionException
	 */
	protected Object doProcessOutcoming(Object o) throws FetionException
	{
		return o;
	}
	
	
	
	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Processor#raiseException(net.solosky.net.maplefetion.FetionException)
     */
    @Override
    public void raiseException(FetionException e)
    {
    	if(this.previousProcessor!=null) {
    		this.previousProcessor.raiseException(e);
    	}
    		
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Proccessor#getBackwardProccessor()
     */
    @Override
    public Processor getNextProcessor()
    {
	    return this.nextProcessor;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Proccessor#getForwardProccessor()
     */
    @Override
    public Processor getPreviousProcessor()
    {
    	return this.previousProcessor;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Proccessor#setBackwardProccessor(net.solosky.net.maplefetion.chain.Proccessor)
     */
    @Override
    public void setNextProcessor(Processor processor)
    {
    	this.nextProcessor = processor;
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Proccessor#setForwardProccessor(net.solosky.net.maplefetion.chain.Proccessor)
     */
    @Override
    public void setPreviousProcessor(Processor processor)
    {
    	this.previousProcessor = processor;
    }

    
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.Processor#startProcessor()
     */
    @Override
    public void startProcessor() throws FetionException
    {
	    //不做任何事情，子类可以覆盖
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.Processor#stopProcessor()
     */
    @Override
    public void stopProcessor() throws FetionException
    {
    	this.stopProcessor(null);
    }
    
	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.chain.Processor#stopProcessor(net.solosky.maplefetion.FetionException)
	 */
	@Override
	public void stopProcessor(FetionException exception) throws FetionException
	{
		//DONothing
	}

	/**
	 * 这个方法实现了处理前面转发过来的对象，具体的交给子类去完成
	 * 子类返回处理后的对象，如果返回null,则终止处理链
	 */
    @Override
    public void processIncoming(Object o) throws FetionException
    {
    	Object ret = this.doProcessIncoming(o);
	    if(ret!=null && this.previousProcessor!=null)
	    	this.previousProcessor.processIncoming(ret);
    }

    /**
	 * 这个方法实现了处理后面转发过来的对象，具体的交给子类去完成
	 * 子类返回处理后的对象，如果返回null,则终止处理链
	 */
    @Override
    public void processOutcoming(Object o) throws FetionException
    {
    	Object ret = this.doProcessOutcoming(o);
    	if(ret!=null && this.nextProcessor!=null)
    		this.nextProcessor.processOutcoming(ret);
    }
}
