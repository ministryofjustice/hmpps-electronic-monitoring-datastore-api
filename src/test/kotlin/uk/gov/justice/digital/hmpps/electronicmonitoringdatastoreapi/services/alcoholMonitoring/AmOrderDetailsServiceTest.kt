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
import java.time.LocalDateTime

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
      firstName = "John",
      lastName = "Smith",
      alias = "Zeno",
      dateOfBirth = "1980-02-01",
      sex = "Sex",
      specialInstructions = "Special instructions",
      phoneNumber = "09876543210",
      address1 = "1 Primary Street",
      address2 = "Sutton",
      address3 = "London",
      postcode = "ABC 123",
      legacyOrderId = "1234567",
      orderStartDate = "2012-02-01",
      orderEndDate = "2013-04-03",
      enforceableCondition = "Enforceable condition",
      orderType = "Community",
      orderTypeDescription = "",
      orderEndOutcome = "",
      responsibleOrganisationPhoneNumber = "01234567890",
      responsibleOrganisationEmail = "a@b.c",
      tagAtSource = "",
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
      Assertions.assertThat(result.firstName).isEqualTo("John")
      Assertions.assertThat(result.orderEndDate).isEqualTo(LocalDateTime.parse("2013-04-03T00:00:00"))
    }
  }
}
