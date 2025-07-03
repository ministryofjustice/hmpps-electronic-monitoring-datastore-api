package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmVisitDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.alcoholMonitoring.AthenaAmVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmVisitDetailsService

class AmVisitDetailsServiceTest {
  private lateinit var amVisitDetailsRepository: AmVisitDetailsRepository
  private lateinit var amVisitDetailsService: AmVisitDetailsService

  @BeforeEach
  fun setup() {
    amVisitDetailsRepository = Mockito.mock(AmVisitDetailsRepository::class.java)
    amVisitDetailsService = AmVisitDetailsService(amVisitDetailsRepository)
  }

  @Nested
  inner class GetVisitDetails {
    val legacySubjectId = "fake-id"

    private val exampleVisitDetails = listOf(
      AthenaAmVisitDetailsDTO(
        legacySubjectId = "123",
        visitId = "300",
        visitType = "visit type",
        visitAttempt = "attempt 1",
        dateVisitRaised = "2001-01-01",
        visitAddress = "test visit address",
        visitNotes = "visit notes",
        visitOutcome = "visit outcome",
        actualWorkStartDate = "2002-02-02",
        actualWorkStartTime = "02:20:20",
        actualWorkEndDate = "2003-03-03",
        actualWorkEndTime = "03:30:30",
        visitRejectionReason = "rejection reason",
        visitRejectionDescription = "rejection description",
        visitCancelReason = "cancel reason",
        visitCancelDescription = "cancel description",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amVisitDetailsRepository.getVisitDetails(legacySubjectId))
        .thenReturn(exampleVisitDetails)
    }

    @Test
    fun `calls getVisitDetails from amVisitDetailsRepository`() {
      amVisitDetailsService.getVisitDetails(legacySubjectId)

      Mockito.verify(amVisitDetailsRepository, Mockito.times(1)).getVisitDetails(legacySubjectId)
    }

    @Test
    fun `returns correct details of the AmVisitDetails when a response is received`() {
      val result = amVisitDetailsService.getVisitDetails(legacySubjectId)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().visitAddress).isEqualTo("test visit address")
      Assertions.assertThat(result.first().visitType).isEqualTo("visit type")
    }
  }
}
