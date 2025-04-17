package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityVisitDetailsDTO

class AthenaVisitDetailsDTOTest {
  @Nested
  inner class Construct {
    val columnNames = arrayOf<String>(
      "legacy_subject_id",
      "address_1",
      "address_2",
      "address_3",
      "postcode",
      "actual_work_start_date",
      "actual_work_start_time",
      "actual_work_end_date",
      "actual_work_end_time",
      "visit_notes",
      "visit_type",
      "visit_outcome",
    )

    @Test
    fun `AthenaVisitDetailsDTO can be mapped with all attributes`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

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
    fun `AthenaVisitDetailsDTO can be mapped without any address details`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

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
    fun `AthenaVisitDetailsDTO can be mapped without any visit notes`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

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
    fun `AthenaVisitDetailsDTO can be mapped without any visit outcome`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

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
    fun `AthenaVisitDetailsDTO can be mapped without any end date`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaIntegrityVisitDetailsDTO>(athenaResponse)

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
