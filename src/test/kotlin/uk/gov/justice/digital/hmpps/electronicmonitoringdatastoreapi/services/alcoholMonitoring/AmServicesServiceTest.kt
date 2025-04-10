package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmServiceDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmServicesRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmServicesService
import java.time.LocalDateTime

class AmServicesServiceTest {
  private lateinit var amServicesRepository: AmServicesRepository
  private lateinit var amServicesService: AmServicesService

  @BeforeEach
  fun setup() {
    amServicesRepository = Mockito.mock(AmServicesRepository::class.java)
    amServicesService = AmServicesService(amServicesRepository)
  }

  @Test
  fun `AmServicesService can be instantiated`() {
    val sut = AmServicesService(amServicesRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetServices {
    val legacySubjectId = "fake-id"

    private val exampleServices = listOf(
      AthenaAmServiceDTO(
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
      Mockito.`when`(amServicesRepository.getServices(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleServices)
    }

    @Test
    fun `calls getServices from amServicesRepository`() {
      amServicesService.getServices(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(amServicesRepository, Mockito.times(1)).getServices(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of AmService when a response is received`() {
      val result = amServicesService.getServices(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
      Assertions.assertThat(result).allSatisfy {
        Assertions.assertThat(it).isInstanceOf(AmService::class.java)
      }
    }

    @Test
    fun `returns correct details of the AmService when a response is received`() {
      val result = amServicesService.getServices(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(AmService::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().serviceAddress).isEqualTo("service address")
      Assertions.assertThat(result.first().hmuSerialNumber).isEqualTo("hmu-01")
      Assertions.assertThat(result.first().serviceStartDate).isEqualTo(LocalDateTime.parse("2001-01-01T00:00:00"))
    }
  }
}
