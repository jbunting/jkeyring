package io.bunting.keyring.backend;

/**
 *
 */
public interface KeyringBackend
{
	/**
	 * Each backend class must supply a priority, a number (float or integer)
	 * indicating the priority of the backend relative to all other backends.
	 * The priority need not be static -- it may (and should) vary based
	 * attributes of the environment in which is runs (platform, available
	 * packages, etc.).
	 *
	 * A higher number indicates a higher priority. The priority should throw
	 * a RuntimeError with a message indicating the underlying cause if the
	 * backend is not suitable for the current environment.
	 * As a rule of thumb, a priority between zero but less than one is
	 * suitable, but a priority of one or greater is recommended.
	 */
	int priority();

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
