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
 * Package  : net.solosky.maplefetion.bean
 * File     : VerifyImage.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2010-3-9
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.bean;

/**
 *
 * 验证图片
 *
 * @author solosky <solosky772@qq.com>
 */
public class VerifyImage
{
	/**
	 * 验证图片编号
	 */
	private String imageId;
	
	/**
	 * 图片的算法
	 */
	private String algorithm;
	
	/**
	 * 验证图片数据
	 */
	private byte[] imageData;
	
	/**
	 * 用户输入的验证字符
	 */
	private String verifyCode;
	
	/**
	 * 图片的类型
	 */
	private String verifyType;
	
	/**
	 * 构造函数
	 * @param imageId	 图片编号
	 * @param imageData 图片数据
	 */
	public VerifyImage(String imageId, byte[] imageData, String algorithm, String verifyType)
	{
		this.imageId    = imageId;
		this.imageData  = imageData;
		this.algorithm  = algorithm;
		this.verifyCode = null;
		this.verifyType = verifyType;
	}
	
	/**
	 * 默认的构造函数
	 */
	public VerifyImage() 
	{
	}

	/**
     * @return the verifyCode
     */
    public String getVerifyCode()
    {
    	return verifyCode;
    }

	/**
     * @param verifyCode the verifyCode to set
     */
    public void setVerifyCode(String verifyCode)
    {
    	this.verifyCode = verifyCode;
    }

	/**
     * @param imageId the imageId to set
     */
    public void setImageId(String imageId)
    {
    	this.imageId = imageId;
    }

	/**
     * @param imageData the imageData to set
     */
    public void setImageData(byte[] imageData)
    {
    	this.imageData = imageData;
    }

	/**
     * @return the imageId
     */
    public String getImageId()
    {
    	return imageId;
    }

	/**
     * @return the imageData
     */
    public byte[] getImageData()
    {
    	return imageData;
    }

	/**
     * @return the verifyType
     */
    public String getVerifyType()
    {
    	return verifyType;
    }

	/**
     * @return the algorithm
     */
    public String getAlgorithm()
    {
    	return algorithm;
    }

	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
	    return "VerifyImage [imageId=" + imageId + ", algorithm=" + algorithm
	            + ", verifyType=" + verifyType + ", verifyCode=" + verifyCode
	            + "]";
    }
	
}
