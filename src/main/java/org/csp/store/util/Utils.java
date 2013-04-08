package org.csp.store.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.csp.store.exception.Md5Exception;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class Utils {

	public static void printBytes(byte[] target) {
		for (byte t : target) {
			System.out.print(t + "\t");
		}
		System.out.println();
	}

	public static String md5(Object target) throws Md5Exception {
		if (target == null)
			throw new IllegalArgumentException("Serialize object should not be null.");
		byte[] convertedTarget = null;
		if (target instanceof String) {
			convertedTarget = ((String) target).getBytes();
		} else if (target instanceof Serializable) {
			try {
				convertedTarget = serialize((Serializable) target);
			} catch (IOException e) {
				throw new Md5Exception(e);
			}
		} else {
			try {
				convertedTarget = serializeByKryo(target);
			} catch (IOException e) {
				throw new Md5Exception(e);
			}
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new Md5Exception(e);
		}
		md.update(convertedTarget);
		byte[] result = md.digest();
		return byteToHexString(result);
	}

	public static String byteToHexString(byte[] target) {
		StringBuffer buffer = new StringBuffer();
		if (target == null)
			throw new IllegalArgumentException("字节数组不能为NULL。");
		for (byte b : target) {
			String hex = Integer.toHexString((int) b & 0xff);
			buffer.append(hex);
		}
		return buffer.toString();
	}

	public static byte[] serialize(Serializable obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			return baos.toByteArray();
		} finally {
			baos.close();
		}
	}

	public static byte[] serializeByKryo(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			Output output = new Output(baos);
			Kryo kryo = new Kryo();
			kryo.setReferences(true);
			kryo.writeObject(output, obj);
			return baos.toByteArray();
		} finally {
			baos.close();
		}
	}
}
