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
 * Package  : net.solosky.maplefetion.client
 * File     : SSISignV1.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-1-15
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.client;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.FetionConfig;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.LocaleSetting;
import net.solosky.maplefetion.util.ParseException;
import net.solosky.maplefetion.util.PasswordEncrypter;
import net.solosky.maplefetion.util.XMLHelper;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * 
 * SSI登录第二版
 * 
 * @author solosky <solosky772@qq.com>
 */
public class SSISignV2 implements SSISign
{
	private LocaleSetting localeSetting;
	private static Logger logger = Logger.getLogger(SSISignV2.class);
	
	/**
	 * 构造函数
	 */
	public SSISignV2()
	{
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.SSISign#signOut()
     */
	public LoginState signOut(User user)
	{
		throw new IllegalAccessError("Not implemented.");
	}
	
	
	/**
	 * 打开连接
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	private URLConnection getConnection(String uri) throws IOException
	{
		URL url = new URL(uri);
		return url.openConnection();
	}
	
	/**
	 * 生成URL
	 * @param user
	 * @param pid
	 * @param pic
	 * @return
	 */
	private String buildUrl(User user, String pid, String pic)
	{
		String v2 = this.localeSetting.getNodeText("/config/servers/ssi-app-sign-in-v2");
		if(v2==null)	v2 = FetionConfig.getString("server.ssi-sign-in-v2");
		
		StringBuffer b = new StringBuffer();
		b.append(v2);
		b.append("?");
		if(user.getMobile()>0){
			b.append("mobileno="+Long.toString(user.getMobile()));
		}else if(user.getFetionId()>0){
			b.append("sid="+Integer.toString(user.getFetionId()));
		}else{
			throw new IllegalStateException("couldn't find valid mobile or fetionId to sign in..");
		}
		b.append("&domains=fetion.com.cn%3bm161.com.cn%3bwww.ikuwa.cn");
		b.append("&digest="+(new PasswordEncrypter().encrypt(user.getPassword())));		//就是用以前写的密码加密工具，我在这里很郁闷。。
		if(pid!=null) {		//这两个字段是用于验证码验证的
			b.append("&pid="+pid);
			b.append("&pic="+pic);
		}
		return b.toString();
	}
	
	/**
	 * 尝试登录操作
	 * @param mobileNo		用户手机号码
	 * @param pass			用户密码
	 * @param pid			验证图片编号
	 * @param pic			验证图片字符
	 * @return				状态值，定义在LoginListener中
	 * @throws IOException
	 * @throws ParseException 
	 */
	private LoginState signIn(User user, String pid, String pic)
	{
		LoginState state = null;
		String url = this.buildUrl(user, pid, pic);
		try {
	        HttpsURLConnection conn = (HttpsURLConnection) this.getConnection(url);
	        conn.addRequestProperty("User-Agent", "IIC2.0/PC "+FetionClient.PROTOCOL_VERSION);
	        logger.debug("SSISignIn: status="+Integer.toString(conn.getResponseCode()));
	        int status = conn.getResponseCode();
	        switch(status) {
	        case 401:
	        	logger.debug("Invalid password...");
	        	state = LoginState.SSI_AUTH_FAIL;
	        	break;
	        	
	        case 421:
	        case 422:
	        	logger.debug("SSISignIn: need verify.");
	        	state = LoginState.SSI_NEED_VERIFY;
	        	break;
	        
	        case 420:
	        	logger.debug("SSISignIn: invalid verify code.");
	        	state = LoginState.SSI_VERIFY_FAIL;
	        	break;
	        	
	        case 433:
	        	logger.debug("SSISignIn: User account suspend.");
	        	state = LoginState.SSI_ACCOUNT_SUSPEND;
	        	break;
	        	
	        case 404:
	        	logger.debug("SSISginIn: User not found..");
	        	state = LoginState.SSI_ACCOUNT_NOT_FOUND;
	        	break;
	        	
	        case 503:
	        	logger.debug("SSIServer overload...");
	        	state = LoginState.SSI_CONNECT_FAIL;
	        	break;
	        	
	        case 200:
	        	logger.debug("SSISignIn: sign in success.");
	        	state = LoginState.SSI_SIGN_IN_SUCCESS;
	        	
	        	String header = conn.getHeaderField("Set-Cookie");
	        	int s = header.indexOf("ssic=");
	        	int e = header.indexOf(';');
	        	String ssic = header.substring(s+5,e);
	        	
	        	Element root = XMLHelper.build(conn.getInputStream());
	        	String statusCode = root.getAttributeValue("status-code");
	        	Element userEl = root.getChild("user");
	        	String uri = userEl.getAttributeValue("uri");
	        	String uid = userEl.getAttributeValue("user-id");
	        	user.setSsiCredential(ssic);
	        	user.setUri(uri);
	        	BeanHelper.setValue(user, "userId", (Integer.parseInt(uid)));
	        	
	        	logger.debug("SSISignIn: ssic = "+ssic);
	        	user.setSsiCredential(ssic);
	        	break;
	        	
	        	default:
	        		state = LoginState.OTHER_ERROR;
	        }
        } catch (NumberFormatException e) {
        	state = LoginState.OTHER_ERROR;
        } catch (ParseException e) {
        	state = LoginState.OTHER_ERROR;
        } catch (IOException e) {
        	state = LoginState.SSI_CONNECT_FAIL;
        }catch(Throwable e) {
        	state = LoginState.OTHER_ERROR;
        }
		return state;
	}
	
	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.SSISign#signIn(net.solosky.maplefetion.bean.User)
     */
    @Override
    public LoginState signIn(User user)
    {
	    return this.signIn(user, null, null);
    }

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.SSISign#signIn(net.solosky.maplefetion.bean.User, net.solosky.maplefetion.bean.VerifyImage)
     */
    @Override
    public LoginState signIn(User user, VerifyImage img)
    {
	    return this.signIn(user, img.getImageId(), img.getVerifyCode());
    }

	/* (non-Javadoc)
	 * @see net.solosky.maplefetion.client.SSISign#setLocaleSetting(net.solosky.maplefetion.util.LocaleSetting)
	 */
	@Override
	public void setLocaleSetting(LocaleSetting localeSetting)
	{
		this.localeSetting = localeSetting;
	}

	/* (non-Javadoc)
     * @see net.solosky.maplefetion.client.SSISign#setFetionContext(net.solosky.maplefetion.FetionContext)
     */
    @Override
    public void setFetionContext(FetionContext context)
    {
	    // TODO Auto-generated method stub
	    
    }
}
