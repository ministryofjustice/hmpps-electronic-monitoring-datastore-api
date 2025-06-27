package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityVisitDetailsAddress
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityVisitDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityVisitDetailsService

class IntegrityVisitDetailsServiceTest {
  private lateinit var integrityVisitDetailsRepository: IntegrityVisitDetailsRepository
  private lateinit var service: IntegrityVisitDetailsService

  @BeforeEach
  fun setup() {
    integrityVisitDetailsRepository = Mockito.mock(IntegrityVisitDetailsRepository::class.java)
    service = IntegrityVisitDetailsService(integrityVisitDetailsRepository)
  }

  @Test
  fun `SuspensionOfVisitsService can be instantiated`() {
    val sut = IntegrityVisitDetailsService(integrityVisitDetailsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetVisitDetails {
    val legacySubjectId = "fake-id"

    val exampleVisitDetails = listOf<AthenaIntegrityVisitDetailsDTO>(
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
      Mockito.`when`(integrityVisitDetailsRepository.getVisitDetails(legacySubjectId, false))
        .thenReturn(exampleVisitDetails)
    }

    @Test
    fun `calls getVisitDetails from order information repository`() {
      service.getVisitDetails(legacySubjectId, false)

      Mockito.verify(integrityVisitDetailsRepository, Mockito.times(1)).getVisitDetails(legacySubjectId, false)
    }

    @Test
    fun `returns a list of VisitDetails when a response is received`() {
      val result = service.getVisitDetails(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the VisitDetails when a response is received`() {
      val result = service.getVisitDetails(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(IntegrityVisitDetails::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().address).isInstanceOf(IntegrityVisitDetailsAddress::class.java)
      Assertions.assertThat(result.first().visitType).isEqualTo("TEST_VISIT_TYPE")
    }
  }
}
