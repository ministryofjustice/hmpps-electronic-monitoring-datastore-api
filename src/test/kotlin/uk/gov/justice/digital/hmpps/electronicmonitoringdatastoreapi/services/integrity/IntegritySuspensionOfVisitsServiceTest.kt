package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySuspensionOfVisitsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegritySuspensionOfVisitsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegritySuspensionOfVisitsService

class IntegritySuspensionOfVisitsServiceTest {
  private lateinit var integritySuspensionOfVisitsRepository: IntegritySuspensionOfVisitsRepository
  private lateinit var service: IntegritySuspensionOfVisitsService

  @BeforeEach
  fun setup() {
    integritySuspensionOfVisitsRepository = Mockito.mock(IntegritySuspensionOfVisitsRepository::class.java)
    service = IntegritySuspensionOfVisitsService(integritySuspensionOfVisitsRepository)
  }

  @Test
  fun `SuspensionOfVisitsService can be instantiated`() {
    val sut = IntegritySuspensionOfVisitsService(integritySuspensionOfVisitsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetSuspensionOfVisits {
    val legacySubjectId = "fake-id"

    val exampleSuspensionOfVisits = listOf(
      AthenaIntegritySuspensionOfVisitsDTO(
        legacySubjectId = "123",
        suspensionOfVisits = "Yes",
        suspensionOfVisitsRequestedDate = "2023-03-03",
        suspensionOfVisitsStartDate = "2023-04-04",
        suspensionOfVisitsStartTime = "03:03:04",
        suspensionOfVisitsEndDate = "2024-04-04",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integritySuspensionOfVisitsRepository.getSuspensionOfVisits(legacySubjectId, false))
        .thenReturn(exampleSuspensionOfVisits)
    }

    @Test
    fun `calls getSuspensionOfVisits from order information repository`() {
      service.getSuspensionOfVisits(legacySubjectId, false)

      Mockito.verify(integritySuspensionOfVisitsRepository, Mockito.times(1)).getSuspensionOfVisits(legacySubjectId, false)
    }

    @Test
    fun `returns a list of SuspensionOfVisits when a response is received`() {
      val result = service.getSuspensionOfVisits(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the suspension of visits when a response is received`() {
      val result = service.getSuspensionOfVisits(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(IntegritySuspensionOfVisits::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().suspensionOfVisits).isEqualTo("Yes")
    }
  }
}
