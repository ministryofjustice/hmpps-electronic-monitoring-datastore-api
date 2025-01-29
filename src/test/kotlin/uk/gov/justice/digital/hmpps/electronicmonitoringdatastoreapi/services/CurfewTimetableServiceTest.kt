package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CurfewTimetable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaCurfewTimetableDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaResultListDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.CurfewTimetableRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.CurfewTimetableService

class CurfewTimetableServiceTest {
  private lateinit var curfewTimetableRepository: CurfewTimetableRepository
  private lateinit var service: CurfewTimetableService

  @BeforeEach
  fun setup() {
    curfewTimetableRepository = Mockito.mock(CurfewTimetableRepository::class.java)
    service = CurfewTimetableService(curfewTimetableRepository)
  }

  @Test
  fun `OrderService can be instantiated`() {
    val sut = CurfewTimetableService(curfewTimetableRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetMonitoringEventsList {
    val orderId = "fake-id"

    val exampleMonitoringEventList = AthenaResultListDTO(
      pageSize = 200,
      items = listOf<AthenaCurfewTimetableDTO>(
        AthenaCurfewTimetableDTO(
          legacySubjectId = 123,
          serviceId = 333,
          serviceAddress1 = "",
          serviceAddress2 = "",
          serviceAddress3 = "",
          serviceAddressPostcode = "WA16 9GH",
          serviceStartDate = "",
          serviceEndDate = "",
          curfewStartDate = "",
          curfewEndDate = "",
          curfewStartTime = "",
          curfewEndTime = "",
          monday = 0,
          tuesday = 0,
          wednesday = 0,
          thursday = 0,
          friday = 0,
          saturday = 0,
          sunday = 0,
        ),
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(curfewTimetableRepository.getServicesList(orderId, AthenaRole.DEV))
        .thenReturn(exampleMonitoringEventList)
    }

    @Test
    fun `calls getMonitoringEventsList from order information repository`() {
      service.getServices(orderId, AthenaRole.DEV)

      Mockito.verify(curfewTimetableRepository, Mockito.times(1)).getServicesList(orderId, AthenaRole.DEV)
    }

    @Test
    fun `returns MonitoringEventList when a response is received`() {
      var result = service.getServices(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the order when a response is received`() {
      var result = service.getServices(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(CurfewTimetable::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo(123)
      Assertions.assertThat(result.first().serviceAddressPostcode).isEqualTo("WA16 9GH")
    }
  }
}
