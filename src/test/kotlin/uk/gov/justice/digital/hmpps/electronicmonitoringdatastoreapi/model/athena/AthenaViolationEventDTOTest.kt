package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.mocks.MockAthenaResultSetBuilder

class AthenaViolationEventDTOTest {

  @Nested
  inner class Construct {
    val columnNames = arrayOf<String>(
      "legacy_subject_id",
      "enforcement_reason",
      "investigation_outcome_reason",
      "breach_details",
      "breach_enforcement_outcome",
      "agency_action",
      "breach_date",
      "breach_time",
      "breach_identified_date",
      "breach_identified_time",
      "authority_first_notified_date",
      "authority_first_notified_time",
      "agency_response_date",
      "breach_pack_requested_date",
      "breach_pack_sent_date",
      "section_9_date",
      "hearing_date",
      "summons_served_date",
      "subject_letter_sent_date",
      "warning_letter_sent_date",
      "warning_letter_sent_time",
    )

    @Test
    fun `AthenaViolationEventDto can be mapped with all attributes`() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaViolationEventDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaViolationEventDTO(
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
    fun `AthenaViolationEventDto can be mapped with just ids `() {
      val athenaResponseString = MockAthenaResultSetBuilder(
        columns = columnNames,
        rows = arrayOf(
          arrayOf<String>(
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
      ).build()
      val athenaResponse = AthenaHelper.resultSetFromJson(athenaResponseString)
      val dto = AthenaHelper.Companion.mapTo<AthenaViolationEventDTO>(athenaResponse)

      Assertions.assertThat(dto.first()).isEqualTo(
        AthenaViolationEventDTO(
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
}
