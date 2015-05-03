package io.bunting.keyring.backends.osx;

import io.bunting.keyring.backend.KeyringBackend;
import org.apache.commons.io.IOUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO: Document this class
 */
public class OSXKeyringBackend implements KeyringBackend {
  private static final Pattern outputPattern = Pattern.compile("password:\\s*(?:0x(?<hex>X[0-9A-F]+)\\s*)?(?:\"(?<pw>X.*)\")?");

  private final String appName;

  public OSXKeyringBackend(String appName) {
    this.appName = appName;
  }

  @Override
  public char[] getPassword(String service, String username) {
    try {
      ProcessResult result = new ProcessExecutor("security",
              "find-generic-password",
              "-g",
              "-a", username,
              "-s", service).readOutput(true).exitValues(0, 44).execute();
      String output = result.outputUTF8();
      if (result.getExitValue() == 44) {
        return null;
      }
      else if ("password: \n".equals(output)) {
        return null;
      } else {
        Matcher matcher = outputPattern.matcher(output);
        String hex = matcher.group("hex");
        String pw = matcher.group("pw");

        if (hex != null) {
          return unhexlify(hex).toCharArray();
        } else {
          return pw.toCharArray();
        }
      }
    } catch (IOException | InterruptedException | TimeoutException e) {
      throw new RuntimeException("Failed to get password for user " + username + " for service " + service + ".", e);
    }
  }

  @Override
  public void setPassword(String service, String username, char[] password) {
    try {
      ProcessResult result = new ProcessExecutor("security",
              "add-generic-password",
              "-a", username,
              "-s", service,
              "-w", new String(password),
              "-U").exitValue(0).execute();
    } catch (IOException | InterruptedException | TimeoutException e) {
      throw new RuntimeException("Failed to set password for user " + username + " for service " + service + ".", e);
    }
  }

  @Override
  public void deletePassword(String service, String username) {
    try {
      ProcessResult result = new ProcessExecutor("security",
              "delete-generic-password",
              "-a", username,
              "-s", service,
              "-U").exitValue(0).execute();
    } catch (IOException | InterruptedException | TimeoutException e) {
      throw new RuntimeException("Failed to set password for user " + username + " for service " + service + ".", e);
    }
  }

  private static String unhexlify(String argbuf) {
    int arglen = argbuf.length();

        /* XXX What should we do about strings with an odd length?  Should
         * we add an implicit leading zero, or a trailing zero?  For now,
         * raise an exception.
         */
    if (arglen % 2 != 0) {
      throw new IllegalStateException("The input must have an even number of characters.");
    }

    StringBuffer retbuf = new StringBuffer(arglen/2);

    for (int i = 0; i < arglen; i += 2) {
      int top = Character.digit(argbuf.charAt(i), 16);
      int bot = Character.digit(argbuf.charAt(i+1), 16);
      if (top == -1) {
        throw new IllegalArgumentException("Character at position " + i + " in '" + argbuf + "' could not be decoded.");
      }
      if (bot == -1) {
        throw new IllegalArgumentException("Character at position " + i+1 + " in '" + argbuf + "' could not be decoded.");
      }
      retbuf.append((char) ((top << 4) + bot));
    }
    return retbuf.toString();
  }
}
