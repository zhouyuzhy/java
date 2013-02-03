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
 * Package  : net.solosky.net.maplefetion.net.tcp
 * File     : TcpTransfer.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-6
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.net.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.solosky.maplefetion.net.AbstractTransfer;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.net.TransferException;

import org.apache.log4j.Logger;


/**
 * 
 * TCP方式消息传输
 * 
 * @author solosky <solosky772@qq.com>
 */
public class TcpTransfer extends AbstractTransfer
{

	/**
	 * 内部线程，用于读取数据
	 */
	private Thread readThread;

	/**
	 * SOCKET
	 */
	private Socket socket;
	/**
	 * 读取对象
	 */
	private InputStream reader;

	/**
	 * 发送对象
	 */
	private OutputStream writer;

	/**
	 * 日志记录
	 */
	private static Logger logger = Logger.getLogger(TcpTransfer.class);

	/**
	 * 关闭标志
	 */
	private volatile boolean userCloseFlag;

	/**
	 * 构造函数
	 * 
	 */
	public TcpTransfer(Port port) throws TransferException
	{
		try {
			
			socket = new Socket();
			
			logger.debug("Connecting to "+port.toString()+"....");
			socket.connect(new InetSocketAddress(port.getAddress(), port.getPort()));
			logger.debug("Connected to "+port.toString()+".");
			
			reader = socket.getInputStream();
	        writer = socket.getOutputStream();
        } catch (IOException e) {
        	logger.warn("Cannot connect to "+port.toString()+"!!!!");
        	throw new TransferException(e);
        }
		userCloseFlag = false;
	}

	@Override
	public void startTransfer() throws TransferException
	{
		Runnable readRunner = new Runnable()
		{
			public void run()
			{
				try {
					logger.debug("Socket ready for read.. "+ socket);
					logger.debug("Local port:"+socket.getLocalSocketAddress().toString());
					
					//初始化缓冲区和一些变量
					byte[] buff = new byte[1024];
					int len   = 0; 
					
					//循环读取数据，直到遇到流的末尾，或者用户主动关闭连接
					//注意这里不能在stopTransfer里只改变关闭标志而不关闭流，否则会在reader.read()方法阻塞掉，即是中断线程也没用
					//线程在阻塞读取数据时是不能中断的，只能强制关闭流，阻塞的reader.read()方法就会立即抛出IO异常，就可以结束读线程
					while(true) {
						len = reader.read(buff, 0, buff.length);
						//这里还需要判断是否读取到流的末尾，否则会进入死循环，通常返回-1表示流的结尾
						//如果读取到结尾，表明是服务器主动关闭了连接，抛出这个异常，通知上层做资源清理工作
						if(len==-1)	{
							logger.debug("Connection closed by server.."+socket);
							raiseException(new TransferException("Connection closed by server. "+socket));
							break;
						}else {
							bytesRecived(buff, 0, len);
						}
					}
				} catch (IOException e) {
					//当发生IO异常时可能是网络出现问题，也可能是用户关闭了连接，这里需要判断下
					if(!userCloseFlag) {
						logger.warn("Connection error.. "+socket);
						raiseException(new TransferException(e));
					}else {
						logger.debug("Connection closed by user.."+socket);
					}
				}
				
				
				//程序执行到这里，表明流已经读取完毕，可能是服务器主动关闭了连接，或者用户关闭了连接
				//如果是服务器关闭了连接，这里就需要关闭流，如果是用户关闭了流，就什么也不做，因为在stopTranfer方法中就已经关闭了
				if(!userCloseFlag) {
					userCloseFlag = true;
    				try {
	                    writer.close();
	                    reader.close();
                    } catch (IOException e) {
                    	logger.warn("Close socket stream failed.. "+socket, e);
                    }
				}
			}
		};

		readThread = new Thread(readRunner);
		readThread.setName(getTransferName());

		readThread.start();

	}

	@Override
	public void stopTransfer() throws TransferException
	{
		//这里设置一个关闭标志，并关闭流，读线程就会抛出IO异常，然后判断关闭标志结束线程
		if(!this.userCloseFlag) {
    		this.userCloseFlag = true;
    		try {
	            this.writer.close();
	            this.reader.close();
            } catch (IOException e) {
            	logger.warn("Close socket stream failed.."+socket , e);
            }
		}
	}

	@Override
	public String getTransferName()
	{
		return "TCPTransfer-" + socket;
	}
	
	

	/**
     * @return the socket
     */
    public Socket getSocket()
    {
    	return socket;
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.net.AbstractTransfer#sendBytes(byte[], int, int)
     */
    @Override
    protected void sendBytes(byte[] buff, int offset, int len)
            throws TransferException
    {
    	try {
	        this.writer.write(buff, offset, len);
	        this.writer.flush();
        } catch (IOException e) {
	       throw new TransferException(e);
        }
    	
    }
}