package io.bunting.keyring.backends.osx;

import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.backend.KeyringBackendProvider;

/**
 * TODO: Document this class
 */
public class OSXKeyringProvider extends KeyringBackendProvider {
  public OSXKeyringProvider() {
    super("OS X");
  }

  @Override
  public int priority() {
    String osName = System.getProperty("os.name").toLowerCase();
    boolean isMacOs = osName.startsWith("mac os x");
    return isMacOs ? 1 : -1;
  }

  @Override
  public String getUnsuitableReason() {
    return "You are not currently running on OS X.";
  }

  @Override
  public KeyringBackend create(String appName) {
    return new OSXKeyringBackend(appName);
  }
}
