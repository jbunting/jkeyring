package io.bunting.keyring.backends.file;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class PlainTextKeyring extends BaseFileBackedKeyringBackend
{
	public PlainTextKeyring(final String appName)
	{
		super(String.format("keyring_%s.cfg", appName));
	}

	PlainTextKeyring(final Path storage)
	{
		super(storage);
	}

	@Override
	protected byte[] encrypt(final char[] password)
	{
		ByteBuffer bb = StandardCharsets.UTF_8.encode(CharBuffer.wrap(password));
		byte[] bytes = new byte[bb.limit()];
		bb.get(bytes);
		return bytes;
	}

	@Override
	protected char[] decrypt(final byte[] encryptedPassword)
	{
		CharBuffer cb = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encryptedPassword));
		char[] chars = new char[cb.limit()];
		cb.get(chars);
		return chars;
	}
}
