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
 * Project  : MapleFetion2.5
 * Package  : net.solosky.maplefetion.event.notify
 * File     : ImageVerifyEvent.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-9-12
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.event.notify;

import net.solosky.maplefetion.bean.VerifyImage;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.sipc.SipcRequest;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class ImageVerifyEvent extends NotifyEvent
{

	public static final int SSI_VERIFY = 0;
	public static final int SIPC_VERIFY = 1;
	
	/**
	 * 验证类型
	 */
	private int verifyAction;
	
	/**
	 * 验证所对应的请求
	 */
	private SipcRequest targetRequest;
	
	/**
	 * 验证图片
	 */
	private VerifyImage verifyImage;
	
	/**
	 * 来自的对话
	 */
	private Dialog targetDialog;
	
	/**
	 * 结果监听器
	 */
	private ActionEventListener targetListener;
	
	/**
	 * 出现验证码的原因
	 */
	private String verifyReason;
	
	/**
	 * 一些提示
	 */
	private String verifyTips;
	
	

	/**
     * @param verifyAction
     * @param verifyImage
     * @param targetRequest
     * @param targetDialog
     */
    public ImageVerifyEvent(int verifyType, VerifyImage verifyImage,
    		String verifyReason, String verifyTips)
    {
	    this.verifyAction = verifyType;
	    this.verifyImage = verifyImage;
	    this.verifyReason = verifyReason;
	    this.verifyTips = verifyTips;
    }
    

	/**
     * @param verifyAction
     * @param verifyImage
     * @param verifyReason
     * @param verifyTips
     * @param targetRequest
     * @param targetDialog
     */
    public ImageVerifyEvent(int verifyAction, VerifyImage verifyImage,
            String verifyReason, String verifyTips, SipcRequest targetRequest,
            Dialog targetDialog, ActionEventListener targetListener)
    {
	    this.verifyAction = verifyAction;
	    this.verifyImage = verifyImage;
	    this.verifyReason = verifyReason;
	    this.verifyTips = verifyTips;
	    this.targetRequest = targetRequest;
	    this.targetDialog = targetDialog;
	    this.targetListener = targetListener;
    }



	/* (non-Javadoc)
     * @see net.solosky.maplefetion.event.NotifyEvent#getEventType()
     */
    @Override
    public NotifyEventType getEventType()
    {
	    return NotifyEventType.IMAGE_VERIFY;
    }


	/**
     * @return the verifyAction
     */
    public int getVerifyAction()
    {
    	return verifyAction;
    }


	/**
     * @return the targetRequest
     */
    public SipcRequest getTargetRequest()
    {
    	return targetRequest;
    }

	/**
     * @return the verifyImage
     */
    public VerifyImage getVerifyImage()
    {
    	return verifyImage;
    }

	/**
     * @return the targetDialog
     */
    public Dialog getTargetDialog()
    {
    	return targetDialog;
    }


	/**
     * @return the verifyTips
     */
    public String getVerifyTips()
    {
    	return verifyTips;
    }

	/**
     * @return the verifyReason
     */
    public String getVerifyReason()
    {
    	return verifyReason;
    }


	/**
     * @param verifyImage the verifyImage to set
     */
    public void setVerifyImage(VerifyImage verifyImage)
    {
    	this.verifyImage = verifyImage;
    }



	/**
     * @return the targetListener
     */
    public ActionEventListener getTargetListener()
    {
    	return targetListener;
    }


	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	    return "ImageVerifyEvent [verifyAction=" + verifyAction
	            + ", verifyReason=" + verifyReason + ", verifyTips="
	            + verifyTips + ", verifyImage=" + verifyImage
	            + ", targetRequest=" + targetRequest + "]";
    }



    
}
