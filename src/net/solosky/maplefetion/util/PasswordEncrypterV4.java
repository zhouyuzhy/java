package net.solosky.maplefetion.util;
/**
 * Project  : MapleFetion
 * Package  : test
 * File     : PassEncrypter.java
 * Author   : solosky < solosky772@qq.com >
 * Date     : 2009-11-16
 * Modified : 2009-11-16
 * License  : Apache License 2.0 
 */

/**
 *  密码加密工具
 *  同样也参照了reflector反编译的飞信源代码以及用HaozesFx测试结果
 */
public class PasswordEncrypterV4
{
	
	public static String encryptV4(int userid, String plainpass)
	{
		String passHex  = encryptV4(plainpass);
		return doHash(ConvertHelper.int2Byte(userid), ConvertHelper.hexString2ByteNoSpace(passHex));
	}
	
	public static String encryptV4(String plainpass)
	{
		return doHash(ConvertHelper.string2Byte("fetion.com.cn:"), ConvertHelper.string2Byte(plainpass));
	}
	
	private static String doHash(byte[] b1, byte[] b2)
	{
		byte [] dst = new byte[b1.length+b2.length];
		System.arraycopy(b1, 0, dst, 0, b1.length);
		System.arraycopy(b2, 0, dst, b1.length, b2.length);
		byte[] res = DigestHelper.SHA1(dst);
		return ConvertHelper.byte2HexStringWithoutSpace(res);
	}

	
	public static String encryptV4Temp(int userid, String digest)
	{
		return doHash(ConvertHelper.int2Byte(userid), ConvertHelper.hexString2ByteNoSpace(digest));
	}
}
