package org.csp.keypair.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import junit.framework.TestCase;

public class PEMKeyPairFactoryImplTest extends TestCase {

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
	
	public void testGeneratePrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		KeyPairFactoryImpl kpfi = new PEMKeyPairFactoryImpl("");
		PrivateKey key = kpfi.generatePrivateKey(PEM_KEY.getBytes());
		assertTrue(key instanceof RSAPrivateKey);
		RSAPrivateKey rsaKey = (RSAPrivateKey)key;
		assertEquals(new BigInteger("105977239137449216800686938089365893516910816681847284853601054611426548497746217444050895607303221774362312783002807200161332294205831102044669933640298731470284440316356445345556785055997606691728701983653224605325253861690955380005991175103358535225681665336955087550990819899775469364786729168086503823529"),
				rsaKey.getModulus());
		assertEquals(new BigInteger("61018129325610094248395882049287160321148553590686565553287184257863029754397603934171788376733444778274677061107892135601075923364860015022866095121580657435104768996856821093134698385516602180113882903477421043241436696767104757492248085285254596116113970633499655208148132045456175088366165602813443080705"),
				rsaKey.getPrivateExponent());
	}
	
	public void testGeneratePublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		KeyPairFactoryImpl kpfi = new PEMKeyPairFactoryImpl("");
		PublicKey key = kpfi.generatePublicKey(PEM_KEY.getBytes());
		assertTrue(key instanceof RSAPublicKey);
		RSAPublicKey rsaKey = (RSAPublicKey)key;
		assertEquals(new BigInteger("105977239137449216800686938089365893516910816681847284853601054611426548497746217444050895607303221774362312783002807200161332294205831102044669933640298731470284440316356445345556785055997606691728701983653224605325253861690955380005991175103358535225681665336955087550990819899775469364786729168086503823529"),
				rsaKey.getModulus());
		assertEquals(new BigInteger("65537"),
				rsaKey.getPublicExponent());
	}
	
}
