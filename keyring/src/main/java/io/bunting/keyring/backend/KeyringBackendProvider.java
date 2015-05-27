package io.bunting.keyring.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The entry point for a backend implementation.
 *
 * Implementations must be declared in a service loader file:
 *
 * META-INF/services/io.bunting.keyring.backend.KeyringBackendProvider
 *
 * In addition, they MUST have a public, no-arg constructor.
 */
public abstract class KeyringBackendProvider
{
	private static final Logger logger = LoggerFactory.getLogger(KeyringBackendProvider.class);

	private final String appName;

	protected KeyringBackendProvider(final String appName)
	{
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	/**
	 * Each backend class must supply a priority, a number (float or integer)
	 * indicating the priority of the backend relative to all other backends.
	 * The priority need not be static -- it may (and should) vary based
	 * attributes of the environment in which is runs (platform, available
	 * packages, etc.).
	 *
	 * A higher number indicates a higher priority. The priority should return
	 * a -1 if it is completely unsuitable for the current system.
	 */
	public abstract int priority();

	/**
	 * This will be called to retrieve the reason if {@link #priority()} returns
	 * -1. If the provider is NOT unsuitable, then this method will return
	 * {@code null}.
	 */
	public abstract String getUnsuitableReason();

	/**
	 * Instantiates the backend.
	 * @return
	 */
	public abstract KeyringBackend create(final String appName);

	public static KeyringBackend loadMostSuitableBackend(final String appName) {
		return loadMostSuitableBackend(appName, 0);
	}

	public static KeyringBackend loadMostSuitableBackend(final String appName, final int minimumPriority)
	{
		final List<KeyringBackendProvider> suitableProviders = loadAllSuitableProviders(minimumPriority);
		final KeyringBackend backend = suitableProviders.isEmpty() ? null : suitableProviders.get(0).create(appName);
		return backend;
	}

	public static List<KeyringBackendProvider> loadAllSuitableProviders(int minimumPriority) {
		final ServiceLoader<KeyringBackendProvider> providers = ServiceLoader.load(KeyringBackendProvider.class);

		final List<KeyringBackendProvider> suitableProviders = new ArrayList<KeyringBackendProvider>();

		for (KeyringBackendProvider provider: providers)
		{
			if (provider.priority() < minimumPriority)
			{
				logger.info("Backend {} determined unsuitable for the current system. {}", provider.appName, provider.getUnsuitableReason());
			}
			else
			{
				suitableProviders.add(provider);
			}
		}
		Collections.sort(suitableProviders, new Comparator<KeyringBackendProvider>() {
			@Override
			public int compare(final KeyringBackendProvider o1, final KeyringBackendProvider o2) {
				// reverse priority -- we want "highest priority" to be at index 0
				return o2.priority() - o1.priority();
			}
		});
		return suitableProviders;
	}
}
