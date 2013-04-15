package org.csp.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.csp.store.AbstractStore;
import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Proxy;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-四月-2013 21:42:58
 */
public class FTPStore extends AbstractStore {

	private String hostName;
	private int port;
	private String userName;
	private String password;
	private Proxy proxy;
	private String dir;
	private FTPClient client;

	public FTPStore() {
	}

	public FTPStore(String hostName, int port, String userName,
			String password, String dir) {
		setHostName(hostName);
		setPort(port);
		setUserName(userName);
		setPassword(password);
		setDir(dir);
	}

	public FTPStore(String hostName, int port, String userName,
			String password, String dir, Proxy proxy) {
		setHostName(hostName);
		setPort(port);
		setUserName(userName);
		setPassword(password);
		setDir(dir);
		setProxy(proxy);
	}

	private void connect() throws LoginFailedException, StoreException {
		if (proxy != null)
			proxy.enableProxy(true);
		if(client == null)
			client = new FTPClient();
		try {
			client.connect(hostName, port);
			boolean login = client.login(userName, password);
			if (!login) {
				throw new LoginFailedException("FTP login failed for user "
						+ userName);
			}
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	private void disconnect() throws StoreException {
		try {
			client.logout();
			if (client.isConnected())
				client.disconnect();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	protected boolean save(String key, InputStream value)
			throws LoginFailedException, StoreException {
		connect();
		boolean result = false;
		try {
			changeWorkingDirectory();
			result = client.storeFile(key, value);
			if (!result)
				throw new StoreException("存储文件" + key + "失败");
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			try {
				value.close();
			} catch (IOException e) {
				throw new StoreException(e);
			}
			disconnect();
		}
		return result;
	}

	/**
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws StoreException
	 */
	private boolean changeWorkingDirectory() throws StoreException {
		boolean result = false;
		if (dir != null) {
			try {
				result = client.changeWorkingDirectory(dir);
			} catch (IOException e) {
				throw new StoreException(e);
			}
			if (!result)
				throw new StoreException("目录" + dir + "不存在.");
		}
		return result;
	}

	@Override
	protected InputStream get(String key) throws LoginFailedException, StoreException {
		connect();
		changeWorkingDirectory();
		try{
			return client.retrieveFileStream(key);
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	protected List<String> listKeys() throws StoreException,
			LoginFailedException {
		List<String> keys = new ArrayList<String>();
		connect();
		changeWorkingDirectory();
		try {
			FTPFile[] files = client.listFiles();
			for(FTPFile file : files){
				if(file.isDirectory())
					continue;
				keys.add(file.getName());
			}
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			disconnect();
		}

		return keys;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public Proxy getProxy() {
		return proxy;
	}
}