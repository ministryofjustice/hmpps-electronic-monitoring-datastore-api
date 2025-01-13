package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderSearchResultDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole

@Service
@Profile("test")
class MockOrderRepository : OrderRepositoryInterface {
  override fun searchOrders(criteria: OrderSearchCriteria, role: AthenaRole): List<AthenaOrderSearchResultDTO> = listOf(
    AthenaOrderSearchResultDTO(
      legacySubjectId = 1000000,
      fullName = "Amy Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "08-02-2019",
      orderEndDate = "08-02-2020",
    ),
    AthenaOrderSearchResultDTO(
      legacySubjectId = 2000000,
      fullName = "Bill Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "03-11-2020",
      orderEndDate = "03-11-2021",
    ),
    AthenaOrderSearchResultDTO(
      legacySubjectId = 3000000,
      fullName = "Claire Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "05-08-2001",
      orderEndDate = "05-08-2002",
    ),
    AthenaOrderSearchResultDTO(
      legacySubjectId = 8000000,
      fullName = "Daniel Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "18-02-2012",
      orderEndDate = "18-02-2014",
    ),
    AthenaOrderSearchResultDTO(
      legacySubjectId = 30000,
      fullName = "Emma Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "24-01-2017",
      orderEndDate = "24-01-2020",
    ),
    AthenaOrderSearchResultDTO(
      legacySubjectId = 4000000,
      fullName = "Fred Smith",
      primaryAddressLine1 = "First line of address",
      primaryAddressLine2 = "",
      primaryAddressLine3 = "",
      primaryAddressPostCode = "",
      orderStartDate = "01-05-2021",
      orderEndDate = "01-05-2022",
    ),
  )

  override fun listLegacyIds(role: AthenaRole): List<String> = listOf("1234567")

  override fun runQuery(athenaQuery: AthenaQuery<*>, role: AthenaRole): String {
    if (athenaQuery.queryString == "THROW ERROR") {
      throw IllegalArgumentException(athenaQuery.queryString)
    }

    return """
      result: 
        ${athenaQuery.queryString} 
    """.trimIndent()
  }
}
