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

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      repository.getAmOrderDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Mockito.verify(emDatastoreClient).getQueryResult(any<AthenaQuery>(), any<AthenaRole>())
    }

    @Test
    fun `getAmOrderDetails returns an AthenaAmOrderDetailsDTO`() {
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet())

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getAmOrderDetails("123", AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isInstanceOf(AthenaAmOrderDetailsDTO::class.java)
    }

    @Test
    fun `getAmOrderDetails returns the first result from getQueryResult`() {
      val orderId = "expectedId"
      val resultSet = AthenaHelper.resultSetFromJson(amOrderDetailsResultSet(orderId))

      Mockito.`when`(emDatastoreClient.getQueryResult(any<AthenaQuery>(), any<AthenaRole>())).thenReturn(resultSet)

      val result = repository.getAmOrderDetails(orderId, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)

      Assertions.assertThat(result).isNotNull
      Assertions.assertThat(result.legacySubjectId).isEqualTo(orderId)
    }

    @Test
    fun `Throws an exception if orderId is not alphanumeric`() {
      val dangerousInput = "123 OR 1=1"

      Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java).isThrownBy {
        repository.getAmOrderDetails(dangerousInput, AthenaRole.ROLE_EM_DATASTORE_GENERAL_RO)
      }.withMessage("Input contains illegal characters")
    }
  }
}
