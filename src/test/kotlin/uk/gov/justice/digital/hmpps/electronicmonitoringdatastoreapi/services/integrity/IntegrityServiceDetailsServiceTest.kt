package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityServiceDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.integrity.AthenaIntegrityServiceDetailsDTO

class IntegrityServiceDetailsServiceTest {
  private lateinit var integrityServiceDetailsRepository: IntegrityServiceDetailsRepository
  private lateinit var service: IntegrityServiceDetailsService

  @BeforeEach
  fun setup() {
    integrityServiceDetailsRepository = Mockito.mock(IntegrityServiceDetailsRepository::class.java)
    service = IntegrityServiceDetailsService(integrityServiceDetailsRepository)
  }

  @Nested
  inner class GetServiceDetails {
    val legacySubjectId = "fake-id"

    val exampleServiceDetails = listOf(
      AthenaIntegrityServiceDetailsDTO(
        legacySubjectId = "123",
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
      Mockito.`when`(integrityServiceDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, false))
        .thenReturn(exampleServiceDetails)
    }

    @Test
    fun `calls getServiceDetails from order information repository`() {
      service.getServiceDetails(legacySubjectId, false)

      Mockito.verify(integrityServiceDetailsRepository, Mockito.times(1)).findByLegacySubjectIdAndRestricted(legacySubjectId, false)
    }

    @Test
    fun `returns correct details of the ServiceDetails when a response is received`() {
      val result = service.getServiceDetails(legacySubjectId, false)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddressPostcode).isEqualTo("WA16 9GH")
    }
  }
}
