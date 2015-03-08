package io.bunting.keyring;

import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.backend.KeyringBackendProvider;

public class DefaultKeyring implements Keyring
{
	private final KeyringBackend backend;

	DefaultKeyring(final KeyringBackend backend)
	{
		this.backend = backend;
	}

	public DefaultKeyring()
	{
		this(KeyringBackendProvider.loadMostSuitableBackend());
	}

	@Override
	public char[] getPassword(final String service, final String username)
	{
		return this.backend.getPassword(service, username);
	}

	@Override
	public void setPassword(final String service, final String username, final char[] password)
	{
		this.backend.setPassword(service, username, password);
	}

	@Override
	public void deletePassword(final String service, final String username)
	{
		this.backend.deletePassword(service, username);
	}
}
