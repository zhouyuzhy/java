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
 * Project  : MapleFetion
 * Package  : net.solosky.maplefetion.net
 * File     : DefaultSIPMessageFactory.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-22
 * License  : Apache License 2.0 
 */

package net.solosky.maplefetion.client.dialog;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Group;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.bean.ScheduleSMS;
import net.solosky.maplefetion.bean.StoreVersion;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.net.Port;
import net.solosky.maplefetion.sipc.SipcBody;
import net.solosky.maplefetion.sipc.SipcHeader;
import net.solosky.maplefetion.sipc.SipcMethod;
import net.solosky.maplefetion.sipc.SipcReceipt;
import net.solosky.maplefetion.sipc.SipcRequest;
import net.solosky.maplefetion.util.AuthGeneratorV4;
import net.solosky.maplefetion.util.ConvertHelper;
import net.solosky.maplefetion.util.NodeBuilder;
import net.solosky.maplefetion.util.PasswordEncrypterV4;
import net.solosky.maplefetion.util.StringHelper;

/**
 *	
 *	默认的SIP信令建立工厂
 *
 * @author solosky <solosky772@qq.com> 
 */
public class MessageFactory
{
	private int globalCallId;
	private int gloalSequence;
	private User user;
	private String lastMethod;
	
	
	/**
	 * 构造函数
	 * @param sid		飞信号
	 * @param domain	飞信域
	 */
	public MessageFactory(User user)
	{
		this.user = user;
		this.globalCallId = 0;
		this.lastMethod = "";
		this.gloalSequence = 1;
	}
	
	/**
	 * 创建默认的SipcRequest
	 * @param m
	 * @return
	 */
    public SipcRequest createDefaultSipcRequest(String m)
    {
	    SipcRequest req = new SipcRequest(m,this.user.getDomain());
	    req.addHeader(SipcHeader.FROM, Integer.toString(this.user.getFetionId()));
	    if(m.equals(this.lastMethod)) {
	    	req.addHeader(SipcHeader.CALLID,   Integer.toString(this.globalCallId));
	    	req.addHeader(SipcHeader.SEQUENCE, Integer.toString(this.getNextSequence())+" "+m);
	    }else {
	    	req.addHeader(SipcHeader.CALLID,   Integer.toString(this.getNextCallID()));
	    	req.addHeader(SipcHeader.SEQUENCE, "1 "+m);
	    	this.gloalSequence = 1;
	    }
	    req.setAliveTime((int) (FetionConfig.getInteger("fetion.sip.default-alive-time")+System.currentTimeMillis()/1000));
	    this.lastMethod = m;
	    return req;
    }
    
    /**
     * 服务器登录请求
     * @return
     */
    public SipcRequest createServerRegisterRequest(int presence, boolean isSupportedMutiConnection)
    {
    	 SipcRequest req = this.createDefaultSipcRequest(SipcMethod.REGISTER);
    	 req.addHeader("CN", AuthGeneratorV4.getCnonce());
         req.addHeader("CL", "type=\"pc\" ,version=\""+FetionClient.PROTOCOL_VERSION+"\"");
         return req;
    }
    
    /**
     * 用户登录验证
     * @return
     */
    public SipcRequest createUserAuthRequest(SipcHeader wwwHeader, int presence, boolean isSupportedMutiConnection, StoreVersion version)
    {
    	SipcRequest  req = this.createDefaultSipcRequest(SipcMethod.REGISTER);
    	
    	Pattern pt = Pattern.compile("Digest algorithm=\"SHA1-sess-v4\",nonce=\"(.*?)\",key=\"(.*?)\",signature=\"(.*?)\"");
    	Matcher mc = pt.matcher(wwwHeader.getValue());
    	if(mc.matches()) {
    		String passHex = PasswordEncrypterV4.encryptV4(this.user.getUserId(),this.user.getPassword());
    		AuthGeneratorV4 auth = new AuthGeneratorV4();
    		String aeskey = ConvertHelper.byte2HexStringWithoutSpace(user.getAesKey());
        	String response = auth.generate(mc.group(2), passHex, mc.group(1), aeskey);
        	String authString ="Digest response=\""+response+"\",algorithm=\"SHA1-sess-v4\"";
        	req.addHeader(SipcHeader.AUTHORIZATION, authString);
        	req.addHeader("AK", "ak-value");
        	
        	String body = MessageTemplate.TMPL_USER_AUTH;
        	body = body.replace("{machineCode}", FetionConfig.getString("fetion.sip.machine-code")); 
        	body = body.replace("{sid}", Integer.toString(this.user.getFetionId()));
        	body = body.replace("{userId}", Integer.toString(this.user.getUserId()));
        	body = body.replace("{presence}", Integer.toString(presence));
        	body = body.replace("{personalVersion}", Integer.toString(version.getPersonalVersion()));
        	body = body.replace("{contactVersion}", Integer.toString(version.getContactVersion()));
        	req.setBody(new SipcBody(body));
        	
    	}else {
    		throw new IllegalStateException("parse wwwHeader failed. wwwHeader="+wwwHeader.getValue());
    	}
    	
    	return req;
    }
    
