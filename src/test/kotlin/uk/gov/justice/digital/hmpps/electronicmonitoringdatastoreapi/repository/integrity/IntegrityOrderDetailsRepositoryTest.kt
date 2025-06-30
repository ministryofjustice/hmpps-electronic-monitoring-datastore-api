package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.SqlQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.testutils.MockAthenaResultSetBuilder

class IntegrityOrderDetailsRepositoryTest {
  private lateinit var emDatastoreClient: EmDatastoreClient
  private lateinit var repository: IntegrityOrderDetailsRepository

  fun orderDetailsResultSet(
    subjectId: String,
    orderId: String,
    offenceRisk: Boolean,
  ) = MockAthenaResultSetBuilder(
    columns = arrayOf(
      "legacy_subject_id",
      "legacy_order_id",
      "offence_risk", // boolean
    ),
    rows = arrayOf(
      arrayOf(subjectId, orderId, offenceRisk.toString()),
      arrayOf("5678", "This row should never be returned", "false"),
    ),
  ).build()

  @BeforeEach
  fun setup() {
    emDatastoreClient = Mockito.mock(EmDatastoreClient::class.java)
    repository = IntegrityOrderDetailsRepository(emDatastoreClient)
  }

  @Nested
  inner class GetOrderDetails {
    @Test
    fun `getOrderDetails calls getQueryResult`() {
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet("123564", "768324", true))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      repository.getOrderDetails("123", false)

      Mockito.verify(emDatastoreClient).getQueryResult(any<SqlQueryBuilder>(), eq(false))
    }

    @Test
    fun `getOrderDetails returns the first result from getQueryResult`() {
      val legacySubjectId = "expectedId"
      val resultSet = AthenaHelper.resultSetFromJson(orderDetailsResultSet(legacySubjectId, "123564", false))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<SqlQueryBuilder>(), eq(false))).thenReturn(resultSet)

      val result = repository.getOrderDetails(legacySubjectId, false)

      Assertions.assertThat(result.legacySubjectId).isEqualTo(legacySubjectId)
    }
  }
}
