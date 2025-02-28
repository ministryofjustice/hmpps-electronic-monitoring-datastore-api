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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CurfewTimetable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.CurfewTimetableController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.CurfewTimetableService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import java.time.LocalDateTime

@ActiveProfiles("test")
@JsonTest
class CurfewTimetableControllerTest {
  private lateinit var curfewTimetableService: CurfewTimetableService
  private lateinit var roleService: AthenaRoleService
  private lateinit var auditService: AuditService
  private lateinit var controller: CurfewTimetableController
  private lateinit var authentication: Authentication

  @BeforeEach
  fun setup() {
    authentication = Mockito.mock(Authentication::class.java)
    Mockito.`when`(authentication.name).thenReturn("MOCK_AUTH_USER")
    curfewTimetableService = Mockito.mock(CurfewTimetableService::class.java)
    roleService = Mockito.mock(AthenaRoleService::class.java)
    Mockito.`when`(roleService.getRoleFromAuthentication(authentication)).thenReturn(AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    auditService = Mockito.mock(AuditService::class.java)
    controller = CurfewTimetableController(curfewTimetableService, roleService, auditService)
  }

  @Nested
  inner class GetCurfewTimetable {
    @Test
    fun `gets order information from order service`() {
      val orderId = "1ab"
      val expectedResult = listOf(
        CurfewTimetable(
          legacySubjectId = 123,
          serviceId = 1,
          serviceAddress1 = "",
          serviceAddress2 = "",
          serviceAddress3 = "",
          serviceAddressPostcode = "TEST+POSTCODE",
          serviceStartDate = LocalDateTime.parse("2020-04-04T00:00:00"),
          serviceEndDate = LocalDateTime.parse("2020-04-14T00:00:00"),
          curfewStartDate = LocalDateTime.parse("2020-04-06T00:00:00"),
          curfewEndDate = LocalDateTime.parse("2020-04-08T00:00:00"),
          monday = 0,
          tuesday = 0,
          wednesday = 0,
          thursday = 0,
          friday = 1,
          saturday = 1,
          sunday = 0,
        ),
      )

      Mockito.`when`(curfewTimetableService.getCurfewTimetable(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)).thenReturn(expectedResult)

      val result = controller.getCurfewTimetable(authentication, orderId)

      Assertions.assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
      Assertions.assertThat(result.body).isEqualTo(expectedResult)

      Mockito.verify(curfewTimetableService, Mockito.times(1)).getCurfewTimetable(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }
  }
}
