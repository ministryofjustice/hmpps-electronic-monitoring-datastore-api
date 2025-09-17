package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityVisitDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityVisitDetailsDTO

class IntegrityVisitDetailsServiceTest {
  private lateinit var integrityVisitDetailsRepository: IntegrityVisitDetailsRepository
  private lateinit var service: IntegrityVisitDetailsService

  @BeforeEach
  fun setup() {
    integrityVisitDetailsRepository = Mockito.mock(IntegrityVisitDetailsRepository::class.java)
    service = IntegrityVisitDetailsService(integrityVisitDetailsRepository)
  }

  @Nested
  inner class GetVisitDetails {
    val legacySubjectId = "fake-id"

    val exampleVisitDetails = listOf(
      AthenaIntegrityVisitDetailsDTO(
        legacySubjectId = "123",
        address1 = "address_line_1",
        address2 = "address_line_2",
        address3 = "address_line_3",
        postcode = "postcode",
        actualWorkStartDate = "2002-02-02",
        actualWorkStartTime = "02:20:20",
        actualWorkEndDate = "2003-03-03",
        actualWorkEndTime = "03:30:30",
        visitNotes = "some notes",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "some outcome",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityVisitDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, false))
        .thenReturn(exampleVisitDetails)
    }

    @Test
    fun `calls getVisitDetails from order information repository`() {
      service.getVisitDetails(legacySubjectId, false)

      Mockito.verify(integrityVisitDetailsRepository, Mockito.times(1)).findByLegacySubjectIdAndRestricted(legacySubjectId, false)
    }

    @Test
    fun `returns correct details of the VisitDetails when a response is received`() {
      val result = service.getVisitDetails(legacySubjectId, false)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().visitType).isEqualTo("TEST_VISIT_TYPE")
    }
  }
}
