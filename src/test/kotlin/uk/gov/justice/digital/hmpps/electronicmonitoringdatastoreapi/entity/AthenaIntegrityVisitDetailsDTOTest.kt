package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.entity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

class AthenaIntegrityVisitDetailsDTOTest {
  @Nested
  inner class Construct {
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
    fun `AthenaIntegrityVisitDetailsDTO can be mapped with all attributes`() {
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

      val dto = AthenaHelper.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaIntegrityVisitDetailsDTO(
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
    fun `AthenaIntegrityVisitDetailsDTO can be mapped without any address details`() {
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

      val dto = AthenaHelper.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaIntegrityVisitDetailsDTO(
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
    fun `AthenaIntegrityVisitDetailsDTO can be mapped without any visit notes`() {
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

      val dto = AthenaHelper.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaIntegrityVisitDetailsDTO(
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
    fun `AthenaIntegrityVisitDetailsDTO can be mapped without any visit outcome`() {
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

      val dto = AthenaHelper.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaIntegrityVisitDetailsDTO(
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
    fun `AthenaIntegrityVisitDetailsDTO can be mapped without any end date`() {
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

      val dto = AthenaHelper.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaIntegrityVisitDetailsDTO(
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
}
