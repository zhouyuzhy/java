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
 * Package  : net.solosky.net.maplefetion.util
 * File     : SipcLogger.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-8
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.chain.AbstractProcessor;
import net.solosky.maplefetion.chain.ProcessorException;
import net.solosky.maplefetion.sipc.SipcInMessage;
import net.solosky.maplefetion.sipc.SipcOutMessage;

import org.apache.log4j.Logger;

/**
*
*	SIP信令记录器
*
* @author solosky <solosky772@qq.com> 
*/
public class SipcLogger extends AbstractProcessor
{
	private String name;
	private BufferedWriter writer;
	private boolean enableLogging;
	private boolean isClosed;
	private static Logger logger = Logger.getLogger(SipcLogger.class);
	/**
	 * 构造函数
	 * @param fileName
	 */
	public SipcLogger(String name)
	{
		this.name = name;
		this.isClosed = false;
		enableLogging = FetionConfig.getBoolean("log.sipc.enable");
		if(enableLogging){
			File logFile = new File(FetionConfig.getString("log.sipc.dir"),name+".log");
			try {
				writer = new BufferedWriter(new FileWriter(logFile));
			}catch (IOException e) {
				logger.warn("Cannot create SIPMessage log file:"+logFile.getAbsolutePath());
			}
		}
	}
	
	/**
	 * 记录收到的信令
	 * @param in
	 * @throws IOException 
	 */
	public void logMessage(SipcInMessage in) throws IOException
	{
		if(!enableLogging || writer==null)
			return;
		
		writer.append("\r\n--------------------------------------------\r\n");
		writer.append("Received<<<<<<<<"+ (new Date()).toString()+"\r\n");
		writer.append("--------------------------------------------\r\n");
		writer.append(in.toSendString());
		writer.flush();
	}
	
	/**
	 * 记录发出包
	 * @param out
	 * @throws IOException 
	 */
	public void logMessage(SipcOutMessage out) throws IOException
	{
		if(!enableLogging || writer==null)
			return;
		writer.append("\r\n--------------------------------------------\r\n");
		writer.append("Send>>>>>>>>>>>>"+ (new Date()).toString()+"\r\n");
		writer.append("--------------------------------------------\r\n");
		writer.append(out.toSendString());
		writer.flush();
	}
	
	/**
	 * 关闭记录器
	 * @throws IOException 
	 */
	public void close() throws IOException
	{
		if(!enableLogging || !isClosed || writer==null)
			return;
		else {
			writer.close();
		}
	}
	
	/**
	 * 返回记录器的明智
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 工厂方法
	 * @param name
	 * @return
	 */
	public static SipcLogger create(String name)
	{
		return new SipcLogger(name);
	}
	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.Processor#getProcessorName()
     */
    @Override
    public String getProcessorName()
    {
	    return "SipcLogger";
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#doProcessIncoming(java.lang.Object)
     */
    @Override
    protected Object doProcessIncoming(Object o) throws FetionException
    {
	    try {
	        this.logMessage((SipcInMessage) o);
        } catch (IOException e) {
	      throw new ProcessorException(e);
        }
        return super.doProcessIncoming(o);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.chain.AbstractProcessor#doProcessOutcoming(java.lang.Object)
     */
    @Override
    protected Object doProcessOutcoming(Object o) throws FetionException
    {
    	 try {
 	        this.logMessage((SipcOutMessage) o);
         } catch (IOException e) {
        	 throw new ProcessorException(e);
         }
         return super.doProcessOutcoming(o);
    }

}
