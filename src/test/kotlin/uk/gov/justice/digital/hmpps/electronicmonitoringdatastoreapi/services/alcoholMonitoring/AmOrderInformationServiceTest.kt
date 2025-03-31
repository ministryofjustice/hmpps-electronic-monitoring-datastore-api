package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderInformationService

class AmOrderInformationServiceTest {
  private lateinit var amOrderInformationRepository: AmOrderInformationRepository
  private lateinit var service: AmOrderInformationService

  @BeforeEach
  fun setup() {
    amOrderInformationRepository = mock(AmOrderInformationRepository::class.java)
    service = AmOrderInformationService(amOrderInformationRepository)
  }

  @Test
  fun `AmOrderInformationService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class GetOrderInformation {
    val orderId = "fake-id"

    val orderInformationData = AthenaAmOrderInformationDTO(
      legacySubjectId = "123",
      legacyOrderId = "321",
      firstName = "testFirstName",
    )

    @BeforeEach
    fun setup() {
      `when`(amOrderInformationRepository.getOrderInformation(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(orderInformationData)
    }

    @Test
    fun `calls getAmOrderInformation from order information repository`() {
      service.getOrderInformation(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Mockito.verify(amOrderInformationRepository, times(1)).getOrderInformation(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns AmOrderInformation`() {
      val result = service.getOrderInformation(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(AmOrderInformation::class.java)
    }

    @Test
    fun `returns correct information of the order`() {
      val result = service.getOrderInformation(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.firstName).isEqualTo("testFirstName")
    }
  }
}
