package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO', 'ROLE_ELECTRONIC_MONITORING_DATASTORE_API_SEARCH')")
@RequestMapping(value = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
class SearchController(
  @Autowired val auditService: AuditService,
) {

  @GetMapping("/confirmConnection")
  fun confirmConnection(
    authentication: Authentication,
  ): ResponseEntity<Map<String, String>> {
    // TODO: Re-enable audit once Cloud Platform in place
//       auditService.createEvent(
//         authentication.principal.toString(),
//         "CONFIRM_CONNECTION",
//         mapOf("confirmConnection" to "true"),
//       )

    return ResponseEntity(
      mapOf("message" to "Connection successful"),
      HttpStatus.OK,
    )
  }

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
  fun confirmAthenaAccess(
    authentication: Authentication,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<ResultSet> {
    val queryString: String = """
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
    val validatedRole: AthenaRole = AthenaRole.Companion.fromString(unvalidatedRole) ?: AthenaRole.DEV

    val athenaService = AthenaService()
    val resultSet: ResultSet = athenaService.getQueryResult(validatedRole, queryString)

    // TODO: Re-enable audit once Cloud Platform in place
    // auditService.createEvent(
    //   authentication.principal.toString(),
    //   "SEARCH_TEST",
    //   mapOf("queryString" to queryString),
    // )

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
    authentication: Authentication,
    @RequestBody(required = true) athenaQuery: AthenaQuery,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
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

    // TODO: Re-enable audit once Cloud Platform in place
    // auditService.createEvent(
    //   authentication.principal.toString(),
    //   "SEARCH_WITH_CUSTOM_QUERY",
    //   mapOf("queryString" to queryString),
    // )

    return AthenaQueryResponse<String>(
      queryString = queryString,
      athenaRole = validatedRole.name,
      isErrored = false,
      queryResponse = result,
    )
  }

  @PostMapping("/orders-old")
  fun searchOrdersFake(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): List<OrderSearchResult> {
    // TODO: Re-enable audit once Cloud Platform in place
    // auditService.createEvent(
    //   authentication.principal.toString(),
    //   "SEARCH_OLD_ORDERS",
    //   mapOf("legacySubjectId" to orderSearchCriteria.legacySubjectId, "searchType" to orderSearchCriteria.searchType),
    // )

    return OrderRepository.Companion.getFakeOrders()
  }

  @PostMapping("/orders")
  fun searchOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<List<OrderSearchResult>> {
    val repository = OrderRepository()

    // 2: query repository
    val result: AthenaQueryResponse<List<OrderSearchResult>> = repository.getOrders(orderSearchCriteria)

    // TODO: Re-enable audit once Cloud Platform in place
    // auditService.createEvent(
    //   authentication.principal.toString(),
    //   "SEARCH_ORDERS",
    //   mapOf("legacySubjectId" to orderSearchCriteria.legacySubjectId, "searchType" to orderSearchCriteria.searchType),
    // )

    return ResponseEntity<List<OrderSearchResult>>(
      result.queryResponse,
      HttpStatus.OK,
    )
  }
}
