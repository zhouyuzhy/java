package com.zy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class HttpsConnectionUtil
{
	private static final String DEFAULT_CHARSET = "UTF-8";
	protected static Logger log = Logger.getLogger(HttpsConnectionUtil.class);

	public static String httpsUrlRequestGet(String url, String charSet, int timeOut)
	{
		if (0 == timeOut)
		{
			timeOut = 5000;
		}
		if(StringUtils.isBlank(charSet))
		{
			charSet = DEFAULT_CHARSET;
		}
		String ret = null;
		HostnameVerifier hv = new HostnameVerifier() {

			public boolean verify(String hostname, SSLSession session)
			{
				return hostname.equals(session.getPeerHost());
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
		HttpURLConnection urlConnect = null;
		try
		{
			//Create a trust manager that does not validate certificate chains
			TrustManager[] trustAllCerts = new TrustManager[]
			{ new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType)
				{
					//System.out.println("checkServerTrusted");
				}

				public void checkClientTrusted(X509Certificate[] chain, String authType)
				{
					//System.out.println("checkClientTrusted");
				}
			} };

			//Install the all-trusting trust manager
			SSLContext sc = SSLContext.getInstance("TLS", "SunJSSE");
			sc.getProvider();
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			//Now you can access an https URL without having the certificate in the truststore
			URL urlClass = new URL(url);
			urlConnect = (HttpURLConnection) urlClass.openConnection();
			urlConnect.setRequestMethod("GET");
			urlConnect.setConnectTimeout(timeOut);
			urlConnect.setReadTimeout(timeOut);
			if(urlConnect.getResponseCode() != 200)
			{
				throw new IllegalStateException("" + urlConnect.getResponseCode());
			}
			InputStream in = urlConnect.getInputStream();
			ret = getReturnValueFromInputStream(in, charSet);
		}
		catch (IOException e)
		{
			log.fatal(e.getMessage(), e);
		}
		catch (KeyManagementException e)
		{
			log.fatal(e.getMessage(), e);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.fatal(e.getMessage(), e);
		}
		catch (NoSuchProviderException e)
		{
			log.fatal(e.getMessage(), e);
		}
		finally
		{
			if (urlConnect != null)
			{
				try
				{
					urlConnect.getOutputStream().close();
				}
				catch (IOException e)
				{
					log.fatal(e.getMessage(), e);
				}
				urlConnect.disconnect();
			}
		}
		return ret;
	}

	public static String getReturnValueFromInputStream(InputStream is, String charSet)
	{
		if (StringUtils.isBlank(charSet))
		{
			charSet = DEFAULT_CHARSET;
		}

		StringBuilder sb = new StringBuilder();
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(is, charSet));
			char[] temp = new char[1024];
			int charCount = 0;
			while ((charCount = br.read(temp)) != -1)
			{
				sb.append(new String(temp, 0, charCount));
			}
			return sb.toString();
		}
		catch (UnsupportedEncodingException e)
		{
			log.fatal(e.getMessage(), e);
			return null;
		}
		catch (Exception e)
		{
			log.fatal(e.getMessage(), e);
			return null;
		}
		finally
		{
			try
			{
				if (is != null)
					is.close();
			}
			catch (IOException e)
			{
				log.fatal(e.getMessage(), e);
			}
		}
	}
}
