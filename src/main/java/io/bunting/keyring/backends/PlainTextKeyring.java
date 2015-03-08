package io.bunting.keyring.backends;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class PlainTextKeyring extends BaseFileBackedKeyringBackend
{
	protected PlainTextKeyring()
	{
		super("keyring_pass.cfg");
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
