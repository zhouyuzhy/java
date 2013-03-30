package org.csp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.bouncycastle.util.encoders.Base64Encoder;


public class Utils {

	public static byte[] readStreamToBytes(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int size = 1024;
		int len = 0;
		byte[] temp = new byte[size];
		while((len=is.read(temp, 0, size))!=-1){
			baos.write(temp, 0, len);
		}
		byte[] result = baos.toByteArray();
		baos.close();
		return result;
	}
	
	public static boolean isPem(String key){
		if(key == null || !key.startsWith("-----BEGIN RSA PRIVATE KEY-----")){
			return false;
		}
		return true;
	}

	
	public static void generateRandomString(byte[] randomBytes){
		Random random = new Random();
		random.nextBytes(randomBytes);
	}
	
	public static String base64Encode(byte[] target) throws IOException{
		Base64Encoder encoder = new Base64Encoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encoder.encode(target, 0, target.length, baos);
		String result = baos.toString();
		baos.close();
		return result;
	}
	
	public static byte[] base64Decode(String target) throws IOException{
		Base64Encoder encoder = new Base64Encoder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		encoder.decode(target, baos);
		byte[] result = baos.toByteArray();
		baos.close();
		return result;
	}
	
}
