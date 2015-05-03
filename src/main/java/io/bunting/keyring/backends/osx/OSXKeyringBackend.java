package io.bunting.keyring.backends.osx;

import io.bunting.keyring.backend.KeyringBackend;

/**
 * TODO: Document this class
 */
public class OSXKeyringBackend implements KeyringBackend {
  private final String appName;

  public OSXKeyringBackend(String appName) {
    this.appName = appName;
  }

  @Override
  public char[] getPassword(String service, String username) {
    // TODO: make it work
    return null;
  }

  @Override
  public void setPassword(String service, String username, char[] password) {
    // TODO: make it work
  }

  @Override
  public void deletePassword(String service, String username) {
    // TODO: make it work
  }
}
