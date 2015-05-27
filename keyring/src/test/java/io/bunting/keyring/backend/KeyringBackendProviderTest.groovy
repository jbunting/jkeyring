package io.bunting.keyring.backend

import spock.lang.Requires
import spock.lang.Specification

/**
 * TODO: Document this class
 */
class KeyringBackendProviderTest extends Specification {
  def "test keyring available on platform"() {
    expect: "at least one keyring is available"
      def backend = KeyringBackendProvider.loadMostSuitableBackend("testApp")
      backend != null
  }

  // this test will FAIL if run on a platform that does NOT have a suitable keyring provide with priority > 0  unless
  // that environment is travis -- in this case this test will be skipped
  // (file backed is not sufficient)
  @Requires({
    def travis = System.getenv("TRAVIS")
    return travis == null || !travis.toBoolean()
  })
  def "test keyrings with priority greater than 0 available"() {
    expect: "at least one keyring with priority greater than 0 is available"
      def backend = KeyringBackendProvider.loadMostSuitableBackend("testApp", 1)
      backend != null
  }
}
