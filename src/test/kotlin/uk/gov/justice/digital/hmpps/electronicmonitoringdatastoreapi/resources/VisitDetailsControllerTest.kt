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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.VisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.VisitDetailsAddress
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.VisitDetailsController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.VisitDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class VisitDetailsControllerTest {
  private lateinit var visitDetailsService: VisitDetailsService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: VisitDetailsController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    visitDetailsService = Mockito.mock(VisitDetailsService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = VisitDetailsController(visitDetailsService, roleService, auditService)
  }

  @Nested
  inner class GetVisitDetails {
    @Test
    fun `gets order information from order service`() {
      val legacyOrderId = "1ab"
      val expectedResult = listOf<VisitDetails>(
        VisitDetails(
          legacySubjectId = 123,
          legacyOrderId = 321,
          address = VisitDetailsAddress(
            addressLine1 = "3 My street",
            addressLine2 = "My town",
            addressLine3 = "My county",
            addressLine4 = null,
            postcode = "MA77 5AD",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-04-04T00:00:00"),
          actualWorkEndDateTime = LocalDateTime.parse("2020-04-14T00:00:00"),
          visitNotes = "nothing to say",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "All good",
        ),
      )

      Mockito.`when`(visitDetailsService.getVisitDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getVisitDetails(authentication, legacyOrderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(visitDetailsService, Mockito.times(1)).getVisitDetails(legacyOrderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
