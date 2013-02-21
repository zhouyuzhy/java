

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.test.privatekey.DERPrivateKeyFactory;
import org.test.privatekey.IPrivateKeyFactory;
import org.test.privatekey.PEMPrivateKeyFactory;
import org.test.publickey.PublicKeyFactory;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
    	
    	
    	PublicKey pubkey = PublicKeyFactory.generatePublicKey("AAAAB3NzaC1yc2EAAAABJQAAAIEApirBEpGoldWVGmIrYhGFGI1X+I5apU6uX17n6GiLPfiRWAJVC8haPPOivURJbqozLxUgtUQ5rXRHReXk+HsvpNfTAU/eFca2n1xyeebeUkj/elOXoLml6WS42t7+08G9HqRp36EBxnBK6YNEm0CFyX4R6CXqC9ZzHFvqsBxhARU=");
    	System.out.println(pubkey);
    }
}
