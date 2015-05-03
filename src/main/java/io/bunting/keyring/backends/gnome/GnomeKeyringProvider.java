package io.bunting.keyring.backends.gnome;

import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.backend.KeyringBackendProvider;
import io.bunting.keyring.support.gnome.GKLib;

/**
 * TODO: Document this class
 */
public class GnomeKeyringProvider extends KeyringBackendProvider {
  private static final boolean SUPPORTED;

  static {
    boolean supported = true;
    try {
      // don't do anything with this, we just want to see if we can access it...
      GKLib lib = GKLib.INSTANCE;
    } catch (UnsatisfiedLinkError e) {
      supported = false;
    }
    SUPPORTED = supported;
  }

  protected GnomeKeyringProvider() {
    super("gnome");
  }

  @Override
  public int priority() {
    if (SUPPORTED) {
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
