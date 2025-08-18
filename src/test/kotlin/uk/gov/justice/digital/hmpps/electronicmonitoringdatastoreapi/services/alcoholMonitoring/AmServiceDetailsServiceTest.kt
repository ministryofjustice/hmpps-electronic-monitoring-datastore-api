package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmServiceDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models.alcoholMonitoring.AthenaAmServiceDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmServiceDetailsService
import java.time.LocalDateTime

class AmServiceDetailsServiceTest {
  private lateinit var amServiceDetailsRepository: AmServiceDetailsRepository
  private lateinit var amServiceDetailsService: AmServiceDetailsService

  @BeforeEach
  fun setup() {
    amServiceDetailsRepository = Mockito.mock(AmServiceDetailsRepository::class.java)
    amServiceDetailsService = AmServiceDetailsService(amServiceDetailsRepository)
  }

  @Nested
  inner class GetServices {
    val legacySubjectId = "fake-id"

    private val exampleServices = listOf(
      AthenaAmServiceDetailsDTO(
        legacySubjectId = "123",
        serviceStartDate = "2001-01-01",
        serviceEndDate = "2002-02-02",
        serviceAddress = "service address",
        equipmentStartDate = "2003-03-03",
        equipmentEndDate = "2004-04-04",
        hmuSerialNumber = "hmu-01",
        deviceSerialNumber = "device-01",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amServiceDetailsRepository.getServiceDetails(legacySubjectId))
        .thenReturn(exampleServices)
    }

    @Test
    fun `calls getServices from amServicesRepository`() {
      amServiceDetailsService.getServiceDetails(legacySubjectId)

      Mockito.verify(amServiceDetailsRepository, Mockito.times(1)).getServiceDetails(legacySubjectId)
    }

    @Test
    fun `returns correct details of the AmService when a response is received`() {
      val result = amServiceDetailsService.getServiceDetails(legacySubjectId)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddress).isEqualTo("service address")
      Assertions.assertThat(result.first().hmuSerialNumber).isEqualTo("hmu-01")
      Assertions.assertThat(result.first().serviceStartDate).isEqualTo(LocalDateTime.parse("2001-01-01T00:00:00"))
    }
  }
}
