package org.test.java;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws StoreException, LoginFailedException, IOException {
//		IStore store = new FTPStore("10.120.41.44", 21, "zsy", "1234", ".",
//				new Proxy("127.0.0.1", 1080));
		
//		store.save(new Key("test"), new Value(new ByteArrayInputStream("test".getBytes())));
//		System.out.println(store.list());
//		System.out.println(store.exist(new Key("test")));
//		Value value = store.get(new Key("test"));
//		byte[] result = new byte[1024];
//		int length = value.getvalue().read(result, 0, 1024);
//		System.out.println(new String(result, 0, length));
		
	}

	protected static void ftpRequest() throws SocketException, IOException {
		// System.setProperty("ftp.proxyHost", "127.0.0.1");
		// System.setProperty("ftp.proxyPort", "1080");
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "1080");
		FTPClient client = new FTPClient();
		// FTPClient client = new FTPHTTPClient("127.0.0.1", 1080);
		client.connect("10.120.41.44", 21);
		boolean login = client.login("zsy", "1234");
		if (!login) {
			System.err.println("µÇÂ¼Ê§°Ü");
			return;
		}
		client.setFileType(FTP.BINARY_FILE_TYPE);
		client.enterLocalPassiveMode();
		for (FTPFile f : client.listFiles()) {
			System.out.println(f.toFormattedString());
		}
		client.disconnect();
	}

	/**
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected static void httpRequest() throws MalformedURLException,
			IOException {
		// http://docs.oracle.com/javase/6/docs/technotes/guides/net/proxies.html
		System.setProperty("socksProxyHost", "127.0.0.1");
		System.setProperty("socksProxyPort", "1080");
		// Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(
		// "127.0.0.1", 1080));
		URL url = new URL("http://10.120.240.46:80");
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		uc.connect();
		int size = 10240;
		byte[] temp = new byte[size];
		int length = 0;
		while ((length = uc.getInputStream().read(temp, 0, size)) != -1) {
			System.out.println(new String(temp, 0, length));
		}
	}
}
