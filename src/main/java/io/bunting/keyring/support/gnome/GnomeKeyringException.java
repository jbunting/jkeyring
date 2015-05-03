package io.bunting.keyring.support.gnome;

/**
 * TODO: Document this class
 */
public class GnomeKeyringException extends RuntimeException {
  private static final int GNOME_KEYRING_RESULT_OK = 0;
  private static final int GNOME_KEYRING_RESULT_DENIED = 1;
  private static final int GNOME_KEYRING_RESULT_NO_KEYRING_DAEMON = 2;
  private static final int GNOME_KEYRING_RESULT_ALREADY_UNLOCKED = 3;
  private static final int GNOME_KEYRING_RESULT_NO_SUCH_KEYRING = 4;
  private static final int GNOME_KEYRING_RESULT_BAD_ARGUMENTS = 5;
  private static final int GNOME_KEYRING_RESULT_IO_ERROR = 6;
  private static final int GNOME_KEYRING_RESULT_CANCELLED = 7;
  private static final int GNOME_KEYRING_RESULT_KEYRING_ALREADY_EXISTS = 8;
  private static final int GNOME_KEYRING_RESULT_NO_MATCH = 9;

  private GnomeKeyringException(String verbing, String username, String service, final String error) {
    super(String.format("'%s' error when %s password for %s for service %s.", error, verbing, username, service));
  }

  public static boolean handle(String service, String username, String verbing, int result) {
    switch (result) {
      case GNOME_KEYRING_RESULT_OK:
        return true;
      case GNOME_KEYRING_RESULT_DENIED:
        throw new GnomeKeyringException(verbing, username, service, "DENIED");
      case GNOME_KEYRING_RESULT_NO_KEYRING_DAEMON:
        throw new GnomeKeyringException(verbing, username, service, "NO KEYRING DAEMON");
      case GNOME_KEYRING_RESULT_ALREADY_UNLOCKED:
        throw new GnomeKeyringException(verbing, username, service, "ALREADY UNLOCKED");
      case GNOME_KEYRING_RESULT_NO_SUCH_KEYRING:
        throw new GnomeKeyringException(verbing, username, service, "NO SUCH KEYRING");
      case GNOME_KEYRING_RESULT_BAD_ARGUMENTS:
        throw new GnomeKeyringException(verbing, username, service, "BAD ARGUMENTS");
      case GNOME_KEYRING_RESULT_IO_ERROR:
        throw new GnomeKeyringException(verbing, username, service, "IO ERROR");
      case GNOME_KEYRING_RESULT_CANCELLED:
        throw new GnomeKeyringException(verbing, username, service, "CANCELLED");
      case GNOME_KEYRING_RESULT_KEYRING_ALREADY_EXISTS:
        throw new GnomeKeyringException(verbing, username, service, "KEYRING ALREADY EXISTS");
      case GNOME_KEYRING_RESULT_NO_MATCH:
        return false;
      default:
        throw new GnomeKeyringException(verbing, username, service, "UNRECOGNIZED ERROR [" + result + "]");
    }
  }
}
