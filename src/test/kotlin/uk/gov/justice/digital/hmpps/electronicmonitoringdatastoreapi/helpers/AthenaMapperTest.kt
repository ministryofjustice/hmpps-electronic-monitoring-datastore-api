package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.EquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.Violations
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.VisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

@ActiveProfiles("test")
class AthenaMapperTest {

  @Nested
  inner class AthenaViolations {
    val columnNames = mapOf(
      "legacy_subject_id" to "varchar",
      "enforcement_reason" to "varchar",
      "investigation_outcome_reason" to "varchar",
      "breach_details" to "varchar",
      "breach_enforcement_outcome" to "varchar",
      "agency_action" to "varchar",
      "breach_date" to "varchar",
      "breach_time" to "varchar",
      "breach_identified_date" to "varchar",
      "breach_identified_time" to "varchar",
      "authority_first_notified_date" to "varchar",
      "authority_first_notified_time" to "varchar",
      "agency_response_date" to "varchar",
      "breach_pack_requested_date" to "varchar",
      "breach_pack_sent_date" to "varchar",
      "section_9_date" to "varchar",
      "hearing_date" to "varchar",
      "summons_served_date" to "varchar",
      "subject_letter_sent_date" to "varchar",
      "warning_letter_sent_date" to "varchar",
      "warning_letter_sent_time" to "varchar",
    )

    @Test
    fun `Violations can be mapped with all attributes`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "TEST_ENFORCEMENT_REASON",
            "TEST_OUTCOME_REASON",
            "some details",
            "TEST_OUTCOME_REASON",
            "TEST_ACTION",
            "2003-03-03",
            "03:03:03",
            "2004-04-04",
            "04:04:04",
            "2005-05-05",
            "05:05:05",
            "2006-06-06",
            "2006-07-07",
            "2007-07-07",
            "2008-08-08",
            "2009-09-09",
            "2010-10-10",
            "2011-11-11",
            "2012-12-12",
            "12:12:12",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(Violations::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        Violations(
          legacySubjectId = "123",
          enforcementReason = "TEST_ENFORCEMENT_REASON",
          investigationOutcomeReason = "TEST_OUTCOME_REASON",
          breachDetails = "some details",
          breachEnforcementOutcome = "TEST_OUTCOME_REASON",
          agencyAction = "TEST_ACTION",
          breachDate = "2003-03-03",
          breachTime = "03:03:03",
          breachIdentifiedDate = "2004-04-04",
          breachIdentifiedTime = "04:04:04",
          authorityFirstNotifiedDate = "2005-05-05",
          authorityFirstNotifiedTime = "05:05:05",
          agencyResponseDate = "2006-06-06",
          breachPackRequestedDate = "2006-07-07",
          breachPackSentDate = "2007-07-07",
          section9Date = "2008-08-08",
          hearingDate = "2009-09-09",
          summonsServedDate = "2010-10-10",
          subjectLetterSentDate = "2011-11-11",
          warningLetterSentDate = "2012-12-12",
          warningLetterSentTime = "12:12:12",
        ),
      )
    }

