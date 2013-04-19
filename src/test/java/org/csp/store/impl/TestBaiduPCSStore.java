package org.csp.store.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.impl.BaiduPCSStore.ConnectionFactory;
import org.csp.store.model.Key;
import org.csp.store.model.Value;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;

public class TestBaiduPCSStore extends TestCase {
	private IMocksControl control;
	private static final String DIR = "test";
	private ConnectionFactory factory;
	private HttpURLConnection putConn;
	private HttpURLConnection getConn;
	private BaiduPCSStore store;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		control = EasyMock.createControl();
		factory = control.createMock(ConnectionFactory.class);
		putConn = control.createMock(HttpURLConnection.class);
		getConn = control.createMock(HttpURLConnection.class);
		store = new BaiduPCSStore(
				"http://authUrl#access_token=access_token&scope=netdisk", DIR,
				factory);
	}

	private void mockPutConnection(String url, String file)
			throws StoreException, IOException {
		url = url.replaceAll(Pattern.quote("${access_token}"), "access_token")
				.replaceAll(Pattern.quote("${dir}"), DIR);
		if (file != null)
			url = url.replaceAll(Pattern.quote("${file}"), file);
		EasyMock.expect(factory.putMethodConnection(url)).andReturn(
				putConn);
		putConn.disconnect();
		EasyMock.expect(putConn.getResponseCode()).andReturn(200);
	}

	private void mockGetConnection(String url, String file)
			throws StoreException, IOException {
		if(!BaiduPCSStore.DOWNLOAD_URL.equals(url))
			getConn.disconnect();
		url = url.replaceAll(Pattern.quote("${access_token}"), "access_token")
				.replaceAll(Pattern.quote("${dir}"), DIR);
		if (file != null)
			url = url.replaceAll(Pattern.quote("${file}"), file);
		EasyMock.expect(factory.getMethodConnection(url)).andReturn(getConn);
		
		EasyMock.expect(getConn.getResponseCode()).andReturn(200);
	}

	private void assertByteArrayEquals(byte[] expected, byte[] actual) {
		if (expected == null)
			assertNull(actual);
		else
			assertNotNull(actual);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

	public void testSave() throws IOException, StoreException,
			LoginFailedException {
		mockPutConnection(BaiduPCSStore.UPLOAD_URL, "test");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		EasyMock.expect(putConn.getOutputStream()).andReturn(baos).times(2);
		EasyMock.replay(putConn);
		byte[] toSave = new byte[] { 0x01, 0x02, 0x11, 0x1f };
		boolean result = store.save(new Key("test"), new Value(
				new ByteArrayInputStream(toSave)));
		assertTrue(result);
		assertByteArrayEquals(toSave, baos.toByteArray());
		EasyMock.verify(putConn);
	}

	public void testGet() throws StoreException, IOException,
			LoginFailedException {
		mockGetConnection(BaiduPCSStore.DOWNLOAD_URL, "test");
		byte[] getValue = new byte[] { 0x01, 0x02, 0x11, 0x1f };
		EasyMock.expect(getConn.getInputStream()).andReturn(
				new ByteArrayInputStream(getValue));
		EasyMock.expect(getConn.getContentLength()).andReturn(4);
		EasyMock.replay(getConn);
		InputStream is = store.get(new Key("test")).getvalue();
		assertEquals(4, is.available());
		byte[] actual = new byte[4];
		is.read(actual);
		assertByteArrayEquals(getValue, actual);
		EasyMock.verify(getConn);
	}

	public void testListKeys() throws StoreException, IOException {
		mockGetConnection(BaiduPCSStore.LIST_URL, null);
		byte[] getValue = "{\"list\":[{\"path\":\"/apps/test/file1\"}, {\"path\": \"/apps/test/file2\"}], \"test\":\"test\"}"
				.getBytes();
		EasyMock.expect(getConn.getInputStream())
				.andReturn(new ByteArrayInputStream(getValue)).times(2);
		EasyMock.replay(getConn);
		List<String> actual = store.listKeys();
		List<String> expected = Arrays.asList("file1", "file2");
		assertEquals(expected, actual);
		EasyMock.verify(getConn);
	}

	public void testExist() throws IOException, StoreException,
			LoginFailedException {
		mockGetConnection(BaiduPCSStore.LIST_URL, null);
		byte[] getValue = "{\"list\":[{\"path\":\"/apps/test/file1\"}, {\"path\": \"/apps/test/file2\"}], \"test\":\"test\"}"
				.getBytes();
		EasyMock.expect(getConn.getInputStream())
				.andReturn(new ByteArrayInputStream(getValue)).times(2);
		EasyMock.replay(getConn);
		boolean actual = store.exist(new Key("file1"));
		assertTrue(actual);
		EasyMock.verify(getConn);
	}

	public void testNotExist() throws IOException, StoreException,
			LoginFailedException {
		mockGetConnection(BaiduPCSStore.LIST_URL, null);
		byte[] getValue = "{\"list\":[{\"path\":\"/apps/test/file1\"}, {\"path\": \"/apps/test/file2\"}], \"test\":\"test\"}"
				.getBytes();
		EasyMock.expect(getConn.getInputStream())
				.andReturn(new ByteArrayInputStream(getValue)).times(2);
		EasyMock.replay(getConn);
		boolean actual = store.exist(new Key("test_not_exists"));
		assertFalse(actual);
		EasyMock.verify(getConn);
	}
}
