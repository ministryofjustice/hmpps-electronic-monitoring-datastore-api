package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

class AmOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock(EmDatastoreClient::class.java)
    repository = AmOrderDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetAmOrderDetails {
    fun amOrderDetailsData(subjectId: String, orderId: String, firstName: String, lastName: String): Array<String> = arrayOf(
      subjectId,
      firstName,
      lastName,
      "alias",
      "1970-01-01",
      "sex",
      "special instructions",
      "09876543210",
      "address line 1",
      "",
      "address line 3",
      "postcode",
      orderId,
      "1970-01-01",
      "1970-01-01",
      "enforceable condition",
      "order type",
      "order type description",
      "order end outcome",
      "responsible organisation phone number",
      "responsible organisation email",
      "tag at source",
    )

    fun amOrderDetailsResultSet(firstSubjectId: String? = "7853521", firstOrderId: String? = "1253587", firstFirstName: String? = "ELLEN", firstLastName: String? = "RIPLEY"): String = MockAthenaResultSetBuilder(
      columns = arrayOf(
        "legacy_subject_id",
        "first_name",
        "last_name",
        "alias",
        "date_of_birth",
        "legacy_gender",
        "special_instructions",
        "phone_or_mobile_number",
        "primary_address_line_1",
        "primary_address_line_2",
        "primary_address_line_3",
        "primary_address_post_code",
        "legacy_order_id",
        "order_start_date",
        "order_end_date",
        "enforceable_condition",
        "order_type",
        "order_type_description",
        "order_end_outcome",
        "responsible_org_details_phone_number",
        "responsible_org_details_email",
        "tag_at_source",
      ),
      rows = arrayOf(
        amOrderDetailsData(firstOrderId!!, firstSubjectId!!, firstFirstName!!, firstLastName!!),
        amOrderDetailsData("1034415", "1032792", "JOHN", "BROWNLIE"),
      ),
    ).build()

    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getOrderDetails("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "subjectId777"
      val legacyOrderId = "orderId777"
      val firstName = "TEST FIRST NAME"
      val lastName = "TEST LAST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet(legacyOrderId, legacySubjectId, firstName, lastName))

      `when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderDetails(legacySubjectId)

      Assertions.assertThat(result.legacyOrderId).isEqualTo(legacyOrderId)
      Assertions.assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.firstName).isEqualTo(firstName)
      Assertions.assertThat(result.lastName).isEqualTo(lastName)
    }
  }
}
