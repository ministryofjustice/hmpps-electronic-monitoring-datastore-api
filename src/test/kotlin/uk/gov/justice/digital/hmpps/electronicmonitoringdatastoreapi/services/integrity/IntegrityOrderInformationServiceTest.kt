package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityOrderInformationService
import java.time.LocalDateTime

class IntegrityOrderInformationServiceTest {
  private lateinit var integrityOrderInformationRepository: IntegrityOrderInformationRepository
  private lateinit var service: IntegrityOrderInformationService

  @BeforeEach
  fun setup() {
    integrityOrderInformationRepository = mock(IntegrityOrderInformationRepository::class.java)
    service = IntegrityOrderInformationService(integrityOrderInformationRepository)
  }

  @Test
  fun `AmOrderInformationService can be instantiated`() {
    Assertions.assertThat(service).isNotNull()
  }

  @Nested
  inner class GetOrderInformation {
    val legacySubjectId = "fake-id"

    val orderInformationData = AthenaIntegrityOrderInformationDTO(
      legacySubjectId = "123",
      legacyOrderId = "321",
      name = "John Smith",
      alias = "Zeno",
      dateOfBirth = "1980-02-01",
      address1 = "1 Primary Street",
      address2 = "Sutton",
      address3 = "London",
      postcode = "ABC 123",
      orderStartDate = "2012-02-01",
      orderEndDate = "2013-04-03",
    )

    @BeforeEach
    fun setup() {
      `when`(integrityOrderInformationRepository.getOrderInformation(legacySubjectId, false))
        .thenReturn(orderInformationData)
    }

    @Test
    fun `calls getOrderInformation from order information repository`() {
      service.getOrderInformation(legacySubjectId, false)
      Mockito.verify(integrityOrderInformationRepository, times(1)).getOrderInformation(legacySubjectId, false)
    }

    @Test
    fun `returns IntegrityOrderInformation`() {
      val result = service.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isInstanceOf(IntegrityOrderInformation::class.java)
    }

    @Test
    fun `returns correct information of the order`() {
      val result = service.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacyOrderId).isEqualTo("321")
      Assertions.assertThat(result.name).isEqualTo("John Smith")
      Assertions.assertThat(result.dateOfBirth).isEqualTo(LocalDateTime.parse("1980-02-01T00:00:00"))
    }
  }
}
