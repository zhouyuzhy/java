import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

import org.csp.exception.KeyPairException;
import org.csp.keypair.IKeyPairFactory;
import org.csp.keypair.KeyPairFactoryAdaptor;
import org.csp.keypair.KeyPairType;


public class Main {
	public static void main(String[] args) throws KeyPairException {
		IKeyPairFactory keyPairFactory = new KeyPairFactoryAdaptor(KeyPairType.PEM, "");
		PublicKey key = keyPairFactory.generatePublicKey("C:/Users/zhoushaoyu/Desktop/keys/github-prikey");
		System.out.println(((RSAPublicKey)key).getPublicExponent().toString(10));
	}
}
