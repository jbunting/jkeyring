package io.bunting.keyring.backend;

/**
 * Thrown by {@link KeyringBackend#priority()} when a backend is unsuitable for
 * the current environment.
 */
public class UnsuitableBackendException extends Exception
{
	public UnsuitableBackendException(final String message)
	{
		super(message);
	}
}
