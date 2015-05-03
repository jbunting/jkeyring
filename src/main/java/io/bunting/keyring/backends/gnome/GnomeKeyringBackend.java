package io.bunting.keyring.backends.gnome;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import io.bunting.keyring.backend.KeyringBackend;
import io.bunting.keyring.support.gnome.GKLib;
import io.bunting.keyring.support.gnome.GnomeKeyringException;

import static io.bunting.keyring.support.gnome.GnomeKeyringException.*;

/**
 * TODO: Document this class
 */
class GnomeKeyringBackend  implements KeyringBackend{
  private static final GKLib.GnomeKeyringPasswordSchema passwordSchema = createPasswordSchema();
  private static final String USERNAME_ATTR = "username";
  private static final String SERVICE_ATTR = "service";

  private static GKLib.GnomeKeyringPasswordSchema createPasswordSchema() {
    GKLib.GnomeKeyringPasswordSchema passwordSchema = new GKLib.GnomeKeyringPasswordSchema();
    passwordSchema.attributes[0] = new GKLib.GnomeKeyringAttribute();
    passwordSchema.attributes[0].name = USERNAME_ATTR;
    passwordSchema.attributes[1] = new GKLib.GnomeKeyringAttribute();
    passwordSchema.attributes[1].name = SERVICE_ATTR;
    return passwordSchema;
  }

  private final GKLib gklib;

  public GnomeKeyringBackend(String appName) {
    // apparently we don't care about the appName? if we set it on glib2, and we instantiate more than once, then we get
    // warnings
    gklib = GKLib.INSTANCE;
  }

  public static boolean isSupported() {
    return Native.isSupportedNativeType(GKLib.class);
  }

  @Override
  public char[] getPassword(String service, String username) {
    PointerByReference passwordPointer = new PointerByReference();
    if (handle(service, username, "finding", gklib.gnome_keyring_find_password_sync(passwordSchema, passwordPointer, buildAttrArray(service, username)))) {
      final Pointer pointer = passwordPointer.getValue();
      String password = pointer.getString(0, "UTF8");
      gklib.gnome_keyring_free_password(pointer);
      return password.toCharArray();
    } else {
      return null;
    }
  }

  @Override
  public void setPassword(String service, String username, char[] password) {
    final String displayName = String.format("Password for %s on service %s.", username, service);
    handle(service, username, "setting", gklib.gnome_keyring_store_password_sync(passwordSchema, null, displayName, new String(password), buildAttrArray(service, username)));
  }

  private String[] buildAttrArray(String service, String username) {
    return new String[]{USERNAME_ATTR, username, SERVICE_ATTR, service};
  }

  @Override
  public void deletePassword(String service, String username) {
    handle(service, username, "deleting", gklib.gnome_keyring_delete_password_sync(passwordSchema, buildAttrArray(service, username)));
  }
}
