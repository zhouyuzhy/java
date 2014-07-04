package org.test.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtils {

    /**
     * log4j logger
     */
    private static Logger logger = Logger.getLogger(HttpUtils.class);

    /**
     * 调试模式
     */
    private static boolean debug = false;

    /**
     * 线程安全的连接管理员
     */
    public static final PoolingClientConnectionManager CONN_MANAGER = new PoolingClientConnectionManager();

    /**
     * HttpClient实例
     */
    public static final HttpClient HTTP_CLIENT;

    /**
     * thread to check http connection state, it will close idle or expire connection
     */
    private static Thread checkConnThread;

    /**
     * the state of checkConnThread
     */
    private static volatile boolean running = false;

    static {
        CONN_MANAGER.setMaxTotal(60); //整个连接管理器的最大连接数设
        CONN_MANAGER.setDefaultMaxPerRoute(50); //每个目标主机的最大连接数
        HTTP_CLIENT = new DefaultHttpClient(CONN_MANAGER);
        HTTP_CLIENT.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000); //设置连接超时时间为5秒
        HTTP_CLIENT.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 10000); //设置读取的超时时间为10000秒。

        TrustManager easyTrustManager = new X509TrustManager() {

			public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
					throws java.security.cert.CertificateException {
				// To change body of implemented methods use File | Settings
				// | File Templates.
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s)
					throws java.security.cert.CertificateException {
				// To change body of implemented methods use File | Settings
				// | File Templates.
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[0]; // To
																	// change
																	// body
																	// of
																	// implemented
																	// methods
																	// use
																	// File
																	// |
																	// Settings
																	// |
																	// File
																	// Templates.
			}
		};

		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { easyTrustManager }, null);
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
		Scheme sch = new Scheme("https", 443, sf);
		HTTP_CLIENT.getConnectionManager().getSchemeRegistry().register(sch);
    }

    static class CheckConnThread implements Runnable {

        /**
         * the thread sleep interval in millisecond, default 5 minutes
         */
        private long checkInterval = 5 * BaseConst.MINUTE;

        public CheckConnThread() {
        }

        /**
         *
         * @param checkInterval the thread sleep interval in millisecond
         */
        public CheckConnThread(long checkInterval) {
            this.checkInterval = checkInterval;
        }

        public void run() {
            while(running) {
                try {
                    Thread.sleep(checkInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Start http conn thecking, close expired or idle connections.");
                //关闭过期连接
                CONN_MANAGER.closeExpiredConnections();
                //关闭空闲连接
                CONN_MANAGER.closeIdleConnections(5, TimeUnit.MINUTES);
            }
        }
    }

    /**
     * start a thread to check http connection, it will close idle or expiere connections
     * @param checkInterval
     */
    public static synchronized void startCheckConnThread(long checkInterval) {
        running = true;
        if (checkConnThread == null || !checkConnThread.isAlive()) {
            checkConnThread = new Thread(new CheckConnThread(checkInterval), "Thread-CheckConnThread");
            checkConnThread.start();
        }
    }

    /**
     * stop the check http connection thread
     */
    public static void stopCheckConnThread() {
        running = false;
    }

    /**
     * 发送get请求
     * @param url 请求url
     * @return 请求返回结果的字符串表示
     */
    public static Response httpGet(String url) {
        return httpGet(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
    }

    /**
     * 发送get请求
     * @param url 请求url
     * @param params 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpGet(String url, Map<String, ?> params) {
        return httpGet(url, params, Collections.EMPTY_MAP);
    }

    /**
     *
     * @param url
     * @param params
     * @param headerMap
     * @return
     */
    public static Response httpGet(String url, Map<String, ?> params, Map<String, String> headerMap) {
        Assert.notNull(params);
        Assert.notNull(headerMap);

        HttpGet httpget = new HttpGet(appendParameter2Url(url, params));
        for (Map.Entry<String, String> headerEntry : headerMap.entrySet()) {
            httpget.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        return httpExecute(httpget);
    }

    /**
     * 发送post请求
     * @param url 请求url
     * @param content 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpPost(String url, String content) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(content, BaseConst.UTF8));
        return httpExecute(httpPost);
    }
    
    /**
     * 发送post请求
     * @param url 请求url
     * @param content 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpPost(String url, String content, Map<String, String> headerMap) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(content, BaseConst.UTF8));
        for (Map.Entry<String, String> headerEntry : headerMap.entrySet()) {
            httpPost.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        return httpExecute(httpPost);
    }

    /**
     * 发送post请求
     * @param url 请求url
     * @param params 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpPost(String url, Map<String, String> params) {
        return httpPost(url, params, Collections.EMPTY_MAP);
    }

    /**
     * 发送post请求
     * @param url 请求url
     * @param params 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpPost(String url, Map<String, String> params, Map<String, String> headerMap) {
        Assert.notNull(params);
        Assert.notNull(headerMap);

        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null) {
            for (Entry<String, String> e : params.entrySet()) {
                nvps.add(new BasicNameValuePair(e.getKey(), e.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, BaseConst.UTF8));

        for (Map.Entry<String, String> headerEntry : headerMap.entrySet()) {
            httpPost.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }

        return httpExecute(httpPost);
    }


    /**
     * 发送post请求
     * @param url 请求url
     * @param params 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpDelete(String url, Map<String, ?> params) {
        HttpDelete httpDelete = new HttpDelete(appendParameter2Url(url, params));
        return httpExecute(httpDelete);
    }

    /**
     * 发送post请求
     * @param url 请求url
     * @param params 请求参数
     * @return 请求返回结果的字符串表示
     */
    public static Response httpPut(String url, Map<String, ?> params) {
        HttpPut httpPut = new HttpPut(appendParameter2Url(url, params));
        return httpExecute(httpPut);
    }


    private static Response httpExecute(HttpUriRequest request) {
        if (debug) {
            logger.info("HttpRequestUri:" + request.getURI());
        }

        Response response = new Response();
        HttpEntity entity = null;
        try {
            HttpResponse httpResponse = HTTP_CLIENT.execute(request);
            entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity, BaseConst.UTF8);
            response.code = httpResponse.getStatusLine().getStatusCode();
            response.content = content;
        } catch (Exception e) {
            logger.error("HttpExecuteError：" + request.getURI());
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * 将参数拼接到url后面
     * @param url url
     * @param params 参数
     * @return url with parameters
     */
    private static String appendParameter2Url(String url, Map<String, ?> params) {
        if (params.size() > 0) {
            StringBuilder sb = new StringBuilder(url);
            if (!url.contains("?")) {
                sb.append("?");
            }
            Iterator<String> i =  params.keySet().iterator();
            while (i.hasNext()) {
                String key = i.next();
                sb.append(key);
                sb.append("=");
                sb.append(params.get(key));
                if (i.hasNext()) {
                    sb.append("&");
                }
            }
            return sb.toString();
        }
        return url;
    }

    /**
     * @param debug the debug to set
     */
    public static void setDebug(boolean debug) {
        HttpUtils.debug = debug;
    }

    public static class Response {

        /**
         * response http status code
         * if code = -1, http execute error
         */
        public int code = -1;

        /**
         * response http content
         */
        public String content;
        
        /**
         * 判断请求是否成功的通用处理
         * @return
         */
        public boolean isRequestSuccess(){
            return isRequestSuccess(null);
        }
        
        /**
         * 判断请求是否成功的通用处理
         * @param requestId 请求的唯一Id(可以是url,或其他的标识),会记录到日志中
         * @return
         */
        public boolean isRequestSuccess(String requestId) {
            if(requestId == null || requestId.trim().isEmpty()) {
                requestId = "";
            } else {
                requestId = "[" + requestId + "]";
            }
            if(code != 200) {
                logger.error(failLog(requestId));
                return false;
            }
            if(content == null || content.trim().isEmpty()) {
                logger.error(failLog(requestId));
                return false;
            }
            return true;
        }
        
        private String failLog(String requestId){
            String ret = "request " + requestId + " fail, " + toString();
            return ret;
        }
        
        @Override
        public String toString() {
            return "HttpUtils.Response [code=" + code + ", content=" + content + "]";
        }

    }




}
