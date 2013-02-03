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
 * Package  : net.solosky.maplefetion.util
 * File     : Convertor.java
 * Author   : solosky < solosky772@qq.com >
 * Created  : 2009-11-20
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 *
 * 转换类
 *
 * @author solosky <solosky772@qq.com> 
 */
public class ConvertHelper
{

	// 16进制字符数组
    private static char[] hex = new char[] { 
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

	/**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @return
     * 			16进制字符串，每个字节之间空格分隔，头尾无空格
     */
    public static String byte2HexString(byte[] b) {
    	if(b == null)
    		return "null";
    	else
    		return byte2HexString(b, 0, b.length);
    }

	/**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @param offset
     * 			从哪里开始转换
     * @param len
     * 			转换的长度
     * @return 16进制字符串，每个字节之间空格分隔，头尾无空格
     */
    public static String byte2HexString(byte[] b, int offset, int len) {
    	if(b == null)
    		return "null";
    	
        // 检查索引范围
        int end = offset + len;
        if(end > b.length)
            end = b.length;
        
        StringBuffer sb = new StringBuffer();
        
        for(int i = offset; i < end; i++) {
            sb.append(hex[(b[i] & 0xF0) >>> 4])
            	.append(hex[b[i] & 0xF])
            	.append(' ');
        }
        if(sb.length() > 0)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

	/**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @return
     * 			16进制字符串，每个字节没有空格分隔
     */
    public static String byte2HexStringWithoutSpace(byte[] b) {
    	if(b == null)
    		return "null";
    	
        return byte2HexStringWithoutSpace(b, 0, b.length);
    }

	/**
     * 把字节数组转换成16进制字符串
     * 
     * @param b
     * 			字节数组
     * @param offset
     * 			从哪里开始转换
     * @param len
     * 			转换的长度
     * @return 16进制字符串，每个字节没有空格分隔
     */
    public static String byte2HexStringWithoutSpace(byte[] b, int offset, int len) {
    	if(b == null)
    		return "null";
    	
        // 检查索引范围
        int end = offset + len;
        if(end > b.length)
            end = b.length;
        
        StringBuffer sb = new StringBuffer();
        
        for(int i = offset; i < end; i++) {
            sb.append(hex[(b[i] & 0xF0) >>> 4])
            	.append(hex[b[i] & 0xF]);
        }
        return sb.toString();
    }

	/**
     * 转换16进制字符串为字节数组
     * 
     * @param s
     * 			16进制字符串，每个字节由空格分隔
     * @return 字节数组，如果出错，返回null，如果是空字符串，也返回null
     */
    public static byte[] hexString2Byte(String s) {
        try {
            s = s.trim();
            StringTokenizer st = new StringTokenizer(s, " ");
            byte[] ret = new byte[st.countTokens()];
            for(int i = 0; st.hasMoreTokens(); i++) {
                String byteString = st.nextToken();
                
                // 一个字节是2个16进制数，如果不对，返回null
                if(byteString.length() > 2)
                    return null;
                
                ret[i] = (byte)(Integer.parseInt(byteString, 16) & 0xFF);     
            }
            return ret;
        } catch (Exception e) {
            return null;
        }
    }

	/**
     * 把一个16进制字符串转换为字节数组，字符串没有空格，所以每两个字符
     * 一个字节
     * 
     * @param s
     * @return
     */
    public static byte[] hexString2ByteNoSpace(String s) {
        int len = s.length();
        byte[] ret = new byte[len >>> 1];
        for(int i = 0; i <= len - 2; i += 2) {
            ret[i >>> 1] = (byte)(Integer.parseInt(s.substring(i, i + 2).trim(), 16) & 0xFF);
        }
        return ret;
    }
    
    /**
     * 把输入流转化一个字符串
     * @param in 输入流
     * @return	字符串
     * @throws IOException
     */
    public static String inputStream2String(InputStream in) throws IOException
    {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	byte[] b = new byte[1024];
    	int len = 0;
    	while((len=in.read(b))!=-1)
    		out.write(b, 0, len);
    	return new String(out.toByteArray());
    }
    
    /**
     * 把字符串转换为utf8字节数据
     * @param src		utf8编码原字符串
     * @return			字节数组
     */
    public static byte[] string2Byte(String src)
    {
    	byte[] ret = null;
    	try {
	        ret = src.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
        	throw new RuntimeException(e);
        }
        return ret;
    }
    
    /**
     * 把utf8字节数据转换为字符串
     * @param src		utf8编码原字节数组
     * @return			编码后的字符串
     */
    public static String byte2String(byte[] src)
    {
    	String ret = null;
    	try {
	        ret = new String(src, "UTF8");
        } catch (UnsupportedEncodingException e) {
        	throw new RuntimeException(e);
        }
        return ret;
    }
    
    /**
     * 把整形数转换为字节数组
     * @param i
     * @return
     */
    public static byte[] int2Byte(int i)
    {
          byte [] b = new byte[4];
          for(int m=0; m<4; m++, i>>=8) {
        	  b[m] = (byte) (i & 0x000000FF);	//奇怪, 在C# 整型数是低字节在前  byte[] bytes = BitConverter.GetBytes(i);
        	  									//而在JAVA里，是高字节在前
          }
          return b;
    }
    

}
