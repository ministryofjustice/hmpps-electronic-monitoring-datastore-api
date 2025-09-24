package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity.AthenaIntegrityEquipmentDetailsDTO
import java.time.LocalDateTime

class IntegrityEquipmentDetailsTest {
  @Nested
  inner class Construct {
    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any equipment details`() {
      val athenaDto = AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = null,
        hmuEquipmentCategoryDescription = null,
        hmuInstallDate = null,
        hmuInstallTime = null,
        hmuRemovedDate = null,
        hmuRemovedTime = null,
        pidId = null,
        pidEquipmentCategoryDescription = null,
        dateDeviceInstalled = null,
        timeDeviceInstalled = null,
        dateDeviceRemoved = null,
        timeDeviceRemoved = null,
      )

      val model = IntegrityEquipmentDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = null,
          hmu = null,
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any home monitoring unit equipment details`() {
      val athenaDto = AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = null,
        hmuEquipmentCategoryDescription = null,
        hmuInstallDate = null,
        hmuInstallTime = null,
        hmuRemovedDate = null,
        hmuRemovedTime = null,
        pidId = "pidId",
        pidEquipmentCategoryDescription = "pidEquipmentCategoryDescription",
        dateDeviceInstalled = "2020-02-02",
        timeDeviceInstalled = "02:02:02",
        dateDeviceRemoved = "2020-03-03",
        timeDeviceRemoved = "03:03:03",
      )

      val model = IntegrityEquipmentDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = IntegrityEquipmentDetails.Equipment(
            id = "pidId",
            equipmentCategoryDescription = "pidEquipmentCategoryDescription",
            installedDateTime = LocalDateTime.parse("2020-02-02T02:02:02"),
            removedDateTime = LocalDateTime.parse("2020-03-03T03:03:03"),
          ),
          hmu = null,
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any personal equipment details`() {
      val athenaDto = AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = "hmu_id",
        hmuEquipmentCategoryDescription = "hmu_equipment_category_description",
        hmuInstallDate = "2020-02-02",
        hmuInstallTime = "02:02:02",
        hmuRemovedDate = "2020-03-03",
        hmuRemovedTime = "03:03:03",
        pidId = null,
        pidEquipmentCategoryDescription = null,
        dateDeviceInstalled = null,
        timeDeviceInstalled = null,
        dateDeviceRemoved = null,
        timeDeviceRemoved = null,
      )

      val model = IntegrityEquipmentDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = null,
          hmu = IntegrityEquipmentDetails.Equipment(
            id = "hmu_id",
            equipmentCategoryDescription = "hmu_equipment_category_description",
            installedDateTime = LocalDateTime.parse("2020-02-02T02:02:02"),
            removedDateTime = LocalDateTime.parse("2020-03-03T03:03:03"),
          ),
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped with just equipment ids`() {
      val athenaDto = AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = "hmu_id",
        hmuEquipmentCategoryDescription = null,
        hmuInstallDate = null,
        hmuInstallTime = null,
        hmuRemovedDate = null,
        hmuRemovedTime = null,
        pidId = "pid_id",
        pidEquipmentCategoryDescription = null,
        dateDeviceInstalled = null,
        timeDeviceInstalled = null,
        dateDeviceRemoved = null,
        timeDeviceRemoved = null,
      )

      val model = IntegrityEquipmentDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = IntegrityEquipmentDetails.Equipment(
            id = "pid_id",
            equipmentCategoryDescription = null,
            installedDateTime = null,
            removedDateTime = null,
          ),
          hmu = IntegrityEquipmentDetails.Equipment(
            id = "hmu_id",
            equipmentCategoryDescription = null,
            installedDateTime = null,
            removedDateTime = null,
          ),
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped with no times`() {
      val athenaDto = AthenaIntegrityEquipmentDetailsDTO(
        legacySubjectId = "123",
        hmuId = "hmu_id",
        hmuEquipmentCategoryDescription = null,
        hmuInstallDate = "2021-02-02",
        hmuInstallTime = null,
        hmuRemovedDate = "2022-02-02",
        hmuRemovedTime = null,
        pidId = "pid_id",
        pidEquipmentCategoryDescription = null,
        dateDeviceInstalled = "2021-02-02",
        timeDeviceInstalled = null,
        dateDeviceRemoved = "2022-02-02",
        timeDeviceRemoved = null,
      )

      val model = IntegrityEquipmentDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityEquipmentDetails(
          legacySubjectId = "123",
          pid = IntegrityEquipmentDetails.Equipment(
            id = "pid_id",
            equipmentCategoryDescription = null,
            installedDateTime = LocalDateTime.parse("2021-02-02T00:00:00"),
            removedDateTime = LocalDateTime.parse("2022-02-02T00:00:00"),
          ),
          hmu = IntegrityEquipmentDetails.Equipment(
            id = "hmu_id",
            equipmentCategoryDescription = null,
            installedDateTime = LocalDateTime.parse("2021-02-02T00:00:00"),
            removedDateTime = LocalDateTime.parse("2022-02-02T00:00:00"),
          ),
        ),
      )
    }
  }
}
