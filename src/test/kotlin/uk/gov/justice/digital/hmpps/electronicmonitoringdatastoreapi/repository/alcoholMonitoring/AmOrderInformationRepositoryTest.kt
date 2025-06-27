package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.alcoholMonitoring.AthenaAmOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow

class AmOrderInformationRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderInformationRepository

  @BeforeEach
  fun setup() {
    emDatastoreClient = mock(EmDatastoreClient::class.java)
    repository = AmOrderInformationRepository(emDatastoreClient)
  }

  @Test
  fun `AmOrderInformationRepository can be instantiated`() {
    val sut = AmOrderInformationRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  fun varCharValueColumn(value: String) = """
    {
      "VarCharValue": "$value"
    }
  """.trimIndent()

  @Nested
  inner class GetAmOrderInformation {
    fun amOrderInformationData(subjectId: String, orderId: String, firstName: String, lastName: String) = """
      {
        "Data": [
          ${varCharValueColumn(subjectId)},
          ${varCharValueColumn(orderId)},
          ${varCharValueColumn(firstName)},
          ${varCharValueColumn(lastName)},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("address line 1")},
          ${varCharValueColumn("")},
          ${varCharValueColumn("address line 3")},
          ${varCharValueColumn("postcode")},
          ${varCharValueColumn("1970-01-01")},
          ${varCharValueColumn("1970-01-01")}
        ]
      }
    """.trimIndent()

    fun amOrderInformationResultSet(firstOrderId: String? = "1253587", firstFirstName: String? = "ELLEN", firstLastName: String? = "RIPLEY") = """
      {
        "ResultSet": {
          "Rows": [
            {
              "Data": [
                ${varCharValueColumn("legacy_subject_id")},
                ${varCharValueColumn("legacy_order_id")},
                ${varCharValueColumn("first_name")},
                ${varCharValueColumn("last_name")},
                ${varCharValueColumn("date_of_birth")},
                ${varCharValueColumn("primary_address_line_1")},
                ${varCharValueColumn("primary_address_line_2")},
                ${varCharValueColumn("primary_address_line_3")},
                ${varCharValueColumn("primary_address_post_code")},
                ${varCharValueColumn("order_start_date")},
                ${varCharValueColumn("order_end_date")}
              ]
            },
            ${amOrderInformationData(firstOrderId!!, firstOrderId, firstFirstName!!, firstLastName!!)},
            ${amOrderInformationData("1034415", "1032792", "JOHN", "BROWNLIE")}
          ],
          "ResultSetMetadata": {
            "ColumnInfo": [
              """ + metaDataRow("legacy_subject_id") + """,
              """ + metaDataRow("legacy_order_id") + """,
              """ + metaDataRow("first_name") + """,
              """ + metaDataRow("last_name") + """,
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
    fun `getOrderInformation calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getOrderInformation("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getOrderInformation returns an AthenaAmOrderInformationDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderInformationResultSet())

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getOrderInformation("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isInstanceOf(AthenaAmOrderInformationDTO::class.java)
    }

    @Test
    fun `getOrderInformation returns the first result from getQueryResult`() {
      val legacySubjectId = "orderId007"
      val firstName = "TEST FIRST NAME"
      val lastName = "TEST LAST NAME"

      val resultSet = AthenaHelper.resultSetFromJson(amOrderInformationResultSet(legacySubjectId, firstName, lastName))

      `when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getOrderInformation(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacyOrderId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
      Assertions.assertThat(result.firstName).isEqualTo(firstName)
      Assertions.assertThat(result.lastName).isEqualTo(lastName)
    }
  }
}
