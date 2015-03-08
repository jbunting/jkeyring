package io.bunting.keyring.backend;

/**
 *
 */
public interface KeyringBackend
{
	/**
	 * Get password of the username for the service.
	 */
	char[] getPassword(final String service, final String username);

	/**
	 * Set password for the username of the service.
	 */
	void setPassword(final String service, final String username, final char[] password);

	/**
	 * Delete the password for the username of the service.
	 */
	void deletePassword(final String service, final String username);
}
