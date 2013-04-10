package org.csp.store.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;

public class TestFTPStore extends TestCase {
	private IMocksControl control;
	private FTPClient mockClient;
	private FTPStore store;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		control = EasyMock.createControl();
		mockClient = control.createMock(FTPClient.class);
		store = new FTPStore();
		Field field = FTPStore.class.getDeclaredField("client");
		field.setAccessible(true);
		field.set(store, mockClient);
		EasyMock.expect(mockClient.login((String) null, (String) null))
				.andReturn(true);
		mockClient.connect((String) null, 0);
		EasyMock.expect(mockClient.setFileType(FTP.BINARY_FILE_TYPE))
				.andReturn(true);
		mockClient.enterLocalPassiveMode();
		EasyMock.expect(mockClient.logout()).andReturn(true);
		EasyMock.expect(mockClient.isConnected()).andReturn(true);
		mockClient.disconnect();
	}

	public void testSave() throws StoreException, LoginFailedException,
			IOException {
		InputStream is = new ByteArrayInputStream("test".getBytes());
		EasyMock.expect(mockClient.storeFile("test", is)).andReturn(true)
				.once();
		EasyMock.replay(mockClient);
		boolean result = store.save(new Key("test"), new Value(is));
		assertEquals(true, result);
		EasyMock.verify(mockClient);
	}

	public void testNotExist() throws StoreException, LoginFailedException,
			IOException {
		EasyMock.expect(mockClient.listFiles()).andReturn(new FTPFile[0])
				.once();
		EasyMock.replay(mockClient);
		boolean result = store.exist(new Key("test"));
		assertEquals(false, result);
		EasyMock.verify(mockClient);

	}

	public void testExist() throws IOException, StoreException,
			LoginFailedException {
		FTPFile file = new FTPFile();
		file.setName("test");
		EasyMock.expect(mockClient.listFiles())
				.andReturn(new FTPFile[] { file }).once();
		EasyMock.replay(mockClient);
		boolean result = store.exist(new Key("test"));
		assertEquals(true, result);
		EasyMock.verify(mockClient);
	}

	public void testGet() throws StoreException, LoginFailedException,
			IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream("test".getBytes());
		EasyMock.expect(mockClient.retrieveFileStream("test")).andReturn(bais)
				.once();
		EasyMock.replay(mockClient);
		Value value = store.get(new Key("test"));
		InputStream is = value.getvalue();
		byte[] result = new byte[1024];
		int length = is.read(result, 0, 1024);
		is.close();
		assertEquals(4, length);
		assertEquals("test", new String(result, 0, length));
	}

	public void testListFiles() throws IOException, StoreException, LoginFailedException {
		FTPFile file = new FTPFile();
		file.setName("test");
		EasyMock.expect(mockClient.listFiles())
				.andReturn(new FTPFile[] { file }).once();
		EasyMock.replay(mockClient);
		List<Key> keys = store.list();
		assertEquals(Arrays.asList(new Key("test")), keys);
		EasyMock.verify(mockClient);
	}
}
