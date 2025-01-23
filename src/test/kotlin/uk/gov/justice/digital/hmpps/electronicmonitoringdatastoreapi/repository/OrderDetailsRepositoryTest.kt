package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.varCharValueColumn
import java.util.*

class OrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderDetailsRepository

  fun orderDetailsData(subjectId: String, orderId: String, offenceRisk: Boolean) = """
    {
      "Data": [
        ${varCharValueColumn(subjectId)},
        ${varCharValueColumn(orderId)},
        ${varCharValueColumn(offenceRisk.toString())},
      ]
    }
  """.trimIndent()

  fun orderDetailsResultSet(
    subjectId: String? = "1234",
    orderId: String? = "fakeOrderId",
    offenceRisk: Boolean = true,
  ) = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("legacy_subject_id")},
            ]
          },
          ${orderDetailsData(subjectId!!, orderId!!, offenceRisk)},
          ${orderDetailsData("5678", "This row should never be returned", false)}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            ${metaDataRow("legacy_subject_id")},
            ${metaDataRow("legacy_order_id")},
            ${metaDataRow("offence_risk", "boolean")}
          ]
        }
      },
      "UpdateCount": 0
    }
  """.trimIndent()

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = OrderDetailsRepository(emDatastoreClient)
  }

  @Test
  fun `OrderDetailsRepository can be instantiated`() {
    val sut = OrderRepository(Mockito.mock(EmDatastoreClient::class.java))
    Assertions.assertThat(sut).isNotNull()
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getOrderDetails("123", AthenaRole.DEV)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getOrderDetails returns an AthenaOrderDetailsDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getOrderDetails("123", AthenaRole.DEV)

      Assertions.assertThat(result).isInstanceOf(AthenaOrderDetailsDTO::class.java)
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val orderId = "expectedId"
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet(orderId))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getOrderDetails(orderId, AthenaRole.DEV)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo(orderId)
    }
  }
}
