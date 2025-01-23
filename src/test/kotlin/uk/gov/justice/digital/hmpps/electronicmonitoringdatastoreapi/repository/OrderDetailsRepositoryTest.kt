package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.metaDataRow
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.varCharValueColumn
import java.util.*

class OrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: OrderDetailsRepository

  fun orderDetailsData(orderId: String) = """
    {
      "Data": [
        ${varCharValueColumn(orderId)},
      ]
    }
  """.trimIndent()

  fun orderDetailsResultSet(orderId: String? = "1234") = """
    {
      "ResultSet": {
        "Rows": [
          {
            "Data": [
              ${varCharValueColumn("legacy_subject_id")},
            ]
          },
          ${orderDetailsData(orderId!!)},
          ${orderDetailsData("5678")}
        ],
        "ResultSetMetadata": {
          "ColumnInfo": [
            """ + metaDataRow("legacy_subject_id") + """,
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

//  @Nested
//  inner class GetOrderDetails {
//    @Test
//    fun `getOrderDetails calls getQueryResult`() {
//      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet())
//
//      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)
//
//      repository.getOrderDetails("123", AthenaRole.DEV)
//
//      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
//    }
//
//    @Test
//    fun `getOrderDetails returns an AthenaOrderDetailsDTO`() {
//      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet())
//
//      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)
//
//      val result = repository.getOrderDetails("123", AthenaRole.DEV)
//
//      Assertions.assertThat(result).isInstanceOf(AthenaOrderDetailsDTO::class.java)
//    }
//
//    @Test
//    fun `getOrderDetails returns the first result from getQueryResult`() {
//      val orderId = "expectedId"
//      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet(orderId))
//
//      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)
//
//      val result = repository.getOrderDetails(orderId, AthenaRole.DEV)
//
//      Assertions.assertThat(result).isNotNull
//      Assertions.assertThat(result.legacySubjectId).isEqualTo(orderId)
//    }
//  }
}
