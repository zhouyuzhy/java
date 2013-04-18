package org.csp.crypto.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.csp.crypto.IEncryption;
import org.csp.exception.CryptoException;

public abstract class BlockEncryption implements IEncryption {
	protected byte[] blocksCrypto(Key key, byte[] iv, byte[] target,
			int blockSize, int cipherMode, String algorithm)
			throws CryptoException {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
		ByteArrayInputStream targetStream = new ByteArrayInputStream(target);
		while (targetStream.available() > 0) {
			try {
				blockSize = blockSize <= targetStream.available() ? blockSize
						: targetStream.available();
				byte[] temp = new byte[blockSize];
				targetStream.read(temp, 0, blockSize);
				Cipher cipher = Cipher.getInstance(algorithm);
				if (iv != null)
					cipher.init(cipherMode, key, new IvParameterSpec(
							iv));
				else
					cipher.init(cipherMode, key);
				resultStream.write(cipher.doFinal(temp));
			} catch (NoSuchAlgorithmException e) {
				throw new CryptoException(e);
			} catch (NoSuchPaddingException e) {
				throw new CryptoException(e);
			} catch (InvalidKeyException e) {
				throw new CryptoException(e);
			} catch (IllegalBlockSizeException e) {
				throw new CryptoException(e);
			} catch (BadPaddingException e) {
				throw new CryptoException(e);
			} catch (IOException e) {
				throw new CryptoException(e);
			} catch (InvalidAlgorithmParameterException e) {
				throw new CryptoException(e);
			}
		}
		return resultStream.toByteArray();
	}
}