    /**
     * 获取个人详细信息
     * @return
     */
    public SipcRequest createGetPersonalInfoRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
	    req.addHeader(SipcHeader.EVENT, "GetPersonalInfo");
    	req.setBody(new SipcBody(MessageTemplate.TMPL_GET_PERSONAL_INFO));
    	
    	return req;
    }
    
    /**
     * 发送在线消息
     */
    public SipcRequest createSendChatMessageRequest(String toUri, Message m)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.MESSAGE);
    	
    	req.addHeader(SipcHeader.TO, toUri);
    	//req.addHeader(SipcHeader.CONTENT_TYPE, "text/plain");text/html-fragment
    	req.addHeader(SipcHeader.CONTENT_TYPE, m.getType());
    	req.addHeader(SipcHeader.EVENT, "CatMsg");
    	
    	req.setBody(new SipcBody(m.getContent()));
    	
    	return req;
    }
    
    /**
     * 发送手机短消息
     * @param uri
     * @param m
     * @return
     */
    public SipcRequest createSendSMSRequest(String uri, Message m)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.MESSAGE);
    	
    	req.addHeader(SipcHeader.TO, uri);
    	req.addHeader(SipcHeader.EVENT, "SendCatSMS");
    	
    	req.setBody(new SipcBody(m.getText()));
    	
    	return req;
    }
    
    /**
     * 保持连接
     * @return
     */
    public SipcRequest createKeepConnectionRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.OPTION);
    	req.addHeader(SipcHeader.EVENT, "KeepConnectionBusy");
    	return req;
    }
    
    /**
     * 保持在线的请求
     * 也就是需要每隔一定时间需要注册一次
     * @return
     */
    public SipcRequest createKeepAliveRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.REGISTER);
    	req.addHeader(SipcHeader.EVENT, "KeepAlive");
    	req.setBody(new SipcBody(MessageTemplate.TMPL_KEEP_ALIVE));
    	return req;
    }
    
    /**
     * 注销登录请求
     */
    public SipcRequest createLogoutRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.REGISTER);
    	req.addHeader(SipcHeader.EXPIRED, "0");
    	return req;
    }
    
    
    /**
     *  获取联系人详细信息
     * @param buddyList
     * @return
     */
    public SipcRequest createGetContactInfoRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	String body = MessageTemplate.TMPL_GET_CONTACT_INFO;
    	body = body.replace("{args}", " uri=\""+uri+"\" version=\"0\" ");
    	req.setBody(new SipcBody(body));
    	
    	req.addHeader(SipcHeader.EVENT, "GetContactInfoV4");
    	
    	return req;
    }
    
    /**
     * 订阅异步通知
     */
    public SipcRequest createSubscribeRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SUBSCRIBE);
    	req.addHeader(SipcHeader.EVENT, "PresenceV4");
    	req.setBody(new SipcBody(MessageTemplate.TMPL_SUBSCRIBE));
    	
    	return req;
    }
    
    /**
     * 开始聊天请求
     */
    public SipcRequest createStartChatRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT,"StartChat");
    	return req;
    }
    
    /**
     * 注册聊天服务器
     */
    public SipcRequest createRegisterChatRequest(String ticket)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.REGISTER);
    	req.addHeader(SipcHeader.AUTHORIZATION,"TICKS auth=\""+ticket+"\"");
    	req.addHeader(SipcHeader.SUPPORTED,"text/html-fragment");
    	//req.addHeader(SipcHeader.SUPPORTED,"multiparty");
    	//req.addHeader(SipcHeader.SUPPORTED,"nudge");
    	//req.addHeader(SipcHeader.SUPPORTED,"share-background");
    	//req.addHeader(SipcHeader.FIELD_SUPPORTED,"fetion-show");
    	
    	return req;
    }
    
    /**
     * 邀请好友加入会话
     */
    public SipcRequest createInvateBuddyRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	String body = MessageTemplate.TMPL_INVATE_BUDDY;
    	body = body.replace("{uri}", uri);
    	
    	req.addHeader(SipcHeader.EVENT,"InviteBuddy");
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 飞信秀（有空再研究）
     */
    public SipcRequest createFetionShowRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.INFO);
