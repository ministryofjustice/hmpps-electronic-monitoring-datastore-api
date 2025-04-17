package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityEquipmentDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityEquipmentDetail
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityEquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityEquipmentDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityEquipmentDetailsService

class IntegrityEquipmentDetailsServiceTest {
  private lateinit var integrityEquipmentDetailsRepository: IntegrityEquipmentDetailsRepository
  private lateinit var service: IntegrityEquipmentDetailsService

  @BeforeEach
  fun setup() {
    integrityEquipmentDetailsRepository = Mockito.mock(IntegrityEquipmentDetailsRepository::class.java)
    service = IntegrityEquipmentDetailsService(integrityEquipmentDetailsRepository)
  }

  @Test
  fun `EquipmentDetailsService can be instantiated`() {
    val sut = IntegrityEquipmentDetailsService(integrityEquipmentDetailsRepository)
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetEquipmentDetails {
    val legacySubjectId = "fake-id"

    val exampleEquipmentDetailsList = listOf<AthenaIntegrityEquipmentDetailsDTO>(
      AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = "123X",
        hmuEquipmentCategoryDescription = "TEST_HMU_CATEGORY",
        hmuInstallDate = "2020-02-02",
        hmuInstallTime = "02:02:02",
        hmuRemovedDate = "2030-03-03",
        hmuRemovedTime = "03:03:03",
        pidId = "321Y",
        pidEquipmentCategoryDescription = "TEST_PID_CATEGORY",
        dateDeviceInstalled = "2020-02-02",
        timeDeviceInstalled = "02:02:02",
        dateDeviceRemoved = "2030-03-03",
        timeDeviceRemoved = "03:03:03",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(integrityEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO))
        .thenReturn(exampleEquipmentDetailsList)
    }

    @Test
    fun `calls getEquipmentDetails from order information repository`() {
      service.getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(integrityEquipmentDetailsRepository, Mockito.times(1)).getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
    }

    @Test
    fun `returns a list of EquipmentDetails when a response is received`() {
      val result = service.getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(List::class.java)
    }

    @Test
    fun `returns correct details of the EquipmentDetails when a response is received`() {
      val result = service.getEquipmentDetails(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.size).isEqualTo(1)

      Assertions.assertThat(result.first()).isInstanceOf(IntegrityEquipmentDetails::class.java)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().pid).isInstanceOf(IntegrityEquipmentDetail::class.java)
      Assertions.assertThat(result.first().pid?.equipmentCategoryDescription).isEqualTo("TEST_PID_CATEGORY")
      Assertions.assertThat(result.first().hmu).isInstanceOf(IntegrityEquipmentDetail::class.java)
      Assertions.assertThat(result.first().hmu?.equipmentCategoryDescription).isEqualTo("TEST_HMU_CATEGORY")
    }
  }
}