    @Test
    fun `Violations can be mapped with just ids `() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
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
      ).toResultSet()

      val dto = AthenaMapper(Violations::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        Violations(
          legacySubjectId = "123",
          enforcementReason = null,
          investigationOutcomeReason = null,
          breachDetails = null,
          breachEnforcementOutcome = null,
          agencyAction = null,
          breachDate = null,
          breachTime = null,
          breachIdentifiedDate = null,
          breachIdentifiedTime = null,
          authorityFirstNotifiedDate = null,
          authorityFirstNotifiedTime = null,
          agencyResponseDate = null,
          breachPackRequestedDate = null,
          breachPackSentDate = null,
          section9Date = null,
          hearingDate = null,
          summonsServedDate = null,
          subjectLetterSentDate = null,
          warningLetterSentDate = null,
          warningLetterSentTime = null,
        ),
      )
    }
  }

  @Nested
  inner class AthenaVisitDetails {
    val columnNames = mapOf(
      "legacy_subject_id" to "varchar",
      "address_1" to "varchar",
      "address_2" to "varchar",
      "address_3" to "varchar",
      "postcode" to "varchar",
      "actual_work_start_date" to "varchar",
      "actual_work_start_time" to "varchar",
      "actual_work_end_date" to "varchar",
      "actual_work_end_time" to "varchar",
      "visit_notes" to "varchar",
      "visit_type" to "varchar",
      "visit_outcome" to "varchar",
    )

    @Test
    fun `VisitDetails can be mapped with all attributes`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "address_1",
            "address_2",
            "address_3",
            "postcode",
            "2020-20-20",
            "02:02:02",
            "3030-30-30",
            "03:03:03",
            "TEST_NOTES",
            "TEST_VISIT_TYPE",
            "TEST_OUTCOME",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(VisitDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        VisitDetails(
          legacySubjectId = "123",
          address1 = "address_1",
          address2 = "address_2",
          address3 = "address_3",
          postcode = "postcode",
          actualWorkStartDate = "2020-20-20",
          actualWorkStartTime = "02:02:02",
          actualWorkEndDate = "3030-30-30",
          actualWorkEndTime = "03:03:03",
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any address details`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "",
            "",
            "",
            "",
            "2020-20-20",
            "02:02:02",
            "3030-30-30",
            "03:03:03",
            "TEST_NOTES",
            "TEST_VISIT_TYPE",
            "TEST_OUTCOME",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(VisitDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        VisitDetails(
          legacySubjectId = "123",
          address1 = null,
          address2 = null,
          address3 = null,
          postcode = null,
          actualWorkStartDate = "2020-20-20",
          actualWorkStartTime = "02:02:02",
          actualWorkEndDate = "3030-30-30",
          actualWorkEndTime = "03:03:03",
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any visit notes`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "address_1",
            "address_2",
            "address_3",
            "postcode",
            "2020-20-20",
            "02:02:02",
            "3030-30-30",
            "03:03:03",
            "",
            "TEST_VISIT_TYPE",
            "TEST_OUTCOME",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(VisitDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        VisitDetails(
          legacySubjectId = "123",
          address1 = "address_1",
          address2 = "address_2",
          address3 = "address_3",
          postcode = "postcode",
          actualWorkStartDate = "2020-20-20",
          actualWorkStartTime = "02:02:02",
          actualWorkEndDate = "3030-30-30",
          actualWorkEndTime = "03:03:03",
          visitNotes = null,
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any visit outcome`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "address_1",
            "address_2",
            "address_3",
            "postcode",
            "2020-20-20",
            "02:02:02",
            "3030-30-30",
            "03:03:03",
            "TEST_NOTES",
            "TEST_VISIT_TYPE",
            "",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(VisitDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        VisitDetails(
          legacySubjectId = "123",
          address1 = "address_1",
          address2 = "address_2",
          address3 = "address_3",
          postcode = "postcode",
          actualWorkStartDate = "2020-20-20",
          actualWorkStartTime = "02:02:02",
          actualWorkEndDate = "3030-30-30",
          actualWorkEndTime = "03:03:03",
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = null,
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any end date`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
            "address_1",
            "address_2",
            "address_3",
            "postcode",
            "2020-20-20",
            "02:02:02",
            "",
            "",
            "TEST_NOTES",
            "TEST_VISIT_TYPE",
            "TEST_OUTCOME",
          ),
        ),
      ).toResultSet()

      val dto = AthenaMapper(VisitDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        VisitDetails(
          legacySubjectId = "123",
          address1 = "address_1",
          address2 = "address_2",
          address3 = "address_3",
          postcode = "postcode",
          actualWorkStartDate = "2020-20-20",
          actualWorkStartTime = "02:02:02",
          actualWorkEndDate = null,
          actualWorkEndTime = null,
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }
  }

  @Nested
  inner class AthenaEquipmentDetails {
    val columnNames = mapOf(
      "legacy_subject_id" to "bigint",
      "hmu_id" to "bigint",
      "hmu_equipment_category_description" to "varchar",
      "hmu_install_date" to "varchar",
      "hmu_install_time" to "varchar",
      "hmu_removed_date" to "varchar",
      "hmu_removed_time" to "varchar",
      "pid_id" to "bigint",
      "pid_equipment_category_description" to "varchar",
      "date_device_installed" to "varchar",
      "time_device_installed" to "varchar",
      "date_device_removed" to "varchar",
      "time_device_removed" to "varchar",
    )

    @Test
    fun `EquipmentDetails can be mapped without any equipment details`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
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
      ).toResultSet()

      val dto = AthenaMapper(EquipmentDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        EquipmentDetails(
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
        ),
      )
    }

    @Test
    fun `EquipmentDetails can be mapped without any home monitoring unit equipment details`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
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
      ).toResultSet()

      val dto = AthenaMapper(EquipmentDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        EquipmentDetails(
          legacySubjectId = "123",
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
    fun `EquipmentDetails can be mapped without any personal equipment details`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
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
      ).toResultSet()

      val dto = AthenaMapper(EquipmentDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        EquipmentDetails(
          legacySubjectId = "123",
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
    fun `EquipmentDetails can be mapped with only equipment ids`() {
      val athenaResponse = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf(
            "123",
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
      ).toResultSet()

      val dto = AthenaMapper(EquipmentDetails::class).mapTo(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        EquipmentDetails(
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
        ),
      )
    }
  }
}
