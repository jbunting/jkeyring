package io.bunting.keyring;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.SystemUtils;

/**
 * TODO: Document this class
 */
public class KeyringUtil
{
	public static Path getDataRoot()
	{
		if (SystemUtils.IS_OS_WINDOWS)
		{
			return getWindowsDataRoot();
		}
		else
		{
			return getLinuxDataRoot();
		}
	}

	private static Path getWindowsDataRoot()
	{
		final Path settingsRoot;
		if (SystemUtils.IS_OS_WINDOWS_XP)
		{
			final String userprofile = System.getenv("USERPROFILE");
			settingsRoot = Paths.get(userprofile, "Local Settings");
		}
		else
		{
			// use Vista-style
			String value = System.getenv("LOCALAPPDATA");
			if (value == null)
			{
				value = System.getenv("ProgramData");
			}
			if (value == null)
			{
				value = ".";
			}
			settingsRoot = Paths.get(value);
		}
		return settingsRoot.resolve("Java Keyring");
	}

	private static Path getLinuxDataRoot()
	{
		final Path settingsRoot;
		if (System.getenv().containsKey("XDG_DATA_HOME"))
		{
			settingsRoot = Paths.get(System.getenv("XDG_DATA_HOME"));
		}
		else
		{
			settingsRoot = Paths.get(SystemUtils.USER_HOME, ".local", "share");
		}
		return settingsRoot.resolve("java_keyring");
	}

	public static interface RWLock
	{
		LockCloser lockForRead();

		LockCloser lockForWrite();
	}

	public static RWLock createReadWriteLock()
	{
		return new RWLock()
		{
			private final ReadWriteLock _lock = new ReentrantReadWriteLock();
			private final LockCloser readCloser = new LockCloser(_lock.readLock());
			private final LockCloser writeCloser = new LockCloser(_lock.writeLock());

			@Override
			public LockCloser lockForRead()
			{
				return readCloser.lock();
			}

			@Override
			public LockCloser lockForWrite()
			{
				return writeCloser.lock();
			}
		};
	}

	public static class LockCloser implements AutoCloseable
	{
		private final Lock lock;

		private LockCloser(final Lock lock)
		{
			this.lock = lock;
		}

		public LockCloser lock()
		{
			this.lock.lock();
			return this;
		}

		@Override
		public void close()
		{
			this.lock.unlock();
		}
	}
}
