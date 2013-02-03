/**
 * Project  : MapleFetion
 * Package  : test
 * File     : AuthGenerator.java
 * Author   : solosky < solosky772@qq.com >
 * Date     : 2009-11-16
 * Modified : 2009-11-16
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *  验证信息生成类
 *  参照了reflector反编译的飞信源代码以及用HaozesFx测试结果^_^!我折腾了一个下午才搞出来。。
 */
public class AuthGenerator
{
	/**
	 * 用户所在域，默认为fetion.com.cn
	 */
	private String domain;
	/**
	 * 服务器返回的32个字符临时随机加密字串
	 */
	private String nonce;
	
	/**
	 * 用户飞信号
	 */
	private String sid;
	
	/**
	 * 未加密的原始密码
	 */
	private String plainpass;
	
	/**
	 * 客户端随机生成的一个32个字符的加密字串
	 */
	private String cnonce;
	
	/**
	 * 加密后的密码，40个字符
	 */
	private String encryptedpass;
	
	/**
	 * 计算的验证密码，需发往服务器验证用户密码
	 */
	private String response;
	
	/**
	 * 构造函数
	 * @param sid			SSSignIn返回的sid也就是飞信号
	 * @param plainpass		原始密码
	 * @param domain		用户所在域
	 * @param nonce			第一次注册服务返回的临时随机加密字串
	 */
	public AuthGenerator(String sid, String plainpass,String domain ,String nonce)
	{
		this.sid    = sid;
		this.domain = domain;
		this.nonce  = nonce;
		this.plainpass = plainpass;
	}

	/**
	 * 生成回复
	 */
	public void generate()
	{
		this.generateCnonce();
		this.encryptPassword();
		byte[] key = this.generateKey();
		byte[] h1  = this.generateH1(key);
		byte[] h2  = this.generateH2();
		this.generateResponse(h1, h2);
	}
	
	/**
	 * 生成客户端随机加密字串
	 */
	private void generateCnonce()
    {
    	String uuid = UUID.randomUUID().toString();
    	this.cnonce = ConvertHelper.byte2HexStringWithoutSpace( DigestHelper.MD5( uuid.getBytes()));
    }
	
	/**
	 * 生成H1
	 * @param key
	 * @return
	 */
	private byte[] generateH1(byte[] key)
    {
    	String s =":"+this.nonce+":"+this.cnonce;
    	byte[] headBytes = s.getBytes();
    	
    	ByteBuffer buffer = ByteBuffer.allocate(headBytes.length+key.length);
    	buffer.put(key);
    	buffer.put(headBytes);
    	return DigestHelper.MD5(buffer.array());
    }
	
	/**
	 * 生成H2
	 * @return
	 */
	private byte[] generateH2()
	{
		String s = "REGISTER:"+this.sid;
		return DigestHelper.MD5(s.getBytes());
		
	}
	
	/**
	 * 加密原始密码，这是完全不同的一种加密算法
	 */
	private void encryptPassword()
	{
		PasswordEncrypter en = new PasswordEncrypter();
		this.encryptedpass = en.encrypt(this.plainpass);
	}
	
	/**
	 * 生成用于加密h1的key
	 * @return
	 */
	private byte[] generateKey()
	{
		String keyStr = this.sid+":"+this.domain+":";
		byte[] headBytes = keyStr.getBytes();
		byte[] passBytes = ConvertHelper.hexString2ByteNoSpace(this.encryptedpass.substring(8));
		ByteBuffer buffer = ByteBuffer.allocate(headBytes.length+passBytes.length);
		buffer.put(headBytes);
		buffer.put(passBytes);
		buffer.flip();
		
		return DigestHelper.SHA1(buffer.array());
	}
	
	/**
	 * 生成回复验证字符串
	 * @param h1
	 * @param h2
	 */
	private void generateResponse(byte[] h1, byte[] h2)
	{
		String sh1 = ConvertHelper.byte2HexStringWithoutSpace(h1);
		String sh2 = ConvertHelper.byte2HexStringWithoutSpace(h2);
		String s = sh1+":"+this.nonce+":"+sh2;
		this.response = ConvertHelper.byte2HexStringWithoutSpace(DigestHelper.MD5(s.getBytes()));
	}
	
	/**
	 * 返回客户端生成的随机字符串
	 * @return
	 */
	public String getCnonce()
    {
    	return cnonce;
    }

	/**
	 * 返回加密后的密码字符串
	 * @return
	 */
	public String getEncryptedPassword()
    {
    	return encryptedpass;
    }
	
	/**
	 * 返回计算的验证回复
	 * @return
	 */
	public String getResponse()
    {
    	return response;
    }
	
	/**
	 * SALT 就是密码的前八个字符
	 * @return
	 */
	public String getSalt()
	{
		return encryptedpass.substring(0,8);
	}
}
