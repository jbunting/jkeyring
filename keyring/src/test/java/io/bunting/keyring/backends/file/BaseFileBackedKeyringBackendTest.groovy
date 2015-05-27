package io.bunting.keyring.backends.file

import org.apache.commons.io.FileUtils
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.nio.file.Files

class BaseFileBackedKeyringBackendTest extends Specification
{
	File testDir = new File("target/test-temp/BaseFileBackedKeyringBackendTest");

	def setup()
	{
		if (testDir.exists())
		{
			FileUtils.forceDelete(testDir)
		}
		if (!testDir.mkdirs())
		{
			throw new IllegalStateException("I can't make my dir!")
		}
	}

	def cleanup()
	{
		if (testDir.exists())
		{
			FileUtils.forceDelete(testDir)
		}
	}
	def "test get set and get"()
	{
		given: "a backend"
			def store = new File(testDir, "keyring_store.cfg").toPath()
			Files.deleteIfExists(store)
			def underTest = new PlainTextKeyring(store)
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
		expect: "the store exists"
			Files.exists(store)
		and: "the store contains both usernames and services"
			def contents = Files.readAllLines(store, StandardCharsets.UTF_8).join("\n")
			contents.contains("test_service")
			contents.contains("other_service")
			contents.contains("johnny82")
		and: "the store does NOT contain either password"
			!contents.contains("hunter2")
			!contents.contains("password1")
	}
}
