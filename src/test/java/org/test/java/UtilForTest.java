package org.test.java;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;

import junit.framework.TestCase;

import org.csp.exception.KeyPairException;
import org.csp.keypair.impl.KeyPairFactoryImpl;
import org.csp.keypair.impl.PEMKeyPairFactoryImpl;

public class UtilForTest {
	static final String ALGORITHM = "AES";

	private static final String PEM_KEY = "-----BEGIN RSA PRIVATE KEY-----\n"
			+"MIICWwIBAAKBgQCW6qYq6m8gVOWLyTB1JGl1aLrJDOCIfErXWNUsNeUXg4UdAtSb"
			+"kiA+Ta9Nx6oMR4w+OkPbxyivnzkZt1YpmDxrm1z99z81/VyVw+lue+3neRjTgfGM"
			+"ascG+46b7DpEKLXlfS2hwOA+4ooRIeR+LbQZVovy5SP6ZTngskiqcySYqQIDAQAB"
			+"AoGAVuSIBFGy5iS7ff66/deXONiyIyxc43jdBpzJMIlffGhnbkrA60n5cqX794as"
			+"rCFN6E5X3+UN0gCVOe9LlutmbZFhb8pd5T02BoEwdWaP2husmsYb3CX+22/5JGjs"
			+"typZemVf/SddcUvDYlVhf9X7oiUUw80gn3ftwvwHQwwMggECQQDm2cAFH4LKbzgq"
			+"TCAwa1I+j7fpcLaSJge5s8vPwOcdDQ10AQt2uVp8QzcunSRR5LBvSgNS1ryew9G7"
			+"IJAQ/OVJAkEAp1uZlwMq8s6DLVEBVxNenjTGh2cGp1c27ID/1O60ZLTR6Rvacw6x"
			+"3JPMcb1j4DXFWvqBH4/tYI1Zg1woopv4YQJAG9hOChAn9YT+0FNIWq7HR9aMB3Na"
			+"AjzmF6cxQUjyV5W4drKdkF+BI5Xz9QJtXBdqlLHnvrk6HShfhuPBnNr0SQJAW7N/"
			+"VxWvRNgPXJdUSqgcYke8uJilNrA9Mh4FEGGzirLhif9vFThqENkTXwdNYJ9WAmHc"
			+"urScdxk8gRPQWDlqQQJATfKR+7vSQddrQOaDa79fWMb9t0INroEjUpaPuFLzVNyO"
			+"97gpoZjznSe4wDezOuJrU8XN9UEtXiXsyjr/GVMxcg==\n"
			+"-----END RSA PRIVATE KEY-----";
	
	
	public static Key generateAesKey() {
		KeyGenerator factory = null;
		try {
			factory = KeyGenerator.getInstance(ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			TestCase.fail("Generate key failed." + e);
			return null;
		}
		factory.init(128);
		return factory.generateKey();
	}
	
	
	public static KeyPair generateRsaKey() throws KeyPairException{
		KeyPairFactoryImpl kpfi = new PEMKeyPairFactoryImpl("");
		PrivateKey priKey = kpfi.generatePrivateKey(PEM_KEY.getBytes());
		PublicKey pubKey = kpfi.generatePublicKey(PEM_KEY.getBytes());
		return new KeyPair(pubKey, priKey);
	}
	
	public static void assertBytes(byte[] expected, byte[] result){
		TestCase.assertNotNull(result);
		TestCase.assertEquals(result.length, expected.length);
		for(int i=0;i < expected.length;i++){
			TestCase.assertEquals(expected[i], result[i]);
		}
	}
	
}
