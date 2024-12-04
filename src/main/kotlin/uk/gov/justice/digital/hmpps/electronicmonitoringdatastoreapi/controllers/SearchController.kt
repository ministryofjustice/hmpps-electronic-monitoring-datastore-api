package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/search"], produces = ["application/json"])
class SearchController(
  @Autowired val auditService: AuditService,
) {

//  @GetMapping("/cases/{caseID}")
//  fun getCases(
//    @PathVariable(
//      required = true,
//      name = "caseID",
//    ) caseId: String,
//  ): JSONObject {
//    val response: JSONObject = JSONObject()
//    response.put("data", "You have successfully queried case $caseId")
//
//    return response
//  }

  @GetMapping("/testEndpoint")
  fun confirmAthenaAccess(): ResponseEntity<ResultSet> {
    val athenaService = AthenaService()
    val testQuery: String = """
        SELECT 
          legacy_subject_id, 
          full_name, 
          primary_address_line_1, 
          primary_address_line_2, 
          primary_address_line_3, 
          primary_address_post_code, 
          order_start_date, 
          order_end_date 
        FROM 
          test_database.order_details
        WHERE 
    """.trimIndent()
    val resultSet: ResultSet = athenaService.getQueryResult(AthenaRole.DEV, testQuery)

    return ResponseEntity<ResultSet>(
      resultSet,
      HttpStatus.OK,
    )

//    val stringResult: String = resultSet.toString()
//    val response: JSONObject = JSONObject(resultSet)
//
//    return response
  }

  @PostMapping("/custom-query")
  fun queryAthena(
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
    @RequestBody(
      required = true,
    ) athenaQuery: AthenaQuery,
  ): AthenaQueryResponse<String> {
    val queryString: String = athenaQuery.queryString
    val validatedRole: AthenaRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV
    val result: String

    try {
      val athenaService = AthenaService()
      val resultSet: ResultSet = athenaService.getQueryResult(validatedRole, queryString)
      result = resultSet.toString()
    } catch (e: Exception) {
      return AthenaQueryResponse(
        queryString = queryString,
        athenaRole = validatedRole.name,
        isErrored = true,
        errorMessage = e.message.toString(),
      )
    }

    auditService.createEvent(
      "SEARCH_WITH_CUSTOM_QUERY",
      mapOf("queryString" to athenaQuery.queryString),
    )

    return AthenaQueryResponse<String>(
      queryString = queryString,
      athenaRole = validatedRole.name,
      isErrored = false,
      queryResponse = result,
    )
  }

  @PostMapping("/orders-old")
  fun searchOrdersFake(
    @RequestHeader("Authorization", required = true) authorization: String,
    @RequestBody searchCriteria: SearchCriteria,
  ): List<Order> = OrderRepository.getFakeOrders()

  @PostMapping("/orders")
  fun searchOrders(
    @RequestHeader("Authorization", required = true) authorization: String,
    @RequestBody searchCriteria: SearchCriteria,
  ): ResponseEntity<List<Order>> {
    val repository = OrderRepository()

    // 2: query repository
    val result: AthenaQueryResponse<List<Order>> = repository.getOrders(searchCriteria)

    auditService.createEvent(
      "SEARCH_ORDERS",
      mapOf("legacySubjectId" to searchCriteria.legacySubjectId),
    )

    return ResponseEntity<List<Order>>(
      result.queryResponse,
      HttpStatus.OK,
    )
  }
}
