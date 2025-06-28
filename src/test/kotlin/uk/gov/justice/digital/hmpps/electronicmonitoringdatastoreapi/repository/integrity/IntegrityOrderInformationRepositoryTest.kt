package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow

class IntegrityOrderInformationRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityOrderInformationRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock(EmDatastoreClient::class.java)
    repository = IntegrityOrderInformationRepository(emDatastoreClient)
  }

  @Test
  fun `OrderInformationRepository can be instantiated`() {
    val sut = IntegrityOrderInformationRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  fun varCharValueColumn(value: String) = """
    {
      "VarCharValue": "$value"
    }
  """.trimIndent()

  @Nested
  inner class GetKeyOrderInformation {
    fun keyOrderInformationData(subjectId: String, orderId: String, fullName: String) = """
      {
        "Data": [
          ${varCharValueColumn(subjectId)},
          ${varCharValueColumn(orderId)},
          ${varCharValueColumn(fullName)},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("address line 1")},
          ${varCharValueColumn("address line 2")},
          ${varCharValueColumn("")},
          ${varCharValueColumn("postcode")},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("1970-01-01")}
        ]
      }
    """.trimIndent()

    fun keyOrderInformationResultSet(firstOrderId: String? = "1253587", firstFullName: String? = "ELLEN RIPLEY") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("full_name")},
                ${varCharValueColumn("date_of_birth")},
                ${varCharValueColumn("primary_address_line_1")},
                ${varCharValueColumn("primary_address_line_2")},
                ${varCharValueColumn("primary_address_line_3")},
                ${varCharValueColumn("primary_address_post_code")},
                ${varCharValueColumn("order_start_date")},
                ${varCharValueColumn("order_end_date")}
              ]
            },
            ${keyOrderInformationData(firstOrderId!!, firstOrderId, firstFullName!!)},
            ${keyOrderInformationData("1034415", "1032792", "JOHN BROWNLIE")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              """ + metaDataRow("legacy_subject_id") + """,
              """ + metaDataRow("legacy_order_id") + """,
              """ + metaDataRow("full_name") + """,
              """ + metaDataRow("date_of_birth") + """,
              """ + metaDataRow("primary_address_line_1") + """,
              """ + metaDataRow("primary_address_line_2") + """,
              """ + metaDataRow("primary_address_line_3") + """,
              """ + metaDataRow("primary_address_post_code") + """,
              """ + metaDataRow("order_start_date") + """,
              """ + metaDataRow("order_end_date") + """
            ]
          }
        },
        "UpdateCount": 0
      }
    """.trimIndent()

    @Test
    fun `getKeyOrderInformation calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getOrderInformation("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getKeyOrderInformation returns an AthenaKeyOrderInformationDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderInformation("123", false)

      Assertions.assertThat(result).isInstanceOf(AthenaIntegrityOrderInformationDTO::class.java)
    }

    @Test
    fun `getKeyOrderInformation returns the first result from getQueryResult`() {
      val legacySubjectId = "orderId004"
      val fullName = "TEST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(keyOrderInformationResultSet(legacySubjectId, fullName))

      `when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderInformation(legacySubjectId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacyOrderId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.name).isEqualTo(fullName)
    }
  }
}
