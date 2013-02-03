/**
 * Project  : MapleFetion
 * Package  : test
 * File     : PassEncrypter.java
 * Author   : solosky < solosky772@qq.com >
 * Date     : 2009-11-16
 * Modified : 2009-11-16
 * License  : Apache License 2.0 
 */
package net.solosky.maplefetion.util;

import java.nio.ByteBuffer;


/**
 *  密码加密工具
 *  同样也参照了reflector反编译的飞信源代码以及用HaozesFx测试结果
 */
public class PasswordEncrypter
{
	
	/**
	 * 加密密码
	 * @param plainpass	原始密码
	 * @param saltBytes 4个字节，用于加密
	 * @return			加密后的40位字符
	 */
	public String encrypt(String plainpass, byte[] saltBytes)
	{	
		byte[] cryptedBytes = DigestHelper.SHA1(plainpass.getBytes());
		
		ByteBuffer buffer  = ByteBuffer.allocate(saltBytes.length+cryptedBytes.length);
		buffer.put(saltBytes);
		buffer.put(cryptedBytes);
		buffer.flip();
		
		cryptedBytes = DigestHelper.SHA1(buffer.array()); 
		
		buffer.clear();
		buffer.put(saltBytes);
		buffer.put(cryptedBytes);
		
		return ConvertHelper.byte2HexStringWithoutSpace(buffer.array());
		
	}
	
	/**
	 * 使用随机的字节加密
	 * @param plainpass
	 * @return
	 */
	public String encrypt(String plainpass)
	{
		byte[] saltBytes = {(byte) 0x89,0x40,0x54,0x02};	//随机的4个加密字节
		return encrypt(plainpass, saltBytes);
	}
}
