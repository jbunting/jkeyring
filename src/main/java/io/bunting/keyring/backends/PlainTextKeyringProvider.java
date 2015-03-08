package io.bunting.keyring.backends;

import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.backend.KeyringBackendProvider;

import org.kohsuke.MetaInfServices;

@MetaInfServices
public class PlainTextKeyringProvider extends KeyringBackendProvider
{
	protected PlainTextKeyringProvider()
	{
		super("plainText");
	}

	@Override
	public int priority()
	{
		// it'll work anywhere, but it's never recommended
		return 0;
	}

	@Override
	public String getUnsuitableReason()
	{
		return "There is no reason for this provider to be unsuitable.";
	}

	@Override
	public KeyringBackend create()
	{
		return new PlainTextKeyring();
	}
}
