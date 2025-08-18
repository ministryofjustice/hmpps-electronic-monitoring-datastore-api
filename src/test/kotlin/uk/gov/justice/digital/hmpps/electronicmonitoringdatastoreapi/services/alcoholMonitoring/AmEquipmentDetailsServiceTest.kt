package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmEquipmentDetailsRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.models.alcoholMonitoring.AthenaAmEquipmentDetailsDTO
import java.time.LocalDateTime

class AmEquipmentDetailsServiceTest {
  private lateinit var amEquipmentDetailsRepository: AmEquipmentDetailsRepository
  private lateinit var amEquipmentDetailsService: AmEquipmentDetailsService

  @BeforeEach
  fun setup() {
    amEquipmentDetailsRepository = Mockito.mock(AmEquipmentDetailsRepository::class.java)
    amEquipmentDetailsService = AmEquipmentDetailsService(amEquipmentDetailsRepository)
  }

  @Nested
  inner class GetEquipmentDetails {
    val legacySubjectId = "fake-id"

    private val exampleEquipmentDetails = listOf(
      AthenaAmEquipmentDetailsDTO(
        legacySubjectId = "123",
        deviceType = "tag",
        deviceSerialNumber = "740",
        deviceAddressType = "secondary",
        legFitting = "right",
        dateDeviceInstalled = "2001-01-01",
        timeDeviceInstalled = "01:10:10",
        dateDeviceRemoved = "2002-02-02",
        timeDeviceRemoved = "02:20:20",
        hmuInstallDate = null,
        hmuInstallTime = null,
        hmuRemovedDate = null,
        hmuRemovedTime = null,
      ),
      AthenaAmEquipmentDetailsDTO(
        legacySubjectId = "123",
        deviceType = "hmu",
        deviceSerialNumber = "740",
        deviceAddressType = "tertiary",
        legFitting = "left",
        dateDeviceInstalled = null,
        timeDeviceInstalled = null,
        dateDeviceRemoved = null,
        timeDeviceRemoved = null,
        hmuInstallDate = "2003-03-03",
        hmuInstallTime = "03:30:30",
        hmuRemovedDate = "2004-04-04",
        hmuRemovedTime = "04:40:40",
      ),
    )

    @BeforeEach
    fun setup() {
      Mockito.`when`(amEquipmentDetailsRepository.getEquipmentDetails(legacySubjectId))
        .thenReturn(exampleEquipmentDetails)
    }

    @Test
    fun `calls getEquipmentDetails from amEquipmentDetailsRepository`() {
      amEquipmentDetailsService.getEquipmentDetails(legacySubjectId)

      Mockito.verify(amEquipmentDetailsRepository, Mockito.times(1)).getEquipmentDetails(legacySubjectId)
    }

    @Test
    fun `returns correct details of the AmEquipmentDetails when a response is received`() {
      val result = amEquipmentDetailsService.getEquipmentDetails(legacySubjectId)

      Assertions.assertThat(result.size).isEqualTo(2)
      Assertions.assertThat(result.first().legacySubjectId).isEqualTo("123")
      Assertions.assertThat(result.first().deviceSerialNumber).isEqualTo("740")
      Assertions.assertThat(result.first().legFitting).isEqualTo("right")
    }

    @Test
    fun `handles null dates and times correctly`() {
      val result = amEquipmentDetailsService.getEquipmentDetails(legacySubjectId)

      Assertions.assertThat(result.first().deviceInstalledDateTime).isEqualTo(LocalDateTime.parse("2001-01-01T01:10:10"))
      Assertions.assertThat(result.first().deviceRemovedDateTime).isEqualTo(LocalDateTime.parse("2002-02-02T02:20:20"))
      Assertions.assertThat(result.first().hmuInstallDateTime).isNull()
      Assertions.assertThat(result.first().hmuRemovedDateTime).isNull()

      Assertions.assertThat(result[1].deviceInstalledDateTime).isNull()
      Assertions.assertThat(result[1].deviceRemovedDateTime).isNull()
      Assertions.assertThat(result[1].hmuInstallDateTime).isEqualTo(LocalDateTime.parse("2003-03-03T03:30:30"))
      Assertions.assertThat(result[1].hmuRemovedDateTime).isEqualTo(LocalDateTime.parse("2004-04-04T04:40:40"))
    }
  }
}
