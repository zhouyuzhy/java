package org.test.java;

import org.csp.store.util.Utils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Utils.printBytes( new String(new byte[]{-35, -2}).getBytes() );
        System.out.println(new String(new byte[]{-35, -2}));
    }
}
