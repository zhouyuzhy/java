package org.csp.client.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import org.csp.client.exception.ServiceException;
import org.csp.crypto.Encryption;
import org.csp.exception.CryptoException;
import org.csp.store.IStore;
import org.csp.store.exception.LoginFailedException;
import org.csp.store.exception.StoreException;
import org.csp.store.model.Key;
import org.csp.store.model.Value;
import org.csp.store.util.Utils;

public class AsymmetricEncryptAndStoreServiceImpl implements
		IEncryptAndStoreService {

	private Encryption encryption;
	private IStore store;

	public AsymmetricEncryptAndStoreServiceImpl() {
	}

	public AsymmetricEncryptAndStoreServiceImpl(Encryption encryption,
			IStore store) {
		this.encryption = encryption;
		this.store = store;
	}

	@Override
	public boolean encryptAndSave(String[] keys, String value,
			PublicKey publicKey) throws ServiceException {
		String encryptedValue;
		try {
			encryptedValue = this.encryption.encryptStringAsBase64(publicKey,
					value);
			return store.save(new Key(keys), new Value(
					new ByteArrayInputStream(encryptedValue.getBytes())));
		} catch (CryptoException e) {
			throw new ServiceException(e);
		} catch (StoreException e) {
			throw new ServiceException(e);
		} catch (LoginFailedException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String getAndDecrypt(String[] keys, PrivateKey privateKey)
			throws ServiceException {
		try {
			Value encryptedValue = store.get(new Key(keys));
			return this.encryption.decryptBase64AsString(privateKey,
					new String(encryptedValue.read()));
		} catch (CryptoException e) {
			throw new ServiceException(e);
		} catch (StoreException e) {
			throw new ServiceException(e);
		} catch (LoginFailedException e) {
			throw new ServiceException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean exists(String[] keys) throws ServiceException {
		try {
			return store.exist(new Key(keys));
		} catch (StoreException e) {
			throw new ServiceException(e);
		} catch (LoginFailedException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public String[][] listKeys() throws ServiceException {
		try {
			List<Key> keys = store.list();
			String[][] result = new String[keys.size()][];
			int i = 0;
			for(Key key : keys){
				result[i++] = key.getkeys().toArray(new String[0]);
			}
			return result;
		} catch (StoreException e) {
			throw new ServiceException(e);
		} catch (LoginFailedException e) {
			throw new ServiceException(e);
		}
	}

	public Encryption getEncryption() {
		return encryption;
	}

	public void setEncryption(Encryption encryption) {
		this.encryption = encryption;
	}

	public IStore getStore() {
		return store;
	}

	public void setStore(IStore store) {
		this.store = store;
	}

}
