package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.test.context.ActiveProfiles
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Document
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.DocumentList
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

@ActiveProfiles("test")
class OrderInformationRepositoryTest {
  private lateinit var repository: OrderInformationRepository
  private lateinit var athenaService: AthenaService
  private lateinit var athenaHelper: AthenaHelper

  @BeforeEach
  fun setup() {
    athenaService = mock()
    athenaHelper = mock()
    repository = OrderInformationRepository(athenaService, athenaHelper)
  }

  @Nested
  inner class GetKeyOrderQuery

  @Nested
  inner class ParseKeyOrdeResponse

  @Nested
  inner class GetKeyOrderInformation {

//    @Test
//    fun `calls athena and returns data`() {
//      val orderId = "test-id"
//
//      val expected = AthenaQueryResponse<KeyOrderInformation>(
//        queryString = "test-id",
//        athenaRole = "DEV",
//        queryResponse = KeyOrderInformation(
//          specials = "fake",
//          legacySubjectId = "fake",
//          legacyOrderId = "fake",
//          name = "fake",
//          alias = "fake-and-nullable",
//          dateOfBirth = "fake",
//          address1 = "fake",
//          address2 = "fake",
//          address3 = "fake",
//          postcode = "fake",
//          orderStartDate = "fake",
//          orderEndDate = "fake",
//        ),
//      )
//
//      val resultSet: ResultSet = AthenaHelper.Companion.resultSetFromJson(
//        AthenaHelperTest().defaultResultSet,
//      )
//
//      `when`(athenaService.getQueryResult(any<AthenaRole>(), any<String>()))
//        .thenReturn(resultSet)
//
//      // TODO: fix errors caused by AthenaHelper parsing
//      // My AthenaHelper isn't parsing the fake ResultSet as the object I want.
//      // This is designed to mock that return, but it's not quite working yet.
//      `when`(athenaHelper.mapTo<AthenaKeyOrderDTO>(resultSet))
//        .thenReturn(listOf(AthenaKeyOrderDTO(legacySubjectId = "fake",
//          legacyOrderId = "fake",
//          name = "fake",
//          alias = "fake-and-nullable",
//          dateOfBirth = "fake",
//          address1 = "fake",
//          address2 = "fake",
//          address3 = "fake",
//          postcode = "fake",
//          orderStartDate = "fake",
//          orderEndDate = "fake",
//        )))
//
//      val result = repository.getKeyOrderInformation(orderId)
//
//
// //      Assertions.assertThat(result.queryString).isEqualTo(expected.queryString)
// //      Assertions.assertThat(result.athenaRole).isEqualTo(expected.athenaRole)
// //      Assertions.assertThat(result.queryResponse).isEqualTo(expected.queryResponse)
// //      Assertions.assertThat(result).isEqualTo(expected)
//      Assertions.assertThat(true).isEqualTo(true)
//    }
  }

  @Nested
  inner class GetMockOrderInformation {
    @Test
    fun `Returns mock order information`() {
      val orderId = "I am the test legacy subject Id"

      val expected: OrderInformation = OrderInformation(
        keyOrderInformation = KeyOrderInformation(
          specials = "No",
          legacySubjectId = "1234567",
          legacyOrderId = orderId,
          name = "John Smith",
          alias = "Zeno",
          dateOfBirth = "01-02-1980",
          address1 = "1 Primary Street",
          address2 = "Sutton",
          address3 = "London",
          postcode = "ABC 123",
          orderStartDate = "01-02-2012",
          orderEndDate = "03-04-2013",
        ),
        subjectHistoryReport = SubjectHistoryReport(
          reportUrl = "#",
          name = "1234567",
          createdOn = "01-02-2020",
          time = "0900",
        ),
        documents = DocumentList(
          pageSize = 14,
          orderDocuments = listOf(
            Document(name = "Document 1", url = "#", createdOn = "01-02-2020", time = "0100", notes = "Order 1 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx"),
            Document(name = "Document 2", url = "#", createdOn = "21-09-2017", time = "0200", notes = "Order 2 documents xxxxxx xxxxxxx NAME HDC New.msg application/octet-stream NAME - TOLD TO IGNORE AS SUBJECT STILL IN CUSTODY ABCD 12 345678 xx-xxxxxx"),
            // There are more documents in here but there is no real utility in testing they are returned
          ),
        ),
      )

      val result: OrderInformation = repository.getMockOrderInformation(orderId)

      Assertions.assertThat(result.keyOrderInformation).isEqualTo(expected.keyOrderInformation)
      Assertions.assertThat(result.subjectHistoryReport).isEqualTo(expected.subjectHistoryReport)
      Assertions.assertThat(result.documents.orderDocuments[1]).isEqualTo(expected.documents.orderDocuments[1])
    }

    // Test added to confirm solution of a bug in OrderInformationRepostory.getMockOrderInformation
    // Bug resulted in appending legacySubjectId in the mock data rather than replacing it, and using the same object reference for all occurrences
    @Test
    fun `Returns correct order summary when called multiple times`() {
      val orderId1 = "I am test data"
      val result1 = repository.getMockOrderInformation(orderId1)

      Assertions.assertThat(result1.keyOrderInformation.legacyOrderId).isEqualTo(orderId1)

      val orderId2 = "I am different test data"
      val result2 = repository.getMockOrderInformation(orderId2)

      Assertions.assertThat(result1.keyOrderInformation.legacyOrderId).isEqualTo(orderId1)
      Assertions.assertThat(result2.keyOrderInformation.legacyOrderId).isEqualTo(orderId2)
    }
  }
}
