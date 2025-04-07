package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CurfewTimetable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaCurfewTimetableDTO
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
  fun `CurfewTimetableService can be instantiated`() {
    val sut = CurfewTimetableService(curfewTimetableRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetCurfewTimetable {
    val legacySubjectId = "fake-id"

    val exampleCurfewTimetable = listOf<AthenaCurfewTimetableDTO>(
      AthenaCurfewTimetableDTO(
        legacySubjectId = 123,
        legacyOrderId = 123,
        serviceId = 333,
        serviceAddress1 = "",
        serviceAddress2 = "",
        serviceAddress3 = "",
        serviceAddressPostcode = "WA16 9GH",
        serviceStartDate = "2020-02-02",
        serviceEndDate = "2021-02-02",
        curfewStartDate = "2020-02-02",
        curfewEndDate = "2021-02-02",
        curfewStartTime = "07:07:07",
        curfewEndTime = "08:08:08",
        monday = 0,
        tuesday = 0,
        wednesday = 0,
        thursday = 0,
        friday = 0,
        saturday = 0,
        sunday = 0,
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(curfewTimetableRepository.getCurfewTimetable(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleCurfewTimetable)
    }

    @Test
    fun `calls getCurfewTimetable from order information repository`() {
      service.getCurfewTimetable(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(curfewTimetableRepository, Mockito.times(1)).getCurfewTimetable(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of CurfewTimetable when a response is received`() {
      var result = service.getCurfewTimetable(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the CurfewTimetable when a response is received`() {
      var result = service.getCurfewTimetable(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(CurfewTimetable::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddressPostcode).isEqualTo("WA16 9GH")
    }
  }
}
