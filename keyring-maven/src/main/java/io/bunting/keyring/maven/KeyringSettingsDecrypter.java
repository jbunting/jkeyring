package io.bunting.keyring.maven;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;
import io.bunting.keyring.KeyringUtil;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.codehaus.plexus.component.annotations.Component;

/**
 * TODO: Document this class
 */
@Component( role = SettingsDecrypter.class, hint = "keyring")
public class KeyringSettingsDecrypter implements SettingsDecrypter
{
  private final Keyring keyring = new DefaultKeyring("maven");

  @Override
  public SettingsDecryptionResult decrypt(SettingsDecryptionRequest settingsDecryptionRequest) {
//    settingsDecryptionRequest.
    return null;
  }
}
