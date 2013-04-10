package org.csp.store.model;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Proxy {

	private String host;
	private Integer port;
	private String userName;
	private String password;

	public Proxy() {
	}

	public Proxy(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public Proxy(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public void enableProxy(boolean useProxy) {
		if (useProxy) {
			if (host != null)
				System.setProperty("socksProxyHost", host);
			if (port != null)
				System.setProperty("socksProxyPort", String.valueOf(port));
			if (userName != null && password != null)
				Authenticator.setDefault(new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password
								.toCharArray());
					}
				});
		} else {
			System.clearProperty("socksProxyHost");
			System.clearProperty("socksProxyPort");
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
