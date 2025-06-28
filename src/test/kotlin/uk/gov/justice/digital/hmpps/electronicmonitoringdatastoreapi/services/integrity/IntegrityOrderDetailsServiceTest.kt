package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderDetailsService
import java.time.LocalDateTime

class IntegrityOrderDetailsServiceTest {
  private lateinit var integrityOrderDetailsRepository: IntegrityOrderDetailsRepository
  private lateinit var service: IntegrityOrderDetailsService

  @BeforeEach
  fun setup() {
    integrityOrderDetailsRepository = mock(IntegrityOrderDetailsRepository::class.java)
    service = IntegrityOrderDetailsService(integrityOrderDetailsRepository)
  }

  @Nested
  inner class GetOrderDetails {
    val legacySubjectId = "fake-id"

    val orderDetailsData = AthenaIntegrityOrderDetailsDTO(
      legacySubjectId = "AA2020",
      firstName = "John",
      lastName = "Smith",
      alias = "Zeno",
      dateOfBirth = "1980-02-01",
      sex = "Sex",
      phoneOrMobileNumber = "09876543210",
      primaryAddressLine1 = "1 Primary Street",
      primaryAddressLine2 = "Sutton",
      primaryAddressLine3 = "London",
      primaryAddressPostCode = "ABC 123",
      legacyOrderId = "1234567",
      orderStartDate = "2012-02-01",
      orderEndDate = "2013-04-03",
      orderType = "Community",
      orderTypeDescription = "",
      adultOrChild = "adult",
      contact = "",
      ppo = "",
      mappa = "",
      technicalBail = "",
      manualRisk = "",
      offenceRisk = true,
      postCodeRisk = "",
      falseLimbRisk = "",
      migratedRisk = "",
      rangeRisk = "",
      reportRisk = "",
      orderTypeDetail = "",
      wearingWristPid = "",
      notifyingOrganisationDetailsName = "",
      responsibleOrganisation = "",
      responsibleOrganisationDetailsRegion = "",
    )

    @BeforeEach
    fun setup() {
      `when`(integrityOrderDetailsRepository.getOrderDetails(legacySubjectId, false))
        .thenReturn(orderDetailsData)
    }

    @Test
    fun `calls getOrderDetails from integrity order details repository`() {
      service.getOrderDetails(legacySubjectId, false)
      Mockito.verify(integrityOrderDetailsRepository, times(1)).getOrderDetails(legacySubjectId, false)
    }

    @Test
    fun `returns correct details of the order`() {
      val result = service.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result.legacySubjectId).isEqualTo("AA2020")
      Assertions.assertThat(result.firstName).isEqualTo("John")
      Assertions.assertThat(result.specials).isEqualTo("no")
      Assertions.assertThat(result.orderEndDate).isEqualTo(LocalDateTime.parse("2013-04-03T00:00:00"))
    }
  }
}
