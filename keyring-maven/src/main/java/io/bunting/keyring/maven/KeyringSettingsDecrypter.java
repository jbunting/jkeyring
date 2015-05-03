package io.bunting.keyring.maven;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.building.DefaultSettingsProblem;
import org.apache.maven.settings.building.SettingsProblem;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document this class
 */
@Component( role = SettingsDecrypter.class, hint = "keyring")
public class KeyringSettingsDecrypter implements SettingsDecrypter
{
  private final Keyring keyring = new DefaultKeyring("maven");

  @Requirement( hint = "maven" )
  private SecDispatcher securityDispatcher;

  @Override
  public SettingsDecryptionResult decrypt(SettingsDecryptionRequest request) {
    List<SettingsProblem> problems = new ArrayList<SettingsProblem>();

    List<Server> servers = new ArrayList<Server>();

    for ( Server server : request.getServers() )
    {
      server = server.clone();

      servers.add( server );

      try
      {
        server.setPassword( decrypt( server.getId(), server.getUsername(), server.getPassword() ) );
      }
      catch ( SecDispatcherException e )
      {
        problems.add( new DefaultSettingsProblem( "Failed to decrypt password for server " + server.getId()
                + ": " + e.getMessage(), SettingsProblem.Severity.ERROR, "server: " + server.getId(), -1, -1, e ) );
      }

      try
      {
        server.setPassphrase( decrypt(server.getId(), server.getUsername(), server.getPassphrase() ) );
      }
      catch ( SecDispatcherException e )
      {
        problems.add( new DefaultSettingsProblem( "Failed to decrypt passphrase for server " + server.getId()
                + ": " + e.getMessage(), SettingsProblem.Severity.ERROR, "server: " + server.getId(), -1, -1, e ) );
      }
    }

    List<Proxy> proxies = new ArrayList<Proxy>();

    for ( Proxy proxy : request.getProxies() )
    {
      proxy = proxy.clone();

      proxies.add( proxy );

      try
      {
        proxy.setPassword( decrypt(proxy.getId(), proxy.getUsername(), proxy.getPassword() ) );
      }
      catch ( SecDispatcherException e )
      {
        problems.add( new DefaultSettingsProblem( "Failed to decrypt password for proxy " + proxy.getId()
                + ": " + e.getMessage(), SettingsProblem.Severity.ERROR, "proxy: " + proxy.getId(), -1, -1, e ) );
      }
    }

    return new DefaultSettingsDecryptionResult( servers, proxies, problems );
  }

  private String decrypt(String service, String username, String str)
          throws SecDispatcherException
  {
    if (str == null) {
      return null;
    } else if ("{keyring}".equals(str)) {
      char[] password = keyring.getPassword(service, username);
      if (password == null) {
        // TODO: prompt
        password = "password".toCharArray();
        keyring.setPassword(service, username, password);
      }
      return new String(password);
    } else {
      return securityDispatcher.decrypt( str );
    }
  }
}
