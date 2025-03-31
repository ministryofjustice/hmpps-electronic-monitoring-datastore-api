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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AmOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService

class AmOrderInformationServiceTest {
  private lateinit var amOrderDetailsRepository: AmOrderDetailsRepository
  private lateinit var service: AmOrderService

  @BeforeEach
  fun setup() {
    amOrderDetailsRepository = mock(AmOrderDetailsRepository::class.java)
    service = AmOrderService(amOrderDetailsRepository)
  }

  @Test
  fun `AmOrderService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class GetOrderDetails {
    val orderId = "fake-id"

    val blankOrderDetails = AthenaAmOrderDetailsDTO(
      legacySubjectId = "",
      legacyOrderId = "",
      responsibleOrgDetailsPhoneNumber = "",
    )

    @BeforeEach
    fun setup() {
      `when`(amOrderDetailsRepository.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(blankOrderDetails)
    }

    @Test
    fun `calls getAmOrderDetails from order details repository`() {
      service.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Mockito.verify(amOrderDetailsRepository, times(1)).getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns AmOrderDetails`() {
      val result = service.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(AmOrderDetails::class.java)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("")
      Assertions.assertThat(result.specials).isEqualTo("no")
    }
  }
}
