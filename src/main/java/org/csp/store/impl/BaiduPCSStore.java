package org.csp.store.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.csp.store.AbstractStore;
import org.csp.store.exception.StoreException;
import org.csp.store.util.Utils;

/**
 * @author zhoushaoyu
 * @version 1.0
 * @created 02-四月-2013 21:42:57
 */
public class BaiduPCSStore extends AbstractStore {

	private static final String API_KEY = "nlxLheRjqaKdtrhySGt6LV4l";
	private static final String AUTHORIZE_URL = String
			.format("https://openapi.baidu.com/oauth/2.0/authorize?response_type=token&client_id=%1$s&redirect_uri=oob&scope=netdisk",
					API_KEY);
	protected static String LIST_URL = "https://pcs.baidu.com/rest/2.0/pcs/file?method=list&access_token=${access_token}&path=%2fapps%2f${dir}";
	protected static String UPLOAD_URL = "https://pcs.baidu.com/rest/2.0/pcs/file?method=upload&path=%2fapps%2f${dir}%2f${file}&access_token=${access_token}&ondup=overwrite";
	protected static String DOWNLOAD_URL = "https://pcs.baidu.com/rest/2.0/pcs/file?method=download&access_token=${access_token}&path=%2Fapps%2F${dir}%2F${file}";
	private String dir;
	private ConnectionFactory connectionFactory = new ConnectionFactory();

	public BaiduPCSStore() {
	}

	public BaiduPCSStore(String authUrl, String dir) throws StoreException {
		setAuthUrl(authUrl);
		setDir(dir);
	}
	
	public BaiduPCSStore(String authUrl, String dir, ConnectionFactory connectionFactory) throws StoreException {
		setAuthUrl(authUrl);
		setDir(dir);
		setConnectionFactory(connectionFactory);
	}

	private String replacePlaceHolder(String target, String key, String value) {
		return target.replaceAll(Pattern.quote(key), value);
	}

	public static class ConnectionFactory {
		
		public ConnectionFactory() {
		}
		
		public HttpURLConnection putMethodConnection(String url)
				throws StoreException {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url)
						.openConnection();
				conn.setRequestMethod("PUT");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				conn.connect();
				return conn;
			} catch (IOException e) {
				throw new StoreException(e);
			}
		}

		public HttpURLConnection getMethodConnection(String url) throws StoreException{
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
				conn.connect();
				return conn;
			} catch (IOException e) {
				throw new StoreException(e);
			}
		}
	}

	private void auth(HttpURLConnection conn) throws StoreException {
		try {
			int code = conn.getResponseCode();
			if (code == 401) {
				throw new StoreException("認證失敗。請重新授權，" + AUTHORIZE_URL);
			} else if (code != 200) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Utils.copyStream(conn.getInputStream(), baos);
				throw new StoreException(new String(baos.toByteArray()));
			}
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	protected boolean save(String key, InputStream value) throws StoreException {
		UPLOAD_URL = replacePlaceHolder(UPLOAD_URL, "${file}", key);
		HttpURLConnection conn = connectionFactory.putMethodConnection(UPLOAD_URL);
		try {
			Utils.copyStream(value, conn.getOutputStream());
			conn.getOutputStream().close();
			auth(conn);
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			StoreException except = null;
			try {
				value.close();
			} catch (IOException e) {
				except = new StoreException(e);
			}
			conn.disconnect();
			if (except != null)
				throw except;
		}
		return true;
	}

	@Override
	protected InputStream get(String key) throws StoreException {
		DOWNLOAD_URL = replacePlaceHolder(DOWNLOAD_URL, "${file}", key);
		HttpURLConnection conn = connectionFactory.getMethodConnection(DOWNLOAD_URL);
		auth(conn);
		try {
			return conn.getInputStream();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	protected List<String> listKeys() throws StoreException {
		HttpURLConnection conn = connectionFactory.getMethodConnection(LIST_URL);
		auth(conn);
		String result;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Utils.copyStream(conn.getInputStream(), baos);
			result = new String(baos.toByteArray());
		} catch (IOException e) {
			throw new StoreException(e);
		} finally {
			conn.disconnect();
			try {
				conn.getInputStream().close();
			} catch (IOException e) {
				throw new StoreException(e);
			}
		}
		List<String> allKeys = new ArrayList<String>();
		Map<?, ?> map = Utils.parseFromJson(result);
		if (map == null)
			throw new StoreException("服务返回结果不满足要求，" + result);
		List<?> files = (List<?>) map.get("list");
		if (files == null)
			throw new StoreException("服务返回结果不满足要求，" + result);
		for (Object file : files) {
			String path = file instanceof Map ? (String) ((Map<?, ?>) file)
					.get("path") : "";
			if (path == null)
				throw new StoreException("服务返回结果不满足要求，" + result);
			String name = path.substring(path.lastIndexOf('/') + 1);
			allKeys.add(name);
		}
		return allKeys;
	}

	public void setAuthUrl(String authUrl) throws StoreException {
		String[] result = authUrl.split("#");
		if (result.length <= 1)
			throw new IllegalArgumentException("非法认证URL： " + authUrl);
		authUrl = result[1];
		result = authUrl.split("&");
		for (String item : result) {
			if (item == null)
				continue;
			String[] keyValue = item.split("=");
			if (keyValue.length != 2)
				continue;
			if ("access_token".equals(keyValue[0])) {
				String accessToken = keyValue[1];
				String key = "${access_token}";
				LIST_URL = replacePlaceHolder(LIST_URL, key, accessToken);
				UPLOAD_URL = replacePlaceHolder(UPLOAD_URL, key, accessToken);
				DOWNLOAD_URL = replacePlaceHolder(DOWNLOAD_URL, key,
						accessToken);
			} else if ("session_secret".equals(keyValue[0])) {
				// Nothing to do
			} else if ("session_key".equals(keyValue[0])) {
				// Nothing to do
			} else if ("scope".equals(keyValue[0])) {
				if (keyValue[1] == null || !keyValue[1].contains("netdisk"))
					throw new StoreException("未被允许操作网盘。請重新授權，" + AUTHORIZE_URL);
			}
		}
	}

	public void setDir(String dir) {
		this.dir = dir;
		String key = "${dir}";
		LIST_URL = replacePlaceHolder(LIST_URL, key, dir);
		UPLOAD_URL = replacePlaceHolder(UPLOAD_URL, key, dir);
		DOWNLOAD_URL = replacePlaceHolder(DOWNLOAD_URL, key, dir);
	}
	
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public String getDir() {
		return dir;
	}

}