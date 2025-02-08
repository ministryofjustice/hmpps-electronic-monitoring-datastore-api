package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.athena

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaEquipmentDetailsDTO

class AthenaEquipmentDetailsDtoTest {
  @Nested
  inner class Construct {
    val columnNames = arrayOf<String>(
      "legacy_subject_id",
      "legacy_order_id",
      "hmu_id",
      "hmu_equipment_category_description",
      "hmu_install_date",
      "hmu_install_time",
      "hmu_removed_date",
      "hmu_removed_time",
      "pid_id",
      "pid_equipment_category_description",
      "date_device_installed",
      "time_device_installed",
      "date_device_removed",
      "time_device_removed",
    )

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any equipment details`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
            "123",
            "321",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
          ),
        ),
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaEquipmentDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaEquipmentDetailsDTO(
          legacySubjectId = 123,
          legacyOrderId = 321,
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
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any home monitoring unit equipment details`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
            "123",
            "321",
            "",
            "",
            "",
            "",
            "",
            "",
            "pidId",
            "pidEquipmentCategoryDescription",
            "dateDeviceInstalled",
            "timeDeviceInstalled",
            "dateDeviceRemoved",
            "timeDeviceRemoved",
          ),
        ),
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaEquipmentDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaEquipmentDetailsDTO(
          legacySubjectId = 123,
          legacyOrderId = 321,
          hmuId = null,
          hmuEquipmentCategoryDescription = null,
          hmuInstallDate = null,
          hmuInstallTime = null,
          hmuRemovedDate = null,
          hmuRemovedTime = null,
          pidId = "pidId",
          pidEquipmentCategoryDescription = "pidEquipmentCategoryDescription",
          dateDeviceInstalled = "dateDeviceInstalled",
          timeDeviceInstalled = "timeDeviceInstalled",
          dateDeviceRemoved = "dateDeviceRemoved",
          timeDeviceRemoved = "timeDeviceRemoved",
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped without any personal equipment details`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
            "123",
            "321",
            "hmu_id",
            "hmu_equipment_category_description",
            "hmu_install_date",
            "hmu_install_time",
            "hmu_removed_date",
            "hmu_removed_time",
            "",
            "",
            "",
            "",
            "",
            "",
          ),
        ),
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaEquipmentDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaEquipmentDetailsDTO(
          legacySubjectId = 123,
          legacyOrderId = 321,
          hmuId = "hmu_id",
          hmuEquipmentCategoryDescription = "hmu_equipment_category_description",
          hmuInstallDate = "hmu_install_date",
          hmuInstallTime = "hmu_install_time",
          hmuRemovedDate = "hmu_removed_date",
          hmuRemovedTime = "hmu_removed_time",
          pidId = null,
          pidEquipmentCategoryDescription = null,
          dateDeviceInstalled = null,
          timeDeviceInstalled = null,
          dateDeviceRemoved = null,
          timeDeviceRemoved = null,
        ),
      )
    }

    @Test
    fun `AthenaEquipmentDetailsDto can be mapped with only equipment ids`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
            "123",
            "321",
            "hmu_id",
            "",
            "",
            "",
            "",
            "",
            "pid_id",
            "",
            "",
            "",
            "",
            "",
          ),
        ),
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaEquipmentDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaEquipmentDetailsDTO(
          legacySubjectId = 123,
          legacyOrderId = 321,
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
        ),
      )
    }
  }
}
