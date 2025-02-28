package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class AuthenticationStub(
  private val name: String? = null,
  private val authorities: MutableCollection<out GrantedAuthority> = mutableListOf(),
) : Authentication {
  override fun getName(): String = name ?: ""
  override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities
  override fun isAuthenticated(): Boolean = false
  override fun getCredentials(): Any? = null
  override fun getDetails(): Any = ""
  override fun getPrincipal(): Any = ""
  override fun setAuthenticated(isAuthenticated: Boolean) {}
}

class AuthenticationStubTest {

  @Test
  fun `AuthenticationStub can be instantiated with passed name value`() {
    val nameValue = "fake name"
    val localStub = AuthenticationStub(nameValue)
    Assertions.assertThat(localStub.name).isEqualTo(nameValue)
  }

  @Test
  fun `AuthenticationStub can be instantiated with passed Authorities`() {
    val authorityName = "fake authority"
    val authority: GrantedAuthority = AuthorityStub(authorityName)
    val authoritiesList = mutableListOf(authority)
    val localStub = AuthenticationStub(authorities = authoritiesList)
    Assertions.assertThat(localStub.authorities).isEqualTo(authoritiesList)
  }

  @Test
  fun `AuthenticationStub can be instantiated with passed name and authorities`() {
    val nameValue = "fake name"
    val authority1Name = "fake authority 1"
    val authority2Name = "fake authority 2"
    val authoritiesList = mutableListOf(
      AuthorityStub(authority1Name),
      AuthorityStub(authority2Name),
    )

    val localStub = AuthenticationStub(
      name = nameValue,
      authorities = authoritiesList,
    )

    Assertions.assertThat(localStub.name).isEqualTo(nameValue)
    Assertions.assertThat(localStub.authorities).isEqualTo(authoritiesList)
    Assertions.assertThat(localStub.authorities.first().authority).isEqualTo(authority1Name)
    Assertions.assertThat(localStub.authorities.last().authority).isEqualTo(authority2Name)
  }
}
