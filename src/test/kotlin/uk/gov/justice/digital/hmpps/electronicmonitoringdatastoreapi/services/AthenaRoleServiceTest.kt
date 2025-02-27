package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService

class AthenaRoleServiceTest {
  private lateinit var athenaRoleService: AthenaRoleService

  @BeforeEach
  fun setUp() {
    athenaRoleService = AthenaRoleService()
  }

  @Test
  fun `AthenaRoleService can be instantiated`() {
    Assertions.assertThat(athenaRoleService).isNotNull()
  }

  @Nested
  inner class GetRoleFromAuthentication {
    // when they have no role in the Authentication object, give them NONE

    // when they have General, as an authority/claim, give them general

    // when special, give them special not general

    // when special and general, then special
  }

  @Nested
  inner class FomString {
    // return the role with name matching NONE

    // return the role with name matching GENERAL

    // return the role with name matching RESTRICTED
  }
}
