package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.varCharValueColumn

class AmOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: AmOrderDetailsRepository

  fun amOrderDetailsData(subjectId: String, orderId: String, responsibleOrgDetailsPhoneNumber: String) = """
    {
      "Data": [
        ${varCharValueColumn(subjectId)},
        ${varCharValueColumn(orderId)},
        ${varCharValueColumn(responsibleOrgDetailsPhoneNumber)},
      ]
    }
  """.trimIndent()

  fun amOrderDetailsResultSet(
    subjectId: String? = "1234",
    orderId: String? = "fakeOrderId",
    phoneNumber: String? = "fakePhoneNumber",
  ) = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("legacy_subject_id")},
            ]
          },
          ${amOrderDetailsData(subjectId!!, orderId!!, phoneNumber!!)},
          ${amOrderDetailsData("5678", "This row should never be returned", "01134567890")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("legacy_subject_id")},
            ${metaDataRow("legacy_order_id")},
            ${metaDataRow("responsible_org_details_phone_number", "boolean")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = AmOrderDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `AmOrderDetailsRepository can be instantiated`() {
    val sut = SearchRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `getAmOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      repository.getAmOrderDetails("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), eq(false))
    }

    @Test
    fun `getAmOrderDetails returns an AthenaAmOrderDetailsDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getAmOrderDetails("123", false)

      Assertions.assertThat(result).isInstanceOf(AthenaAmOrderDetailsDTO::class.java)
    }

    @Test
    fun `getAmOrderDetails returns the first result from getQueryResult`() {
      val orderId = "expectedId"
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet(orderId))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), eq(false))).thenReturn(resultSet)

      val result = repository.getAmOrderDetails(orderId, false)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo(orderId)
    }

    @Test
    fun `Throws an exception if orderId is not alphanumeric`() {
      val dangerousInput = "123 OR 1=1"

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        repository.getAmOrderDetails(dangerousInput, false)
      }.withMessage("Input contains illegal characters")
    }
  }
}
