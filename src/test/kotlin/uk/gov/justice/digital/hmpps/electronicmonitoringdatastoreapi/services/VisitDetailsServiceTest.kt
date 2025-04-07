package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.VisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.VisitDetailsAddress
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaVisitDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.VisitDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.VisitDetailsService

class VisitDetailsServiceTest {
  private lateinit var visitDetailsRepository: VisitDetailsRepository
  private lateinit var service: VisitDetailsService

  @BeforeEach
  fun setup() {
    visitDetailsRepository = Mockito.mock(VisitDetailsRepository::class.java)
    service = VisitDetailsService(visitDetailsRepository)
  }

  @Test
  fun `SuspensionOfVisitsService can be instantiated`() {
    val sut = VisitDetailsService(visitDetailsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetVisitDetails {
    val legacySubjectId = "fake-id"

    val exampleVisitDetails = listOf<AthenaVisitDetailsDTO>(
      AthenaVisitDetailsDTO(
        legacySubjectId = 123,
        legacyOrderId = 321,
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
      Mockito.`when`(visitDetailsRepository.getVisitDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleVisitDetails)
    }

    @Test
    fun `calls getVisitDetails from order information repository`() {
      service.getVisitDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(visitDetailsRepository, Mockito.times(1)).getVisitDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of VisitDetails when a response is received`() {
      var result = service.getVisitDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the VisitDetails when a response is received`() {
      var result = service.getVisitDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(VisitDetails::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().address).isInstanceOf(VisitDetailsAddress::class.java)
      Assertions.assertThat(result.first().visitType).isEqualTo("TEST_VISIT_TYPE")
    }
  }
}
