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
 * File     : Processor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.chain;

import net.solosky.maplefetion.FetionException;


/**
 *
 *  处理接口
 *  
 *  通过这个接口可以构建起一个双向的处理链
 *  一个对象可以交个前一个处理器往前传送处理，也可以交给后一个接口往后处理。
 *  每一个处理器都简单的处理对象后，就交给下一个处理器
 *  如果是前面传递过来的，处理完就交给后面的处理器
 *  如果是后面传递过来的，处理完后就交给前一个处理器
 *
 * @author solosky <solosky772@qq.com>
 */
public interface Processor
{
	/**
	 * 处理从前面转发过来的对象
	 * @param o
	 * @throws FetionException
	 */
	public void processIncoming(Object o) throws FetionException;
	
	/**
	 * 处理从后面转发过来的对象
	 * @param o
	 * @throws FetionException
	 */
	public void processOutcoming(Object o) throws FetionException;
	
	/**
	 * 设置前一个处理器
	 * @param proccessor
	 */
	public void setPreviousProcessor(Processor processor);
	
	/**
	 * 设置后一个处理器
	 * @param proccessor
	 */
	public void setNextProcessor(Processor processor);
	
	/**
	 * 返回前一个处理器
	 * @return
	 */
	public Processor getPreviousProcessor();
	
	/**
	 * 返回后一个处理器
	 * @return
	 */
	public Processor getNextProcessor();
	
	/**
	 * 返回处理器名字
	 */
	public String getProcessorName();
	
	/**
	 * 向上传递异常
	 */
	public void raiseException(FetionException e);
	
	/**
	 * 启动处理器
	 * @throws FetionException
	 */
	public void startProcessor() throws FetionException;
	
	/**
	 * 停止处理器
	 * @throws FetionException
	 */
	public void stopProcessor() throws FetionException;
	
	/**
	 * 以一个异常停止处理器，应该根据这个异常进行不同的判断
	 * @param exception
	 */
	public void stopProcessor(FetionException exception) throws FetionException;
}
