package io.bunting.keyring.backends;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import io.bunting.keyring.KeyringUtil;
import io.bunting.keyring.KeyringUtil.LockCloser;
import io.bunting.keyring.KeyringUtil.RWLock;
import io.bunting.keyring.backend.KeyringBackend;

import org.apache.commons.codec.binary.Base64;
import org.ini4j.Ini;

/**
 * TODO: filesystem locking
 */
public abstract class BaseFileBackedKeyringBackend implements KeyringBackend
{
	private final Path storage;
	private RWLock lock = KeyringUtil.createReadWriteLock();

	protected BaseFileBackedKeyringBackend(final String filename)
	{
		this(KeyringUtil.getDataRoot().resolve(filename).toAbsolutePath());
	}

	BaseFileBackedKeyringBackend(final Path storage)
	{
		this.storage = storage;
	}

	protected abstract byte[] encrypt(final char[] password);

	protected abstract char[] decrypt(final byte[] encryptedPassword);

	@Override
	public char[] getPassword(final String service, final String username)
	{
		try (LockCloser _ = lock.lockForRead())
		{
			Ini ini = loadStore();
			final String base64Encrypted = ini.get(service, username);
			if (base64Encrypted == null)
			{
				return null;
			}
			else
			{
				final byte[] encrypted = Base64.decodeBase64(base64Encrypted);
				return this.decrypt(encrypted);
			}
		}
	}

	@Override
	public void setPassword(final String service, final String username, final char[] password)
	{
		final byte[] encrypted = this.encrypt(password);
		final String base64Encrypted = Base64.encodeBase64String(encrypted);
		try (LockCloser _ = lock.lockForWrite())
		{
			Ini ini = loadStore();
			ini.put(service, username, base64Encrypted);
			saveStore(ini);
		}
	}

	@Override
	public void deletePassword(final String service, final String username)
	{
		try (LockCloser _ = lock.lockForWrite())
		{
			Ini ini = loadStore();
			ini.remove(service, username);
			saveStore(ini);
		}
	}

	private Ini loadStore()
	{
		if (!Files.exists(this.storage))
		{
			return new Ini();
		}

		try (BufferedReader reader = Files.newBufferedReader(this.storage, StandardCharsets.UTF_8))
		{
			return new Ini(reader);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to read keyring storage file " + storage);
		}
	}

	private void saveStore(final Ini ini)
	{
		try (BufferedWriter writer = Files.newBufferedWriter(this.storage, StandardCharsets.UTF_8))
		{
			ini.store(writer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Failed to write keyring storage file " + storage);
		}
	}
}
