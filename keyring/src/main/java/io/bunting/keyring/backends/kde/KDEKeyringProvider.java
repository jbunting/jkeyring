//package io.bunting.keyring.backends.kde;
//
//import io.bunting.keyring.backend.KeyringBackend;
//import io.bunting.keyring.backend.KeyringBackendProvider;
//import io.bunting.keyring.support.gnome.GKLib;
//import org.zeroturnaround.exec.ProcessExecutor;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * TODO: Document this class
// */
//public class KDEKeyringProvider extends KeyringBackendProvider {
//
//  private static final boolean SUPPORTED;
//
//  static {
//    boolean supported = true;
//    try {
//      //startCommand("isEnabled").upd
//    } catch (UnsatisfiedLinkError e) {
//      supported = false;
//    }
//    SUPPORTED = supported;
//  }
//
//  public KDEKeyringProvider() {
//    super("KDE");
//  }
//
//  @Override
//  public int priority() {
//
//    return 0;
//  }
//
//  @Override
//  public String getUnsuitableReason() {
//    return null;
//  }
//
//  @Override
//  public KeyringBackend create(String appName) {
//    return null;
//  }
//
//  static ProcessExecutor startCommand(String cmd, String ... cmdArgs) {
//    List<String> args = new ArrayList<>();
//    args.add("qdbus");
//    args.add("org.kde.kwalletd");
//    args.add("/modules/kwalletd");
//    args.add("org.kde.KWallet." + cmd);
//    for (String cmdArg: cmdArgs) {
//      args.add(cmdArg);
//    }
//    return new ProcessExecutor().command(args);
//  }
//}
