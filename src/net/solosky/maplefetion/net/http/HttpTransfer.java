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
 * Package  : net.solosky.maplefetion.net.http
 * File     : HttpTransfer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-4-16
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.solosky.maplefetion.net.AbstractTransfer;
import net.solosky.maplefetion.net.TransferException;
import net.solosky.maplefetion.net.buffer.ByteArrayWriter;
import net.solosky.maplefetion.net.buffer.ByteWriter;

/**
 *
 * HTTP方式传输信令
 *
 * @author solosky <solosky772@qq.com>
 */
public class HttpTransfer extends AbstractTransfer
{
	private String ssic;
	private String pragma;
	private Thread runThead;
	private int requestId;
	private volatile boolean closeFlag;
	private String url;
	private BlockingQueue<BytesEntry> bytesEntryQueue;
	
	public static final int TYPE_INIT = 0;
	public static final int TYPE_ALIVE = 1;
	public static final int TYPE_DATA = 2;

	
	
	public HttpTransfer(String url,String ssic, String pragma)
	{
		this.ssic = ssic;
		this.url  = url; 
		this.pragma = pragma;
		this.requestId = 1;
		this.bytesEntryQueue = new LinkedBlockingQueue<BytesEntry>();
	}
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.AbstractTransfer#sendBytes(byte[], int, int)
     */
    @Override
    protected void sendBytes(byte[] buff, int offset, int len)
            throws TransferException
    {
    	this.bytesEntryQueue.add(new BytesEntry(buff, offset, len));
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.Transfer#startTransfer()
     */
    @Override
    public void startTransfer() throws TransferException
    {
	    Runnable r = new Runnable(){
            public void run()
            {
            	try{
                	ByteWriter writer =  new ByteArrayWriter();
                	//最开始执行了一个i请求
                	writer.write("SIPP".getBytes());
                	if(!tryExecuteRequest("i", requestId++, writer, 1))
                		raiseException(new TransferException("Init Http Transfer failed.."));
                	
                	
                	while(!closeFlag) {
                		writer.clear();
                		BytesEntry entry = bytesEntryQueue.poll(5,TimeUnit.SECONDS);		//等待五秒,如果没有元素也返回
                		if(entry!=null) {
                			writer.writeBytes(entry.getBytes(), entry.getOffset(), entry.getLength());	//
                			while(bytesEntryQueue.size()>0) {
                				entry = bytesEntryQueue.poll();
                				writer.writeBytes(entry.getBytes(), entry.getOffset(), entry.getLength());
                			}
                		}
                		writer.write("SIPP".getBytes());
                		
                		//尝试发送这个请求，如果超过指定次数，传递传输异常
                		if(!tryExecuteRequest("s",requestId++,writer, 3)) {
                			closeFlag = true;
                			raiseException( new TransferException());
                        }
                	}
                	
                	
                	//结束
                	writer.clear();
                	writer.write("SIPP".getBytes());
                	tryExecuteRequest("d", requestId++, writer, 1);
                	
            	}catch(Throwable e) {
            		raiseException(new TransferException(e));
            	}
            }
	    };
	    
	    this.runThead = new Thread(r);
	    this.runThead.setName(this.getTransferName());
	    this.runThead.start();
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.Transfer#stopTransfer()
     */
    @Override
    public void stopTransfer() throws TransferException
    {
	    this.closeFlag = true;
    }
   
    
    /**
     * 尝试发起请求，如果失败继续尝试，直到超过指定的尝试次数为止
     * @param writer
     * @param maxRetryTimes
     * @return 执行成功返回true或尝试超过指定次数返回false
     */
    private boolean tryExecuteRequest(String s, int i,ByteWriter writer, int maxRetryTimes)
    {
    	int nowRetryTimes = 0;
    	while(nowRetryTimes<maxRetryTimes) {
    		try {
	            this.executeRequest(s, i ,writer);
	            return true;			//如果没有出现错误就退出重试循环
            } catch (IOException e) {
            	nowRetryTimes++;	//出现错误继续发起请求
            }
    	}
    	return nowRetryTimes<maxRetryTimes;

    }
    
    /**
     * 执行一个请求
     * @param s
     * @param i
     * @param writer
     * @throws IOException
     */
    private void executeRequest(String s, int i,ByteWriter writer) throws IOException
    {
    	String turl = this.url+"?";
    	turl += "t="+s;
    	turl += "&i="+ Integer.toString(i);
    	
    	URL realURL = new URL(turl);
    	HttpURLConnection connection = (HttpURLConnection) realURL.openConnection();
    	connection.setRequestMethod("POST");
    	connection.setRequestProperty("User-Agent", "IIC2.0/PC 3.5.2540");
    	connection.setRequestProperty("Pragma", this.pragma);
    	connection.setRequestProperty("Content-Type", "application/oct-stream");
    	connection.setRequestProperty("Accept", "*/*");
    	connection.setRequestProperty("Cookie", "ssic="+this.ssic);
    	connection.setDoOutput(true);
    	OutputStream out = connection.getOutputStream();
    	out.write(writer.toByteArray(), 0, writer.size());
    	out.flush();
    	
    	if(connection.getResponseCode()==200) {
        	int contentLength = connection.getContentLength();
        	if(contentLength>4) {
            	InputStream in = connection.getInputStream();
            	writer.clear();
            	while(contentLength>4){
            		writer.writeByte(in.read());
            		contentLength--;
            	}
            	this.bytesRecived(writer.toByteArray(), 0, writer.size());
        	}
    	}else {
    		throw new IOException("Invalid response stateCode="+connection.getResponseCode());
    	}
    }
    
    private class BytesEntry
    {
    	private byte[] bytes;
    	private int offset;
    	private int length;
    	public BytesEntry(byte[] bytes, int offset, int length)
    	{
    		this.bytes = bytes;
    		this.offset = offset;
    		this.length = length;
    	}
    	public byte[] getBytes()
    	{
    		return this.bytes;
    	}
    	public int getOffset()
    	{
    		return this.offset;
    	}
    	public int getLength()
    	{
    		return this.length;
    	}
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.Transfer#getTransferName()
     */
    @Override
    public String getTransferName()
    {
    	return "HttpTransfer - "+this.url;
    }

}
