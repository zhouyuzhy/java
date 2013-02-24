package org.csp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


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

}
