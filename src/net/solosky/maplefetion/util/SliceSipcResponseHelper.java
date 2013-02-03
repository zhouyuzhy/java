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
 * Package  : net.solosky.maplefetion.util
 * File     : SliceSipcResponseHelper.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-6-10
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import net.solosky.maplefetion.sipc.SipcBody;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.sipc.SipcStatus;

/**
 *
 * 分块回复消息工具类
 * 
 * 用于把分块的消息组装为一整个消息
 * 如果一个的消息的消息体很大，服务器就会分块传输
 * 一般说来，一个分块的消息体是有多个消息状态为188 Partial和一个消息状态不为188的消息组成，所有的消息的I和Q都相同
 * 通常消息状态不为188的消息是最后发送的，可以使用这个来判断是否已经收到了所有的分块
 * 而且每个消息的偏移一般都是先后到达的，也就是说偏移在前的先到，偏移在后的后到，因为飞信服务器是基于流传输，而不是像QQ基于块传输的
 * @author solosky <solosky772@qq.com>
 *
 */
public class SliceSipcResponseHelper
{
	/**
	 * 所有的部分消息列表
	 */
	private ArrayList<SipcResponse> sliceReponseList;
	
	/**
	 * 分块回复的CallId
	 */
	private int callid;
	
	/**
	 * 分块回复的Sequence
	 */
	private String sequence;
	
	/**
	 * 以第一个分块回复构造
	 * @param first  第一个分块回复
	 */
	public SliceSipcResponseHelper(SipcResponse first)
	{
		this.sliceReponseList = new ArrayList<SipcResponse>();
		this.sliceReponseList.add(first);
		this.callid   = first.getCallID();
		this.sequence = first.getSequence();
	}
	
	/**
	 * 添加一个部分回复消息包
	 * @param response
	 */
	public void addSliceSipcResponse(SipcResponse response)
	{
		if(response.isSlice()){
			this.sliceReponseList.add(response);
			//为了保险起见，这里先对所有的包的分块偏移做个排序
			this.sortBySliceOffset();
		}
	}
	
	/**
	 * 判断是否整个消息已经收到所有的分块包
	 * 这里通过判断是否有状态不为188的来判断，如果有表明收到了所有的回复否则就没有
	 * 因为状态不为188的总是最后一个到达，所以这里可以简单的判断最后一个元素是否是188状态就可以了
	 * 本来还应该判断各个偏移是否完整，因为飞信是基于流传输的，这里可以忽略掉
	 * 
	 * @return
	 */
	public boolean isFullSipcResponseRecived()
	{
		if(this.sliceReponseList.size()>0){
			SipcResponse res = this.sliceReponseList.get(this.sliceReponseList.size()-1);
			return res.getStatusCode()!=SipcStatus.PARTIAL;
		}else{
			return false;
		}
	}
	
	/**
	 * 返回完整的的回复消息包
	 * @return
	 */
	public SipcResponse getFullSipcResponse()
	{
		//有个问题：这里这么多的分块回复，每个回复都是独立的，那究竟最终的回复是依据第一个还是最后一个呢？
		//这里依据的是最后一个回复
		
		//新建一个回复对象,把最后一个回复的状态和回复消息复制到到新回复对象
		SipcResponse last = this.sliceReponseList.get(this.sliceReponseList.size()-1);
		SipcResponse some = new SipcResponse(last.getStatusCode(), last.getStatusMessage());
		
		//把最后一个回复对象的所有消息头除了Content-Length都复制到新回复对象
		Iterator<SipcHeader> it = last.getHeaders().iterator();
		while(it.hasNext()){
			SipcHeader header = it.next();
			if(!SipcHeader.LENGTH.equals(header.getName())){	//如果不是L域就复制到新的回复头部中
				some.addHeader(header);
			}
		}
		
		//复制所有消息体到新的回复包中
		StringBuffer buffer = new StringBuffer();
		Iterator<SipcResponse> rit = this.sliceReponseList.iterator();
		while(rit.hasNext()){
			SipcResponse res = rit.next();
			if(res.getBody()!=null){
				buffer.append(res.getBody().toSendString());
			}
		}
		some.setBody(new SipcBody(buffer.toString()));
		
		//别忘了设置消息长度，虽然可能用不上。。
		some.addHeader(SipcHeader.LENGTH, Integer.toString(some.getBody().getLength()));
		
		//返回新的回复
		return some;
	}
	
	/**
	 * @return the callid
	 */
	public int getCallid()
	{
		return callid;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence()
	{
		return sequence;
	}

	/**
	 * 对分块的消息根据分块偏移按从小到大的顺序进行排序
	 */
	private void sortBySliceOffset()
	{
		Collections.sort(this.sliceReponseList,new Comparator<SipcResponse>(){
			public int compare(SipcResponse res1, SipcResponse res2){
				if(res1.getSliceOffset()<res2.getSliceOffset()){
					return -1;
				}else if(res1.getSliceOffset()>res2.getSliceOffset()){
					return 1;
				}else{
					return 0;
				}
			}
		});
	}
}
