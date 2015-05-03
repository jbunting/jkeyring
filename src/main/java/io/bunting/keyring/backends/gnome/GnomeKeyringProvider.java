package io.bunting.keyring.backends.gnome;

import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.backend.KeyringBackendProvider;

/**
 * TODO: Document this class
 */
public class GnomeKeyringProvider extends KeyringBackendProvider {
  protected GnomeKeyringProvider() {
    super("gnome");
  }

  @Override
  public int priority() {
    if (GnomeKeyringBackend.isSupported()) {
      return 1;
    } else {
      return -1;
    }
  }

  @Override
  public String getUnsuitableReason() {
    return "This system is not running gnome.";
  }

  @Override
  public KeyringBackend create(String appName) {
    return new GnomeKeyringBackend(appName);
  }
}
