package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaServiceDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.ServiceDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.ServiceDetailsService

class ServiceDetailsServiceTest {
  private lateinit var serviceDetailsRepository: ServiceDetailsRepository
  private lateinit var service: ServiceDetailsService

  @BeforeEach
  fun setup() {
    serviceDetailsRepository = Mockito.mock(ServiceDetailsRepository::class.java)
    service = ServiceDetailsService(serviceDetailsRepository)
  }

  @Test
  fun `ServiceDetailsService can be instantiated`() {
    val sut = ServiceDetailsService(serviceDetailsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetServiceDetails {
    val legacySubjectId = "fake-id"

    val exampleServiceDetails = listOf<AthenaServiceDetailsDTO>(
      AthenaServiceDetailsDTO(
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
      Mockito.`when`(serviceDetailsRepository.getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleServiceDetails)
    }

    @Test
    fun `calls getServiceDetails from order information repository`() {
      service.getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(serviceDetailsRepository, Mockito.times(1)).getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of ServiceDetails when a response is received`() {
      var result = service.getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the ServiceDetails when a response is received`() {
      var result = service.getServiceDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(ServiceDetails::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddressPostcode).isEqualTo("WA16 9GH")
    }
  }
}
