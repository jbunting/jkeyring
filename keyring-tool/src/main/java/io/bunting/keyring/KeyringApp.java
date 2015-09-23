package io.bunting.keyring;

import io.bunting.keyring.DefaultKeyring;
import io.bunting.keyring.Keyring;

public class KeyringApp
{
	private enum Action
	{
		GET, SET, DELETE;

		public static Action asAction(String str)
		{
			for (Action me : Action.values())
			{
				if (me.name().equalsIgnoreCase(str))
					return me;
			}
			return null;
		}
	}

	public static void main(final String[] args)
	{
		if (args.length < 3)
		{
			printHelp();
			System.exit(1);
		}

		Action action = Action.asAction(args[0]);

		if (action == null)
		{
			printHelp();
			System.exit(1);
		}

		String service = args[1].toLowerCase();
		String username = args[2].toLowerCase();
		String password;

		switch(action)
		{
		case GET:
			if (args.length != 3)
			{
				printHelp();
				System.exit(1);
			}

			password = get(service, username);

			if (password != null)
			{
				System.out.println(password);
			}
			else
			{
				System.err.println("No value for that service and username.");
				System.exit(1);
			}
			return;

		case SET:
			if (args.length != 4)
			{
				printHelp();
				System.exit(1);
			}

			password = args[3];
			set(service, username, password);
			return;

		case DELETE:
			if (args.length != 3)
			{
				printHelp();
				System.exit(1);
			}

			delete(service, username);
			return;
		}
	}

	private static String get(final String service, final String username)
	{
		Keyring keyring = getKeyring();
		char[] pass = keyring.getPassword(service, username);
		if (pass == null)
		{
			return null;
		}
		return new String(pass);
	}

	private static boolean set(final String service, final String username,
							   final String password)
	{
		Keyring keyring = getKeyring();
		char[] pass = password.toCharArray();
		keyring.setPassword(service, username, pass);
		return password.equals(KeyringApp.get(service, username));
	}

	private static boolean delete(final String service, final String username)
	{
		Keyring keyring = getKeyring();
		keyring.deletePassword(service, username);
		return KeyringApp.get(service, username) == null;
	}

	private static Keyring getKeyring()
	{
		String appName = System.getProperty("appName");
		if (appName == null)
		{
			appName = "jkeyring";
		}
		return new DefaultKeyring(appName);
	}

	private static void printHelp()
	{
		System.err.println("Usage:");
		System.err.println("\tget {service} {username}");
		System.err.println("\tset {service} {username} {password}");
		System.err.println("\tdelete {service} {username}");
		System.err.println();
		System.err.println("Use -DappName={appName} to change the appName.");
	}
}
