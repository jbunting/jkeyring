package io.bunting.keyring.backends

import com.github.goldin.spock.extensions.testdir.TestDir
import spock.lang.Specification

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets

/**
 * TODO: Document this class
 */
class BaseFileBackedKeyringBackendTest extends Specification
{
	@TestDir(baseDir = "target/test-temp", clean = false)
	File testDir;

	def "test get set and get"()
	{
		given: "a backend"
			new File(testDir, "keyring_store.cfg").delete()
			def underTest = new BaseFileBackedKeyringBackend(testDir.toPath().resolve("keyring_store.cfg")) {
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
		expect: "when i get a password it will be null"
			null == underTest.getPassword("test_service", "johnny82")
		when: "i set then get a password"
			underTest.setPassword("test_service", "johnny82", "hunter2".toCharArray())
		then: "it will equal the original value"
			"hunter2".toCharArray() == underTest.getPassword("test_service", "johnny82")
		when: "i set the same user in another service"
			underTest.setPassword("other_service", "johnny82", "password1".toCharArray())
		then: "it has the right password"
			"password1".toCharArray() == underTest.getPassword("other_service", "johnny82")
	}
}
