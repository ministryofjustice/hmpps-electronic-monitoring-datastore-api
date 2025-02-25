package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService

class AthenaRoleServiceTest {
  lateinit var service: AthenaRoleService

  @Nested
  inner class AuthenticationStub(
    private val name: String,
  ) : Authentication {
    override fun getName(): String = name
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()
    override fun isAuthenticated(): Boolean = false
    override fun getCredentials(): Any? = null
    override fun getDetails(): Any = ""
    override fun getPrincipal(): Any = ""
    override fun setAuthenticated(isAuthenticated: Boolean) {}
  }

  @BeforeEach
  fun setup() {
    service = AthenaRoleService()
  }

  @Test
  fun `Role service instantiates correctly`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class GetRoleFromAuthentication {

    @Test
    fun `returns correct IAM value for DEV role`() {
      val authentication = AuthenticationStub("DEV")
      val expectedIamRole = "arn:aws:iam::800964199911:role/cmt_read_emds_data_dev"

      val result = service.getRoleFromAuthentication(authentication)

      Assertions.assertThat(result.name).isEqualTo("DEV")
      Assertions.assertThat(result.iamRole).isEqualTo(expectedIamRole)
    }

    @Test
    fun `returns correct IAM value for TEST role`() {
      val authentication = AuthenticationStub("TEST")
      val expectedIamRole = "arn:aws:iam::396913731313:role/cmt_read_emds_data_test"

      val result = service.getRoleFromAuthentication(authentication)

      Assertions.assertThat(result.name).isEqualTo("TEST")
      Assertions.assertThat(result.iamRole).isEqualTo(expectedIamRole)
    }

    @Test
    fun `returns NONE role when role name not found`() {
      val noNameStub = AuthenticationStub("")
      Assertions
        .assertThat(service.getRoleFromAuthentication(noNameStub))
        .isEqualTo(AthenaRole.NONE)

      val wrongNameStub = AuthenticationStub("totally fake name here")
      Assertions
        .assertThat(service.getRoleFromAuthentication(wrongNameStub))
        .isEqualTo(AthenaRole.NONE)
    }
  }
}
