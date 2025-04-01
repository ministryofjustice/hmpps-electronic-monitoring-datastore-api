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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderDetailsService

class AmOrderDetailsServiceTest {
  private lateinit var amOrderDetailsRepository: AmOrderDetailsRepository
  private lateinit var service: AmOrderDetailsService

  @BeforeEach
  fun setup() {
    amOrderDetailsRepository = mock(AmOrderDetailsRepository::class.java)
    service = AmOrderDetailsService(amOrderDetailsRepository)
  }

  @Test
  fun `AmOrderDetailsService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class GetOrderDetails {
    val orderId = "fake-id"

    val orderDetailsData = AthenaAmOrderDetailsDTO(
      legacySubjectId = "AA2020",
      legacyOrderId = "222",
      firstName = "testFirstName",
    )

    @BeforeEach
    fun setup() {
      `when`(amOrderDetailsRepository.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(orderDetailsData)
    }

    @Test
    fun `calls getAmOrderDetails from order details repository`() {
      service.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      Mockito.verify(amOrderDetailsRepository, times(1)).getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns AmOrderDetails`() {
      val result = service.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(AmOrderDetails::class.java)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo("AA2020")
      Assertions.assertThat(result.firstName).isEqualTo("testFirstName")
    }
  }
}
