package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringServiceDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAlcoholMonitoringServiceDetailsDTO
import java.time.LocalDateTime

class AlcoholMonitoringServiceDetailsServiceTest {
  private lateinit var alcoholMonitoringServiceDetailsRepository: AlcoholMonitoringServiceDetailsRepository
  private lateinit var alcoholMonitoringServiceDetailsService: AlcoholMonitoringServiceDetailsService

  @BeforeEach
  fun setup() {
    alcoholMonitoringServiceDetailsRepository = Mockito.mock(AlcoholMonitoringServiceDetailsRepository::class.java)
    alcoholMonitoringServiceDetailsService = AlcoholMonitoringServiceDetailsService(alcoholMonitoringServiceDetailsRepository)
  }

  @Nested
  inner class GetServices {
    val legacySubjectId = "fake-id"

    private val exampleServices = listOf(
      AthenaAlcoholMonitoringServiceDetailsDTO(
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
      Mockito.`when`(alcoholMonitoringServiceDetailsRepository.findByLegacySubjectId(legacySubjectId))
        .thenReturn(exampleServices)
    }

    @Test
    fun `calls getServices from amServicesRepository`() {
      alcoholMonitoringServiceDetailsService.getServiceDetails(legacySubjectId)

      Mockito.verify(alcoholMonitoringServiceDetailsRepository, Mockito.times(1)).findByLegacySubjectId(legacySubjectId)
    }

    @Test
    fun `returns correct details of the AlcoholMonitoringService when a response is received`() {
      val result = alcoholMonitoringServiceDetailsService.getServiceDetails(legacySubjectId)

      Assertions.assertThat(result.size).isEqualTo(1)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddress).isEqualTo("service address")
      Assertions.assertThat(result.first().hmuSerialNumber).isEqualTo("hmu-01")
      Assertions.assertThat(result.first().serviceStartDate).isEqualTo(LocalDateTime.parse("2001-01-01T00:00:00"))
    }
  }
}
