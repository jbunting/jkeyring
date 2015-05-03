package io.bunting.keyring.backends.gnome

import com.github.goldin.spock.extensions.testdir.TestDir
import spock.lang.Specification
import spock.lang.Stepwise

import java.nio.file.Files

/**
 * TODO: Document this class
 */
@Stepwise
class GnomeKeyringBackendTest extends Specification {
  def "test get set and get"()
  {
    given: "a backend"
      def underTest = new GnomeKeyringBackend("jkeyring-test")
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

  def "test delete"()
  {
    given: "a backend"
      def underTest = new GnomeKeyringBackend("jkeyring-test")
    expect: "passwords are in the store"
      "hunter2".toCharArray() == underTest.getPassword("test_service", "johnny82")
      "password1".toCharArray() == underTest.getPassword("other_service", "johnny82")
    when: "i delete a password"
      underTest.deletePassword("test_service", "johnny82")
    then: "it will be null"
      null == underTest.getPassword("test_service", "johnny82")
    when: "i delete another password"
      underTest.deletePassword("other_service", "johnny82")
    then: "it will be null"
      null == underTest.getPassword("other_service", "johnny82")
  }
}
