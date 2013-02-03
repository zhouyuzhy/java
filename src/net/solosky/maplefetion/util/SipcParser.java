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
 * Package  : net.solosky.net.maplefetion.client
 * File     : SipcParser.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-5
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.chain.AbstractProcessor;
import net.solosky.maplefetion.net.buffer.ByteArrayWriter;
import net.solosky.maplefetion.net.buffer.ByteReader;
import net.solosky.maplefetion.net.buffer.ByteWriter;
import net.solosky.maplefetion.sipc.SipcBody;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcInMessage;
import net.solosky.maplefetion.sipc.SipcMessage;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.sipc.SipcOutMessage;
import net.solosky.maplefetion.sipc.SipcResponse;

/**
 *
 * 协议解析器
 *
 * @author solosky <solosky772@qq.com>
 */
public class SipcParser extends AbstractProcessor
{
	
	/**
	 * 临时缓冲区
	 */
	private ByteWriter byteWriter;
	
	/**
	 * 字节缓冲区
	 */
	private ByteReader byteReader;
	
	/**
	 * 当前解码的消息
	 */
	private SipcInMessage curMessage;
		
	/**
	 * 消息体还需要读取的长度
	 */
	private int contentLeft;
	
	/**
	 * 读取一行时上一个字符
	 */
	private int lineLast;
	
	/**
	 * 上一次操作
	 */
	private int lastAction;
	
	/**
	 * 有效字符掩码
	 */
	private static final int BYTE_MASK = 0x7FFFFFFF;
	
	/**
	 * 操作常量
	 */
	private static final int ACTION_NONE 		= 0x00;
	private static final int ACTION_READ_RETURN 	= 0x01;
	private static final int ACTION_READ_HEADER 	= 0x02;
	private static final int ACTION_READ_CONTENT = 0x03;
	
	
	
	/**
	 * 默认构造函数
	 */
	public SipcParser()
	{
		this.byteWriter = new ByteArrayWriter();
		this.contentLeft = 0;
		this.lastAction = ACTION_NONE;
		this.lineLast = BYTE_MASK;
	}
	
	
	/**
	 * 把发出信令转换为字节写对象
	 * @return
	 */
	public ByteWriter parseOutMessage(SipcOutMessage out)
	{
		ByteWriter writer = new ByteArrayWriter();
		byte[] bytes = ConvertHelper.string2Byte(out.toSendString());
		writer.write(bytes);
		return writer;
		
	}
	
	/**
	 * 从buffer里解码ＳＩＰ信令对象
	 * @throws FetionException 
	 */
	public boolean parseInMessage() throws FetionException
	{
		switch (lastAction)
		{
		case ACTION_READ_RETURN:
		case ACTION_NONE:
			String headline = this.readLine();
			if (headline == null) {
				lastAction = ACTION_READ_RETURN;
				return false;
			}
			return this.readSipcMessage(headline);

		case ACTION_READ_HEADER:
			return this.readSipcHeaders() && this.readSipcBody();

		case ACTION_READ_CONTENT:
			return this.readSipcBody();

		default:
			return false;
		}
	}

    
    /**
     * 把当前对象刷新至下一个处理器
     * @throws FetionException 
     */
    private void flushInMessage() throws FetionException
    {
    	if(this.previousProcessor!=null)
    		this.previousProcessor.processIncoming(this.curMessage);
	 	this.curMessage = null;
	 	this.lastAction = ACTION_NONE;
	 	this.lineLast = BYTE_MASK;
    }
    
    /**
     * 把字节写入对象刷新至下一个处理器 ，一般是Transfer
     * @param writer
     * @throws FetionException
     */
    private void flushOutMessage(ByteWriter writer) throws FetionException
    {
    	if(this.nextProcessor!=null) {
    		this.nextProcessor.processOutcoming(writer);
    	}
    }
    
    /**
     * 读取SIP信令
     * @param buffer 	缓存对象
     * @param head		首行信息
     */
    private boolean readSipcMessage(String headline)
    {
    	if(headline.startsWith("SIP-C/")) {
    		this.curMessage = new SipcResponse(headline);	//SIP-C/2.0 200 OK
	 	}else {
	 		this.curMessage = new SipcNotify(headline);		//BN 685592830 SIP-C/2.0
	 	}

		if(this.readSipcHeaders()) {
			return this.readSipcBody();
		}else {
			return false;
		}
    }
    
    
    /**
     * 读取一行字符
     * @param buffer	缓存对象
     * @return			字符串不包含\r\n
     */
    public String readLine()
    {
    	int cur  = BYTE_MASK;
    	int last = BYTE_MASK;
    	boolean end = false;
    	if(lineLast==BYTE_MASK) {
    		byteWriter.clear();
        }else {
        	last = lineLast;
        }
    	
    	while(byteReader.hasRemaining()) {
    		cur = byteReader.readByte();
    		//0x0D 0x0A为行结束符
    		if(last==0x0D && cur==0x0A) {
    			end = true;
    			break;
    		}else if(last==BYTE_MASK) {
    			last = cur;
    		}else {
    			byteWriter.writeByte(last);
    			last = cur;
    		}
    	} 
    	if(end) {
    		lineLast = BYTE_MASK;
    		return ConvertHelper.byte2String(byteWriter.toByteArray());
    	}else {
    		lineLast = last;
    		return null;
    	}
    	
    }
    
    /**
     * 读取所有消息头
     * @return				如果读取完毕返回true否则返回false
     */
    private boolean readSipcHeaders()
    {
    	while(true) {
        	String headline = this.readLine();
        	if(headline==null) {
        		lastAction = ACTION_READ_HEADER;
        		return false;
        	}else if(headline.length()==0) {
        		lastAction = ACTION_NONE;
        		return true;
        	}else {
        		curMessage.addHeader(new SipcHeader(headline));
        	}
    	}
    }
    
    
    /**
     * 读取消息体
     * @return		如果读取完毕返回true否则返回false
     */
    private boolean readSipcBody()
    {
    	if(this.lastAction==ACTION_NONE) {
    		if(this.curMessage.getContentLength()>0) {
    			this.contentLeft = this.curMessage.getContentLength();
    			this.byteWriter.clear();
    		}else {
    			lastAction = ACTION_NONE;
    			return true;
    		}
    	}
    	
    	for(;byteReader.hasRemaining() && this.contentLeft>0; this.contentLeft--)
    		this.byteWriter.writeByte((byteReader.readByte()));
    	if(this.contentLeft==0) {
    		this.curMessage.setBody(new SipcBody( ConvertHelper.byte2String(this.byteWriter.toByteArray())));
    		this.lastAction = ACTION_NONE;
    		return true;
    	}else {
    		this.lastAction = ACTION_READ_CONTENT;
    		return false;
    	}
    }
	
    /* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.AbstractProcessor#processIncoming(java.lang.Object)
     */
    @Override
    public void processIncoming(Object o) throws FetionException
    {
    	if(o instanceof ByteReader) {
    		this.byteReader = (ByteReader) o;
    		while(this.byteReader.hasRemaining()) {
    			if(this.parseInMessage())
    				this.flushInMessage();
    		}
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.net.maplefetion.chain.AbstractProcessor#processOutcoming(java.lang.Object)
     */
    @Override
    public void processOutcoming(Object o) throws FetionException
    {
	  if(o instanceof SipcOutMessage) {
		  SipcOutMessage out = (SipcOutMessage) o;
		  this.flushOutMessage(this.parseOutMessage(out));
	  }
    }
    
	@Override
    public String getProcessorName()
    {
	    return "SipcParser";
    }

}
