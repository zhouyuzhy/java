package org.csp.keypair.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import junit.framework.TestCase;

import org.csp.keypair.IKeyPairFactory;
import org.csp.keypair.KeyPairFactoryAdaptor;
import org.csp.keypair.KeyPairType;

import sun.misc.BASE64Decoder;

public class OpensshKeyPairFatoryTest extends TestCase {
	public static final String KEY = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQCW6qYq6m8gVOWLyTB1JGl1aLrJDOCIfErXWNUsNeUXg4UdAtSbkiA+Ta9Nx6oMR4w+OkPbxyivnzkZt1YpmDxrm1z99z81/VyVw+lue+3neRjTgfGMascG+46b7DpEKLXlfS2hwOA+4ooRIeR+LbQZVovy5SP6ZTngskiqcySYqQ== RSA-1024";
	
	
	public void testGeneratePublicKeyWithBase64() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		IKeyPairFactory ikpf = new KeyPairFactoryAdaptor(KeyPairType.OPENSSH, "");
		PublicKey key = ikpf.generatePublicKey(KEY.getBytes());
		assertTrue(key instanceof RSAPublicKey);
		RSAPublicKey rsaKey = (RSAPublicKey)key;
		assertEquals(new BigInteger("105977239137449216800686938089365893516910816681847284853601054611426548497746217444050895607303221774362312783002807200161332294205831102044669933640298731470284440316356445345556785055997606691728701983653224605325253861690955380005991175103358535225681665336955087550990819899775469364786729168086503823529"),
				rsaKey.getModulus());
		assertEquals(new BigInteger("65537"),
				rsaKey.getPublicExponent());
	}
	
	public void testGeneratePublicKeyWithBinary() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		byte[] keyBinary = new BASE64Decoder().decodeBuffer(KEY.split(" ")[1]);
		IKeyPairFactory ikpf = new KeyPairFactoryAdaptor(KeyPairType.OPENSSH, "");
		PublicKey key = ikpf.generatePublicKey(keyBinary);
		assertTrue(key instanceof RSAPublicKey);
		RSAPublicKey rsaKey = (RSAPublicKey)key;
		assertEquals(new BigInteger("105977239137449216800686938089365893516910816681847284853601054611426548497746217444050895607303221774362312783002807200161332294205831102044669933640298731470284440316356445345556785055997606691728701983653224605325253861690955380005991175103358535225681665336955087550990819899775469364786729168086503823529"),
				rsaKey.getModulus());
		assertEquals(new BigInteger("65537"),
				rsaKey.getPublicExponent());
	}
}
