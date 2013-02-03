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
 * File     : ProcessorChain.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.chain;

import java.util.Iterator;

import net.solosky.maplefetion.FetionException;

/**
 *
 * 处理链
 * 处理链就是把每一个处理构造成一个双向的链表，然后对象可以在这个链中逐个从前到后处理并传递，或者从后到前处理并传递
 *2[]
 * @author solosky <solosky772@qq.com>
 */
public class ProcessorChain
{
	/**
	 * 处理链头部
	 */
	private Processor headProcossor;
	
	/**
	 * 处理链是否关闭
	 */
	private volatile boolean isChainClosed;
	
	/**
	 * 构造函数
	 */
	public ProcessorChain()
	{
		this.isChainClosed = false;
	}
	
	/**
	 * 在头部添加一个处理器
	 * @param processor
	 */
	public void addFirst(Processor processor)
	{
		if(this.headProcossor==null) {
			this.headProcossor = processor;
		}else {
			this.headProcossor.setPreviousProcessor(processor);
			processor.setNextProcessor(this.headProcossor);
			this.headProcossor = processor;
		}
	}
	
	/**
	 * 在尾部添加一个处理器
	 * @param processor
	 */
	public void addLast(Processor processor)
	{
		if(this.headProcossor==null) {
			this.headProcossor = processor;
		}else {
			Processor last = this.getLast();
			processor.setPreviousProcessor(last);
			last.setNextProcessor(processor);
		}
	}
	
	
	/**
	 * 在制定的名字后面的处理器后添加处理器
	 * @param name
	 * @param curProcessor
	 */
	public void addAfter(String name,Processor curProcessor)
	{
		Processor preProcessor = this.getProcessor(name);
		if(preProcessor!=null) {
			Processor nextProcessor = preProcessor.getNextProcessor();
			
			preProcessor.setNextProcessor(curProcessor);
			curProcessor.setPreviousProcessor(preProcessor);
			
			if(nextProcessor!=null) {
				nextProcessor.setPreviousProcessor(curProcessor);
				curProcessor.setNextProcessor(nextProcessor);
			}
		}
	}
	
	
	/**
	 * 删除指定名字的处理器
	 * @param name
	 */
	public void remove(String name)
	{
		Iterator<Processor> it = this.iterator();
		Processor processor = null;
		while(it.hasNext()) {
			processor = it.next();
			if(processor.getProcessorName().equals(name)) {
				it.remove();
				break;
			}
		}
	}
	
	
	/**
	 * 删除处理器，修复处理链
	 * @param processor
	 */
	private void removeProcessor(Processor processor)
	{
		if(processor==null)		return;
		Processor preProcessor  = processor.getPreviousProcessor();
		Processor nextProcessor = processor.getNextProcessor();
		
		if(preProcessor!=null) {
			preProcessor.setNextProcessor(nextProcessor);
		}
		if(nextProcessor!=null) {
			nextProcessor.setPreviousProcessor(preProcessor);
		}
	}
	
	/**
	 * 返回最后一个处理器
	 * @return
	 */
	public Processor getLast()
	{
		Processor processor = this.headProcossor;
		while(processor.getNextProcessor()!=null) {
			processor = processor.getNextProcessor();
		}
		return processor;
	}
	
	/**
	 * 返回第一个处理器
	 */
	public Processor getFirst()
	{
		return this.headProcossor;
	}
	
	/**
	 * 启动处理链
	 * @throws FetionException
	 */
	public void startProcessorChain() throws FetionException
	{
		Iterator<Processor> it = this.iterator();
		while(it.hasNext()) {
			it.next().startProcessor();
		}
	}
	
	
	/**
	 * 停止处理链
	 * @throws FetionException
	 */
	public void stopProcessorChain() throws FetionException
	{
		if(!this.isChainClosed) {
    		this.isChainClosed = true;
    		Iterator<Processor> it = this.iterator();
    		while(it.hasNext()) {
    			it.next().stopProcessor();
    			}
    		}
	}
	
	/**
	 * 停止处理链
	 * @throws FetionException
	 */
	public void stopProcessorChain(FetionException ex) throws FetionException
	{
		if(!this.isChainClosed) {
    		this.isChainClosed = true;
    		Iterator<Processor> it = this.iterator();
    		while(it.hasNext()) {
    			it.next().stopProcessor(ex);
    			}
    		}
	}
	
	/**
	 * 返回指定名字的处理器
	 * @param name
	 * @return
	 */
	public Processor getProcessor(String name)
	{
		Iterator<Processor> it = this.iterator();
		Processor processor = null;
		while(it.hasNext()) {
			processor = it.next();
			if(processor.getProcessorName().equals(name)) {
				return processor;
			}
		}
		return null;
	}
	
	
	/**
	 * 返回处理链的迭代器
	 * @return
	 */
	public Iterator<Processor> iterator()
	{
		return new ProcessorIterator(this.headProcossor);
	}
	
	/**
     * @return the isChainClosed
     */
    public boolean isChainClosed()
    {
    	return isChainClosed;
    }

	/**
	 * 
	 * 内部类，实现了迭代器，方便迭代
	 *
	 * @author solosky <solosky772@qq.com>
	 */
	private class ProcessorIterator implements Iterator<Processor>
	{
		private Processor curProcessor;
		private boolean isFirst;
		public ProcessorIterator(Processor headProcessor) {
			curProcessor = headProcessor;
			isFirst = true;
		}
        @Override
        public boolean hasNext()
        {
	        return curProcessor.getNextProcessor()!=null;
        }
        @Override
        public Processor next()
        {
        	if(isFirst) {
        		isFirst = false;
        		return curProcessor;
        	}else {
        		return curProcessor = curProcessor.getNextProcessor();
        	}
        }
        @Override
        public void remove()
        {
        	removeProcessor(curProcessor);
        }
	}
}
