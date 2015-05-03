package io.bunting.keyring.support.gnome;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

/**
 * TODO: Document this class
 */
public interface GKLib extends Library {
  GKLib INSTANCE = (GKLib) Native.loadLibrary("gnome-keyring", GKLib.class);

  int gnome_keyring_find_password_sync(GnomeKeyringPasswordSchema schema, PointerByReference password, String ... attributes);

  int gnome_keyring_store_password_sync(GnomeKeyringPasswordSchema schema, String keyring, String displayName, String password, String ... attributes);

  int gnome_keyring_delete_password_sync(GnomeKeyringPasswordSchema schema, String ... attributes);

  void gnome_keyring_free_password(Pointer password);

  // the following classes are direct translations from the structs defined in gnome-keyring.h

  class GnomeKeyringPasswordSchema extends Structure {
    public int itemType = 0; // you shouldn't need to change this -- it means generic password
    public GnomeKeyringAttribute[] attributes = new GnomeKeyringAttribute[32]; // don't change this length! it's in the struct def

    @Override
    protected List getFieldOrder() {
      return Arrays.asList("itemType", "attributes");
    }
  }

  class GnomeKeyringAttribute extends Structure {
    public String name; // set this! this is what you care about!
    public int type = 0; // you shouldn't need to change this -- it means UTF-8 string

    @Override
    protected List getFieldOrder() {
      return Arrays.asList("name", "type");
    }
  }
}
