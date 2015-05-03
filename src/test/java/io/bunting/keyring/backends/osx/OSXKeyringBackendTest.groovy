package io.bunting.keyring.backends.osx

import org.junit.internal.AssumptionViolatedException
import spock.lang.Specification

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
}
