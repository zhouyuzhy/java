package org.csp.store.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;

public class TestFileStore extends TestCase {

	static final String tempDir = System.getProperty("java.io.tmpdir");
	static final String storeDir = tempDir + "FileStore" + File.separator;
	private FileStore fileStore;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		fileStore = new FileStore(storeDir);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		File directory = new File(storeDir);
		deleteDirectory(directory);
	}

	private void deleteDirectory(File directory) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory())
				deleteDirectory(file);
			else
				file.delete();
		}
		directory.delete();
	}

	public void testCreatedDirectory() {
		File dir = new File(storeDir);
		assertTrue(dir.exists());
		assertTrue(dir.isDirectory());
	}

	public void testSave() throws StoreException, IOException, LoginFailedException {
		ByteArrayInputStream bais = new ByteArrayInputStream("test".getBytes());
		boolean result = fileStore
				.save(new Key("test", "csp"), new Value(bais));
		assertTrue(result);
		assertEquals(0, bais.available());

		File file = new File(storeDir + "test_csp");
		assertTrue(file.exists());
		InputStream is = new FileInputStream(file);
		byte[] readResult = new byte[is.available()];
		is.read(readResult);
		assertEquals("test", new String(readResult));
		is.close();
	}

	public void testExist() throws IOException, StoreException, LoginFailedException {
		File file = new File(storeDir + "test_csp");
		assertTrue(file.createNewFile());

		assertTrue(fileStore.exist(new Key("test", "csp")));
	}

	public void testGet() throws IOException, StoreException, LoginFailedException {
		File file = new File(storeDir + "test_csp");
		OutputStream os = new FileOutputStream(file);
		os.write("test".getBytes());
		os.close();

		Value value = fileStore.get(new Key("test", "csp"));
		assertEquals("test", new String(value.read()));
	}

	public void testListKeys() throws IOException, StoreException, LoginFailedException {
		for (int i = 0; i < 3; i++) {
			File file = new File(storeDir + "test_csp" + i);
			file.createNewFile();
		}

		List<Key> keys = fileStore.list();
		List<Key> expected = Arrays.asList(new Key("test", "csp0"), new Key(
				"test", "csp1"), new Key("test", "csp2"));
		assertEquals(expected, keys);
	}

}
