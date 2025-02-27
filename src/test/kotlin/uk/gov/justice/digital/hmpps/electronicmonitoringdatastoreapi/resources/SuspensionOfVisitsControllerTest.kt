package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.SuspensionOfVisitsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.SuspensionOfVisitsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class SuspensionOfVisitsControllerTest {
  private lateinit var suspensionOfVisitsService: SuspensionOfVisitsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: SuspensionOfVisitsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    suspensionOfVisitsService = Mockito.mock(SuspensionOfVisitsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = SuspensionOfVisitsController(suspensionOfVisitsService, roleService, auditService)
  }

  @Nested
  inner class GetSuspensionOfVisits {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = listOf(
        SuspensionOfVisits(
          legacySubjectId = 123,
          suspensionOfVisits = "Yes",
          suspensionOfVisitsRequestedDate = LocalDateTime.parse("2020-04-04T00:00:00"),
          suspensionOfVisitsStartDate = LocalDateTime.parse("2020-04-14T00:00:00"),
          suspensionOfVisitsStartTime = "00:00:00",
          suspensionOfVisitsEndDate = LocalDateTime.parse("2020-04-24T00:00:00"),
        ),
      )

      Mockito.`when`(suspensionOfVisitsService.getSuspensionOfVisits(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getSuspensionOfVisits(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(suspensionOfVisitsService, Mockito.times(1)).getSuspensionOfVisits(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
