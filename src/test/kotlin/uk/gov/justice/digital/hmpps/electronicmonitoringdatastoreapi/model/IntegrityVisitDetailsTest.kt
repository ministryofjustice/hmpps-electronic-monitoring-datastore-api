package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.VisitDetails
import java.time.LocalDateTime

class IntegrityVisitDetailsTest {
  @Nested
  inner class Construct {
    @Test
    fun `VisitDetails can be mapped with all attributes`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = "address_1",
        address2 = "address_2",
        address3 = "address_3",
        postcode = "postcode",
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = "3030-03-30",
        actualWorkEndTime = "03:03:03",
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "address_1",
            addressLine2 = "address_2",
            addressLine3 = "address_3",
            addressLine4 = null,
            postcode = "postcode",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = LocalDateTime.parse("3030-03-30T03:03:03"),
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped with the minimum details`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = null,
        address2 = null,
        address3 = null,
        postcode = null,
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = null,
        actualWorkEndTime = null,
        visitNotes = null,
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = null,
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = null,
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = null,
          visitNotes = null,
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = null,
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any address details`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = null,
        address2 = null,
        address3 = null,
        postcode = null,
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = "3030-03-30",
        actualWorkEndTime = "03:03:03",
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = null,
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = LocalDateTime.parse("3030-03-30T03:03:03"),
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any visit notes`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = "address_1",
        address2 = "address_2",
        address3 = "address_3",
        postcode = "postcode",
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = "3030-03-30",
        actualWorkEndTime = "03:03:03",
        visitNotes = null,
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "address_1",
            addressLine2 = "address_2",
            addressLine3 = "address_3",
            addressLine4 = null,
            postcode = "postcode",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = LocalDateTime.parse("3030-03-30T03:03:03"),
          visitNotes = null,
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any visit outcome`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = "address_1",
        address2 = "address_2",
        address3 = "address_3",
        postcode = "postcode",
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = "3030-03-30",
        actualWorkEndTime = "03:03:03",
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = null,
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "address_1",
            addressLine2 = "address_2",
            addressLine3 = "address_3",
            addressLine4 = null,
            postcode = "postcode",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = LocalDateTime.parse("3030-03-30T03:03:03"),
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = null,
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without any end date`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = "address_1",
        address2 = "address_2",
        address3 = "address_3",
        postcode = "postcode",
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = null,
        actualWorkEndTime = null,
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "address_1",
            addressLine2 = "address_2",
            addressLine3 = "address_3",
            addressLine4 = null,
            postcode = "postcode",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = null,
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without a complete address`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = null,
        address2 = null,
        address3 = null,
        postcode = "postcode",
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = null,
        actualWorkEndTime = null,
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = null,
            addressLine2 = null,
            addressLine3 = null,
            addressLine4 = null,
            postcode = "postcode",
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = null,
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }

    @Test
    fun `VisitDetails can be mapped without a postcode`() {
      val athenaDto = VisitDetails(
        legacySubjectId = "123",
        address1 = "address line 1",
        address2 = null,
        address3 = null,
        postcode = null,
        actualWorkStartDate = "2020-02-20",
        actualWorkStartTime = "02:02:02",
        actualWorkEndDate = null,
        actualWorkEndTime = null,
        visitNotes = "TEST_NOTES",
        visitType = "TEST_VISIT_TYPE",
        visitOutcome = "TEST_OUTCOME",
      )

      val model = IntegrityVisitDetails(athenaDto)

      Assertions.assertThat(model).isEqualTo(
        IntegrityVisitDetails(
          legacySubjectId = "123",
          address = IntegrityVisitDetails.Address(
            addressLine1 = "address line 1",
            addressLine2 = null,
            addressLine3 = null,
            addressLine4 = null,
            postcode = null,
          ),
          actualWorkStartDateTime = LocalDateTime.parse("2020-02-20T02:02:02"),
          actualWorkEndDateTime = null,
          visitNotes = "TEST_NOTES",
          visitType = "TEST_VISIT_TYPE",
          visitOutcome = "TEST_OUTCOME",
        ),
      )
    }
  }
}
