package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.core.GrantedAuthority

class AuthorityStub(
  private val authorityString: String,
) : GrantedAuthority {
  override fun getAuthority(): String = authorityString
}

class AuthorityStubTest {
  @Test
  fun `AuthorityStub can be instantiated with an authority string`() {
    val fakeClaimString = "FAKE_AUTHORITY_VALUE"
    val authorityStub = AuthorityStub(fakeClaimString)
    Assertions.assertThat(authorityStub.authority).isEqualTo(fakeClaimString)
  }
}
