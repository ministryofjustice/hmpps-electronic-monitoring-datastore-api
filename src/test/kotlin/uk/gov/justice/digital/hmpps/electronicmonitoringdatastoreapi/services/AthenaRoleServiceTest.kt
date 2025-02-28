package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.AuthenticationStub
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.AuthorityStub

class AthenaRoleServiceTest {
  private lateinit var athenaRoleService: AthenaRoleService
  private val restrictedRoleIamString = "fake:iam:restricted"
  private val generalRoleIamString = "fake:iam:restricted"

  @BeforeEach
  fun setUp() {
    athenaRoleService = AthenaRoleService(
      restrictedRoleIamString,
      generalRoleIamString,
    )
  }

  @Test
  fun `AthenaRoleService can be instantiated`() {
    Assertions.assertThat(athenaRoleService).isNotNull()
  }

  @Nested
  inner class MapToOrderedUniqueRoles {
    @Test
    fun `de-duplicates identical elements`() {
      val result: List<AthenaRole> = athenaRoleService.mapToOrderedUniqueRoles(
        listOf("element", "element", "element"),
      )
      Assertions.assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `orders the results by role priority`() {
      val result: List<AthenaRole> = athenaRoleService.mapToOrderedUniqueRoles(
        listOf(
          "ROLE_EM_DATASTORE_GENERAL_RO",
          "ROLE_FOR_OTHER_SERVICE",
          "ROLE_EM_DATASTORE_GENERAL_RO",
          "ROLE_FOR_OTHER_SERVICE",
          "ROLE_EM_DATASTORE_RESTRICTED_RO",
        ),
      )
      Assertions.assertThat(result.size).isEqualTo(3)
      Assertions.assertThat(result[0]).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)
      Assertions.assertThat(result[1]).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Assertions.assertThat(result[2]).isEqualTo(AthenaRole.NONE)
    }
  }

  @Nested
  inner class GetRoleFromAuthentication {

    // when they have no role in the Authentication object, give them NONE
    @Test
    fun `GetRoleFromAuthentication returns role NONE if no authorities present`() {
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.NONE)
    }

    @Test
    fun `GetRoleFromAuthentication returns role NONE if wrong authorities present`() {
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(AuthorityStub("ROLE_FOR_OTHER_SERVICE")),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.NONE)
    }

    // when they have General, as an authority/claim, give them general
    @Test
    fun `GetRoleFromAuthentication returns role ROLE_EM_DATASTORE_GENERAL_RO with correct IAM role if this role is present`() {
      val expectedRole = "arn:aws:iam::396913731313:role/cmt_read_emds_data_test"
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(AuthorityStub("ROLE_EM_DATASTORE_GENERAL_RO")),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Assertions.assertThat(result.iamRole).isEqualTo(expectedRole)
    }

    @Test
    fun `GetRoleFromAuthentication returns role ROLE_EM_DATASTORE_GENERAL_RO if other roles also present`() {
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(
          AuthorityStub("ROLE_FOR_OTHER_SERVICE"),
          AuthorityStub("ROLE_EM_DATASTORE_GENERAL_RO"),
          AuthorityStub("ROLE_FOR_OTHER_SERVICE"),
        ),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    // when special, give them special not general
    @Test
    fun `GetRoleFromAuthentication returns role ROLE_EM_DATASTORE_RESTRICTED_RO if this role is present`() {
      val expectedRole = "arn:aws:iam::396913731313:role/cmt_read_emds_data_test"
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(AuthorityStub("ROLE_EM_DATASTORE_RESTRICTED_RO")),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)
      Assertions.assertThat(result.iamRole).isEqualTo(expectedRole)
    }

    @Test
    fun `GetRoleFromAuthentication returns role ROLE_EM_DATASTORE_RESTRICTED_RO if other roles also present`() {
      val auth = AuthenticationStub(
        name = "fake name",
        authorities = mutableListOf(
          AuthorityStub("ROLE_FOR_OTHER_SERVICE"),
          AuthorityStub("ROLE_EM_DATASTORE_GENERAL_RO"),
          AuthorityStub("ROLE_EM_DATASTORE_RESTRICTED_RO"),
          AuthorityStub("ROLE_EM_DATASTORE_GENERAL_RO"),
          AuthorityStub("ROLE_EM_DATASTORE_RESTRICTED_RO"),
          AuthorityStub("ROLE_FOR_OTHER_SERVICE"),
        ),
      )

      val result: AthenaRole = athenaRoleService.getRoleFromAuthentication(auth)

      Assertions.assertThat(result).isEqualTo(AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO)
    }

    // when special and general, then special
  }

  @Nested
  inner class FomString {
    // return the role with name matching NONE

    // return the role with name matching GENERAL

    // return the role with name matching RESTRICTED
  }
}
