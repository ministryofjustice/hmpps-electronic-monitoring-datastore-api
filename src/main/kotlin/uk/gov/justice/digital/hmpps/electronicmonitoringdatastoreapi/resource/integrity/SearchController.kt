package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class SearchController(
  @Autowired val orderService: OrderService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Integrity orders"],
    summary = "Get the monitoring events for an order",
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/search/orders",
      "/integrity/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun searchOrders(
    authentication: Authentication,
    @Parameter(description = "The search criteria for the query", required = true)
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> {
    val queryExecutionId = orderService.getQueryExecutionId(orderSearchCriteria)

    auditService.createEvent(
      authentication.name,
      "SEARCH_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "queryExecutionId" to queryExecutionId,
        "restrictedOrdersIncluded" to false,
      ),
    )

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @Operation(
    tags = ["Integrity orders"],
    summary = "Get the search results for a previous job execution",
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/search/orders/{queryExecutionId}",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSearchOrderResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> = retrieveSearchResults(authentication, queryExecutionId)

  @Operation(
    tags = ["Integrity orders"],
    summary = "Get the search results for a previous job execution",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/integrity/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSearchResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @RequestParam(name = "id", required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> = retrieveSearchResults(authentication, queryExecutionId)

  private fun retrieveSearchResults(authentication: Authentication, queryExecutionId: String): ResponseEntity<List<OrderSearchResult>> {
    val results = orderService.getSearchResults(queryExecutionId)

    auditService.createEvent(
      authentication.name,
      "RETRIEVE_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "restrictedOrdersIncluded" to false,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }
}
