package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaSuspensionOfVisitsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SuspensionOfVisitsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.SuspensionOfVisitsService

class SuspensionOfVisitsServiceTest {
  private lateinit var suspensionOfVisitsRepository: SuspensionOfVisitsRepository
  private lateinit var service: SuspensionOfVisitsService

  @BeforeEach
  fun setup() {
    suspensionOfVisitsRepository = Mockito.mock(SuspensionOfVisitsRepository::class.java)
    service = SuspensionOfVisitsService(suspensionOfVisitsRepository)
  }

  @Test
  fun `SuspensionOfVisitsService can be instantiated`() {
    val sut = SuspensionOfVisitsService(suspensionOfVisitsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetSuspensionOfVisits {
    val legacySubjectId = "fake-id"

    val exampleSuspensionOfVisits = listOf<AthenaSuspensionOfVisitsDTO>(
      AthenaSuspensionOfVisitsDTO(
        legacySubjectId = 123,
        legacyOrderId = 456,
        suspensionOfVisits = "Yes",
        suspensionOfVisitsRequestedDate = "2023-03-03",
        suspensionOfVisitsStartDate = "2023-04-04",
        suspensionOfVisitsStartTime = "03:03:04",
        suspensionOfVisitsEndDate = "2024-04-04",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(suspensionOfVisitsRepository.getSuspensionOfVisits(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleSuspensionOfVisits)
    }

    @Test
    fun `calls getSuspensionOfVisits from order information repository`() {
      service.getSuspensionOfVisits(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(suspensionOfVisitsRepository, Mockito.times(1)).getSuspensionOfVisits(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of SuspensionOfVisits when a response is received`() {
      var result = service.getSuspensionOfVisits(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the CurfewTimetable when a response is received`() {
      var result = service.getSuspensionOfVisits(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(SuspensionOfVisits::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().suspensionOfVisits).isEqualTo("Yes")
    }
  }
}
