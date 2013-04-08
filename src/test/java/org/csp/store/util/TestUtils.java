package org.csp.store.util;

import java.io.Serializable;

import junit.framework.TestCase;

import org.csp.store.exception.Md5Exception;

public class TestUtils extends TestCase {

	public void testByteToHexString() {
		String expected = "1f60";
		String result = Utils.byteToHexString(new byte[] { 0x1f, 0x06, 0x00 });
		assertEquals(expected, result);
	}

	public void testMd5String() throws Md5Exception {
		String expected = "e2fc714c4727ee9395f324cd2e7f331f";
		String result = Utils.md5("abcd");
		assertEquals(expected, result);
	}

	public void testMd5Serializable() throws Md5Exception {
		String expected = "6651d7cd977a38aaf023490668ef92f";
		String result = Utils.md5(new TestForSerializable());
		assertEquals(expected, result);
	}

	public void testMd5Kryo() throws Md5Exception {
		String expected = "d41d8cd98f0b24e980998ecf8427e";
		String result = Utils.md5(new TestForKryo());
		assertEquals(expected, result);
	}

	static class TestForSerializable implements Serializable {
		private static final long serialVersionUID = -8293533135049125923L;
		@SuppressWarnings("unused")
		private int id = 1;
		@SuppressWarnings("unused")
		private String name = "test";
	}

	static class TestForKryo {
		@SuppressWarnings("unused")
		private int id = 1;
		@SuppressWarnings("unused")
		private String name = "test";
	}
}
