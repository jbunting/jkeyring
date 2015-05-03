package io.bunting.keyring.backends.osx

import org.junit.internal.AssumptionViolatedException
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class OSXKeyringBackendTest extends Specification {

  def "test get set and get"()
  {
    given: "the provider"
      def provider = new OSXKeyringProvider()
      if (provider.priority() < 0) {
        throw new AssumptionViolatedException("${provider.getAppName()} provider is not available on this system.")
      }
    and: "a backend"
      def underTest = provider.create("jkeyring-test")
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
    given: "the provider"
      def provider = new OSXKeyringProvider();
      if (provider.priority() < 0) {
        throw new AssumptionViolatedException("${provider.getAppName()} provider is not available on this system.")
      }
    and: "a backend"
      def underTest = provider.create("jkeyring-test")
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

  def "test extracting password from output"() {
    expect: "the password is hunter2"
      "hunter2".toCharArray() == OSXKeyringBackend.extractPasswordFromOutput(testOutput)
  }


  static def testOutput = """
keychain: "/Users/cpenner/Library/Keychains/login.keychain"
class: "genp"
attributes:
    0x00000007 <blob>="test_service"
    0x00000008 <blob>=<NULL>
    "acct"<blob>="johnny82"
    "cdat"<timedate>=0x32303135303530333231343932355A00  "20150503214925Z\\000"
    "crtr"<uint32>=<NULL>
    "cusi"<sint32>=<NULL>
    "desc"<blob>=<NULL>
    "gena"<blob>=<NULL>
    "icmt"<blob>=<NULL>
    "invi"<sint32>=<NULL>
    "mdat"<timedate>=0x32303135303530333231343932355A00  "20150503214925Z\\000"
    "nega"<sint32>=<NULL>
    "prot"<blob>=<NULL>
    "scrp"<sint32>=<NULL>
    "svce"<blob>="test_service"
    "type"<uint32>=<NULL>
password: "hunter2"
"""
}