//    	ByteArrayBuffer buffer = new ByteArrayBuffer(100);
//    	buffer.append(MessageTemplate.TMPL_FETION_SHOW_1.getBytes(), 0,MessageTemplate.TMPL_FETION_SHOW_1.getBytes().length);
//    	buffer.append(0xE5);
//    	buffer.append(0x9B);	//飞信太变态了，这里居然有几个字节无法用字符表示
//    	buffer.append(0xA7);
//    	buffer.append(MessageTemplate.TMPL_FETION_SHOW_2.getBytes(), 0,MessageTemplate.TMPL_FETION_SHOW_2.getBytes().length);
//    	
//    	byte[] bodyArr = buffer.toByteArray();
//    	req.setBody(new SipcBody(new String(bodyArr)));
    	return req;
    }
    
    /**
     * 添加飞信好友请求
     * @param uri
     * @param promptId
     * @param cordId
     * @param desc
     * @return
     */
    public SipcRequest createAddBuddyRequest(String uri, int promptId, int cordId, String desc, String localName)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	String body = MessageTemplate.TMPL_ADD_BUDDY;
    	body = body.replace("{uri}", uri);
    	body = body.replace("{promptId}", Integer.toString(promptId));
    	body = body.replace("{cordId}", cordId==-1? "": Integer.toString(cordId));
    	body = body.replace("{desc}", StringHelper.qouteHtmlSpecialChars(desc));
    	body = body.replace("{localName}", localName!=null?"local-name=\""+localName+"\"":"");
    	
    	req.addHeader(SipcHeader.EVENT,"AddBuddyV4");
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 删除好友
     * @param uri
     * @return
     */
    public SipcRequest createDeleteBuddyRequest(int userId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	String body = MessageTemplate.TMPL_DELETE_BUDDY;
    	body = body.replace("{userId}", Integer.toString(userId));
    	
    	req.addHeader(SipcHeader.EVENT,"DeleteBuddyV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 同意对方添加好友
     * @param uri
     * @param localName
     * @param cordId
     * @return
     */
    public SipcRequest createAgreeApplicationRequest(int userId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	String body = MessageTemplate.TMPL_APPLICATION_AGREED;
    	body = body.replace("{userId}", Integer.toString(userId));
    	
    	req.addHeader(SipcHeader.EVENT,"HandleContactRequestV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 拒绝陌生人添加好友请求
     * @param userId
     * @return
     */
    public SipcRequest createDeclineApplicationRequest(int userId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	String body = MessageTemplate.TMPL_APPLICATION_DECLINED;
    	body = body.replace("{userId}", Integer.toString(userId));
    	
    	req.addHeader(SipcHeader.EVENT,"HandleContactRequestV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 更改个人资料
     * 这里只支持更改昵称和个性签名
     * @param updateXML 更新的XML
     * @return
     */
    public SipcRequest createSetPersonalInfoRequest()
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	NodeBuilder builder = new NodeBuilder();
    	//因为用户可以改变自己的信息，这里权限改变了所以不使用BeanHelper来处理
    	builder.add("nickname", StringHelper.qouteHtmlSpecialChars(user.getNickName()));
    	builder.add("impresa",  StringHelper.qouteHtmlSpecialChars(user.getImpresa()));
    	//用户扩展信息。.TODO ..
        //BeanHelper.toUpdateXML(BuddyExtend.class, this.client.getFetionUser(), builder);
    	
    	String body = MessageTemplate.TMPL_SET_PERSONAL_INFO;
    	body = body.replace("{personal}", builder.toXML("personal"));
    	
    	req.addHeader(SipcHeader.EVENT,"SetUserInfoV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 设置好友本地姓名
     * @param userId	好友用户ID
     * @param localName	本地显示名字
     * @return
     */
    public SipcRequest createSetBuddyLocalName(int userId, String localName)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	String body = MessageTemplate.TMPL_SET_BUDDY_LOCAL_NAME;
    	body = body.replace("{userId}", Integer.toString(userId));
    	body = body.replace("{localName}", localName);
    	
    	req.addHeader(SipcHeader.EVENT,"SetContactInfoV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 设置好友分组信息
     * @param uri
     * @param cordId
     * @return
     */
    public SipcRequest createSetBuddyCord(int userId, String cordId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	String body = MessageTemplate.TMPL_SET_BUDDY_CORD;
    	body = body.replace("{userId}", Integer.toString(userId));
    	body = body.replace("{cordId}", cordId!=null?cordId:"");
    	
    	req.addHeader(SipcHeader.EVENT,"SetContactInfoV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 建立新的分组
     */
    public SipcRequest createCreateCordRequest(String title)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT,"CreateBuddyList");
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_CREATE_CORD.replace("{title}", title)) );
    	return req;
    }
    
    /**
     * 删除分组
     */
    public SipcRequest createDeleteCordRequest(int cordId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT,"DeleteBuddyList");
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_DELETE_CORD.replace("{cordId}", Integer.toString(cordId))) );
    	return req;
    }
    
    /**
     * 更新分组标题
     */
    public SipcRequest createSetCordTitleRequest(int cordId, String title)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT,"SetBuddyListInfo");
    	
    	String body = MessageTemplate.TMPL_UPDATE_CORD;
    	body = body.replace("{cordId}", Integer.toString(cordId));
    	body = body.replace("{title}", title);
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 设置在线状态
     */
    public SipcRequest createSetPresenceRequest(int presence)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	
    	String body = MessageTemplate.TMPL_SET_PRESENCE;
    	body = body.replace("{presence}", Integer.toString(presence));
    	
    	req.addHeader(SipcHeader.EVENT,"SetPresenceV4");
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    /**
     * 退出客户端
     */
    public SipcRequest createLogoutRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.BYE);
    	
    	req.addHeader(SipcHeader.TO, uri);
    	return req;
    }
    
    //////////////////////////////////////群操作///////////////////////////////////////////////
    /**
     * 获取群列表
     */
    public SipcRequest createGetGroupListRequest(int localVersion)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "PGGetGroupList");
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_GET_GROUP_LIST.replace("{version}", Integer.toString(localVersion))));
    	
    	return req;
    }
    
    /**
     * 获取群信息
     */
    public SipcRequest createGetGroupInfoRequest(Collection<Group> groupList)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "PGGetGroupInfo");
    	
    	Iterator<Group> it = groupList.iterator();
    	StringBuffer buffer = new StringBuffer();
    	String node = "<group uri=\"{uri}\" />";
    	while(it.hasNext()){
    		buffer.append(node.replace("{uri}", it.next().getUri()));
    	}
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_GET_GROUP_INFO.replace("{groupList}", buffer.toString())));
    	
    	return req;
    }
    
    /**
     * 获取群成员列表
     */
    public SipcRequest createGetMemberListRequest(Collection<Group> groupList)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "PGGetGroupMembers");
    	
    	Iterator<Group> it = groupList.iterator();
    	StringBuffer buffer = new StringBuffer();
    	String node = "<group uri=\"{uri}\" />";
    	while(it.hasNext()){
    		buffer.append(node.replace("{uri}", it.next().getUri()));
    	}
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_GET_MEMBER_LIST.replace("{groupList}", buffer.toString())));
    	
    	return req;
    }
    
    /**
     * 订阅群通知
     */
    public SipcRequest createSubscribeGroupNotifyRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SUBSCRIBE);
    	req.addHeader(SipcHeader.EVENT, "PGPresence");
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_SUBSCRIBE_GROUP_NOPTIFY.replace("{uri}", uri)));
    	
    	return req;
    }
    
    
    /**
     * 开始群会话
     */
    public SipcRequest createInviteRequest(String uri, Port localPort)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.INVATE);
    	req.addHeader(SipcHeader.TO, uri);
    	req.addHeader(SipcHeader.SUPPORTED,"text/html-fragment");
    	//req.addHeader(SipcHeader.SUPPORTED, "text/plain");
    	req.addHeader(SipcHeader.SUPPORTED,"multiparty");
    	req.addHeader(SipcHeader.SUPPORTED,"nudge");
    	req.addHeader(SipcHeader.SUPPORTED,"share-background");
    	req.addHeader(SipcHeader.SUPPORTED,"fetion-show");
    	
    	req.setNeedReplyTimes(2);	// 需回复两次
    	
    	//正文是一些固定的参数
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("v=0\r\n");
    	buffer.append("o=-0 0 IN "+localPort.toString()+"\r\n");
    	buffer.append("s=session\r\n");
    	buffer.append("c=IN IP4 "+localPort.toString()+"\r\n");
    	buffer.append("t=0 0\r\n");
    	buffer.append("m=message "+Integer.toString(localPort.getPort())+" sip "+uri);
    	
    	req.setBody(new SipcBody(buffer.toString()));
    	
    	return req;
    }
    
    
    /**
     * 确认会话收到请求
     */
    public SipcRequest createAckRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.ACK);
    	req.addHeader(SipcHeader.TO, uri);
    	
    	req.setNeedReplyTimes(0);
    	return req;
    	
    }
    
    /**
     * 群在线请求
     */
    public SipcRequest createGroupKeepLiveRequest(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.INFO);
    	req.addHeader(SipcHeader.TO, uri);
    	
    	req.setBody(new SipcBody(MessageTemplate.TMPL_GROUP_KEEP_LIVE));
    	
    	return req;
    }
    
    /**
     * 群消息
     */
    public SipcRequest createSendGroupChatMessageRequest(String uri, String message)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.MESSAGE);
    	req.addHeader(SipcHeader.CONTENT_TYPE, "text/html-fragment");
    	req.addHeader(SipcHeader.CONTENT_TYPE, "text/plain");
    	req.addHeader(SipcHeader.SUPPORTED, "SaveHistory");
    	req.addHeader(SipcHeader.TO, uri);
    	req.setBody(new SipcBody(message));
    	
    	return req;
    }
     
    /**
     * 设置群状态
     */
    public SipcRequest createSetGroupPresenceRequest(String uri, int presense)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "PGSetPresence");
    	String body = MessageTemplate.TMPL_GROUP_SET_PRESENCE;
		body = body.replace("{uri}", uri);
		body = body.replace("{presence}", Integer.toString(presense));
    	
    	req.setBody(new SipcBody(body));
    	
    	return req;
    }
    
    
    /////////////////////////////////////定时短信////////////////////////////////////////////
    /**
     * 获取定时短信列表
     */
    public SipcRequest createGetScheduleSMSListRequest(int localVersion)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "SSGetScheduleSmsList");
    	
    	String body = MessageTemplate.TMPL_GET_SCHEDULE_SMS_LIST;
    	body = body.replace("{version}", Integer.toString(localVersion));
    	req.setBody(new SipcBody(body));
    	
    	return req;
    }
    
    /**
     * 获取定时短信的详细信息
     */
    public SipcRequest createGetScheduleSMSInfo(Collection<ScheduleSMS> scheduleSMSList)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "SSGetScheduleSms");
    	
    	String body = MessageTemplate.TMPL_DELETE_SCHEDULE_SMS;
    	
    	String scheduleSMSTmpl = "<schedule-sms id=\"{id}\" />";
    	StringBuffer buffer = new StringBuffer();
    	Iterator<ScheduleSMS> it = scheduleSMSList.iterator();
    	while(it.hasNext()){
    		ScheduleSMS s = it.next();
    		buffer.append(scheduleSMSTmpl.replace("{id}", Integer.toString(s.getId())));
    	}
    	body = body.replace("{scheduleSMSList}", buffer.toString());
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 创建定时短信
     */
    public SipcRequest createCreateScheduleSMSRequest(Date sendDate, Message message, Collection<Buddy> receiverList)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "SSSetScheduleCatSms");
    	
    	String body = MessageTemplate.TMPL_CREATE_SCHEDULE_SMS;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d H:m:s");
    	df.setTimeZone(TimeZone.getTimeZone("GMT 0"));
    	body = body.replace("{sendDate}", df.format(sendDate));
    	body = body.replace("{message}", message.getText());
    	
    	String receiverTmpl = "<receiver uri=\"{uri}\" />";
    	StringBuffer buffer = new StringBuffer();
    	Iterator<Buddy> it = receiverList.iterator();
    	while(it.hasNext()){
    		Buddy b = it.next();
    		buffer.append(receiverTmpl.replace("{uri}", b.getUri()));
    	}
    	body = body.replace("{receiverList}", buffer.toString());
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    /**
     * 删除定时短信
     */
    public SipcRequest createDeleteScheduleSMSRequest(Collection<ScheduleSMS> scheduleSMSList)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "SSDeleteScheduleSms");
    	
    	String body = MessageTemplate.TMPL_DELETE_SCHEDULE_SMS;
    	
    	String scheduleSMSTmpl = "<schedule-sms id=\"{id}\" />";
    	StringBuffer buffer = new StringBuffer();
    	Iterator<ScheduleSMS> it = scheduleSMSList.iterator();
    	while(it.hasNext()){
    		ScheduleSMS s = it.next();
    		buffer.append(scheduleSMSTmpl.replace("{id}", Integer.toString(s.getId())));
    	}
    	body = body.replace("{scheduleSMSList}", buffer.toString());
    	
    	req.setBody(new SipcBody(body));
    	return req;
    }
    
    
    /**
     * 将好友移到黑名单中
     */
    public SipcRequest createAddBuddyToBlackList(String uri)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "AddToBlacklist");
    	req.setBody(new SipcBody(MessageTemplate.TMPL_ADD_TO_BLACKLIST.replace("{uri}", uri)));
    	return req;
    }
    
    /**
     * 将好友从黑名单移除
     */
    public SipcRequest createRemoveBuddyFromBlackList(int userId)
    {
    	SipcRequest req = this.createDefaultSipcRequest(SipcMethod.SERVICE);
    	req.addHeader(SipcHeader.EVENT, "RemoveFromBlacklistV4");
    	req.setBody(new SipcBody(MessageTemplate.TMPL_REMOVE_FROM_BLACKLIST.replace("{userId}", Integer.toString(userId))));
    	return req;
    }
    ///////////////////////////////////////收据///////////////////////////////////////////////
    
    /**
     * 默认收据
     */
    public SipcReceipt createDefaultReceipt(String callId, String sequence)
    {
    	SipcReceipt receipt = new SipcReceipt(200, "OK");
    	receipt.addHeader(SipcHeader.CALLID, callId);
    	receipt.addHeader(SipcHeader.SEQUENCE, sequence);
    	
    	return receipt;
    }
    
    /**
     * 信息收到收据
     */
    public SipcReceipt createDefaultReceipt(String fromUri, String callId,String sequence)
    {
    	SipcReceipt receipt = this.createDefaultReceipt(callId, sequence);
    	receipt.addHeader(SipcHeader.FROM, fromUri);
    	
    	return receipt;
    }
    
    
    public SipcReceipt createHttpInviteReceipt(String uri, String callId, String sequence, Port local)
    {
    	SipcReceipt receipt = this.createDefaultReceipt(callId, sequence);
    	receipt.addHeader(SipcHeader.FROM, uri);
    	receipt.addHeader(SipcHeader.SUPPORTED,"text/html-fragment");
    	receipt.addHeader(SipcHeader.SUPPORTED, "text/plain");
    	//receipt.addHeader(SipcHeader.SUPPORTED,"multiparty");
    	//receipt.addHeader(SipcHeader.SUPPORTED,"nudge");
    	//receipt.addHeader(SipcHeader.SUPPORTED,"share-background");
    	//receipt.addHeader(SipcHeader.SUPPORTED,"fetion-show");
    	
    	
    	//正文是一些固定的参数
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("v=0\r\n");
    	buffer.append("o=-0 0 IN "+local.toString()+"\r\n");
    	buffer.append("s=session\r\n");
    	buffer.append("c=IN IP4 "+local.toString()+"\r\n");
    	buffer.append("t=0 0\r\n");
    	buffer.append("m=message "+Integer.toString(local.getPort())+" sip "+uri);
    	
    	receipt.setBody(new SipcBody(buffer.toString()));
    	
    	return receipt;
    }
    
    /**
     * 下一次CALLID
     * @return
     */
    private synchronized int getNextCallID()
    {
    	return ++globalCallId;
    }
    
    /**
     * 下一次Sequence
     */
    private synchronized int getNextSequence()
    {
    	return ++gloalSequence;
    }
}
