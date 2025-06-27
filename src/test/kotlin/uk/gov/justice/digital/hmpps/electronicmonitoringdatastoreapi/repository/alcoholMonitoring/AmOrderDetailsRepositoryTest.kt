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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow

class AmOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderDetailsRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock(EmDatastoreClient::class.java)
    repository = AmOrderDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `AmOrderDetailsRepository can be instantiated`() {
    val sut = AmOrderDetailsRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  fun varCharValueColumn(value: String) = """
    {
      "VarCharValue": "$value"
    }
  """.trimIndent()

  @Nested
  inner class GetAmOrderDetails {
    fun amOrderDetailsData(subjectId: String, orderId: String, firstName: String, lastName: String) = """
      {
        "Data": [
          ${varCharValueColumn(subjectId)},
          ${varCharValueColumn(firstName)},
          ${varCharValueColumn(lastName)},
          ${varCharValueColumn("alias")},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("sex")},
          ${varCharValueColumn("special instructions")},
          ${varCharValueColumn("09876543210")},
          ${varCharValueColumn("address line 1")},
          ${varCharValueColumn("")},
          ${varCharValueColumn("address line 3")},
          ${varCharValueColumn("postcode")},
          ${varCharValueColumn(orderId)},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("enforceable condition")},
          ${varCharValueColumn("order type")},
          ${varCharValueColumn("order type description")},
          ${varCharValueColumn("order end outcome")},
          ${varCharValueColumn("responsible organisation phone number")},
          ${varCharValueColumn("responsible organisation email")},
          ${varCharValueColumn("tag at source")}
        ]
      }
    """.trimIndent()

    fun amOrderDetailsResultSet(firstSubjectId: String? = "7853521", firstOrderId: String? = "1253587", firstFirstName: String? = "ELLEN", firstLastName: String? = "RIPLEY") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("first_name")},
                ${varCharValueColumn("last_name")},
                ${varCharValueColumn("alias")},
                ${varCharValueColumn("date_of_birth")},
                ${varCharValueColumn("legacy_gender")},
                ${varCharValueColumn("special_instructions")},
                ${varCharValueColumn("phone_or_mobile_number")},
                ${varCharValueColumn("primary_address_line_1")},
                ${varCharValueColumn("primary_address_line_2")},
                ${varCharValueColumn("primary_address_line_3")},
                ${varCharValueColumn("primary_address_post_code")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("order_start_date")},
                ${varCharValueColumn("order_end_date")},
                ${varCharValueColumn("enforceable_condition")},
                ${varCharValueColumn("order_type")},
                ${varCharValueColumn("order_type_description")},
                ${varCharValueColumn("order_end_outcome")},
                ${varCharValueColumn("responsible_org_details_phone_number")},
                ${varCharValueColumn("responsible_org_details_email")},
                ${varCharValueColumn("tag_at_source")}
              ]
            },
            ${amOrderDetailsData(firstOrderId!!, firstSubjectId!!, firstFirstName!!, firstLastName!!)},
            ${amOrderDetailsData("1034415", "1032792", "JOHN", "BROWNLIE")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              """ + metaDataRow("legacy_subject_id") + """,
              """ + metaDataRow("first_name") + """,
              """ + metaDataRow("last_name") + """,
              """ + metaDataRow("alias") + """,
              """ + metaDataRow("date_of_birth") + """,
              """ + metaDataRow("legacy_gender") + """,
              """ + metaDataRow("special_instructions") + """,
              """ + metaDataRow("phone_or_mobile_number") + """,
              """ + metaDataRow("primary_address_line_1") + """,
              """ + metaDataRow("primary_address_line_2") + """,
              """ + metaDataRow("primary_address_line_3") + """,
              """ + metaDataRow("primary_address_post_code") + """,
              """ + metaDataRow("legacy_order_id") + """,
              """ + metaDataRow("order_start_date") + """,
              """ + metaDataRow("order_end_date") + """,
              """ + metaDataRow("enforceable_condition") + """,
              """ + metaDataRow("order_type") + """,
              """ + metaDataRow("order_type_description") + """,
              """ + metaDataRow("order_end_outcome") + """,
              """ + metaDataRow("responsible_org_details_phone_number") + """,
              """ + metaDataRow("responsible_org_details_email") + """,
              """ + metaDataRow("tag_at_source") + """
            ]
          }
        },
        "UpdateCount": 0
      }
    """.trimIndent()

    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      repository.getOrderDetails("123")

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), eq(false))
    }

    @Test
    fun `getOrderDetails returns an AthenaAmOrderDetailsDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderDetails("123")

      Assertions.assertThat(result).isInstanceOf(AthenaAmOrderDetailsDTO::class.java)
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "subjectId777"
      val legacyOrderId = "orderId777"
      val firstName = "TEST FIRST NAME"
      val lastName = "TEST LAST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet(legacyOrderId, legacySubjectId, firstName, lastName))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderDetails(legacySubjectId)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacyOrderId).isEqualTo(legacyOrderId)
      Assertions.assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.firstName).isEqualTo(firstName)
      Assertions.assertThat(result.lastName).isEqualTo(lastName)
    }
  }
}
