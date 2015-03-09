package io.bunting.keyring;

/**
 * The client API interface for the keyring. All consumers of this library should interact with
 * this interface.
 */
public interface Keyring
{
	/**
	 * Returns the password stored in the active keyring. If the password does not exist, it will return {@code null}.
	 */
	char[] getPassword(final String service, final String username);

	/**
	 * Store the password in the keyring.
	 */
	void setPassword(final String service, final String username, final char[] password);

	/**
	 * Delete the password stored in keyring. If the password does not exist, it will raise an exception.
	 */
	void deletePassword(final String service, final String username);
}
