package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@RequestMapping(value = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
class SearchController(
  @Autowired val orderService: OrderService,
  @Autowired val auditService: AuditService,
) {
  @GetMapping("/confirmConnection")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun confirmConnection(
    authentication: Authentication,
  ): ResponseEntity<Map<String, String>> {
    auditService.createEvent(
      authentication.name,
      "CONFIRM_CONNECTION",
      mapOf("confirmConnection" to "true"),
    )

    var athenaAccess = orderService.checkAvailability()
    val message = if (athenaAccess) {
      "Connection successful"
    } else {
      "API Connection successful, but no access to Athena"
    }

    return ResponseEntity(
      mapOf("message" to message),
      HttpStatus.OK,
    )
  }

  @PostMapping("/orders")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
  fun searchOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> {
    val queryExecutionId = startSearch(orderSearchCriteria, authentication.name, false)
    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @PostMapping("/orders/specials")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun searchSpecialsOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> {
    val queryExecutionId = startSearch(orderSearchCriteria, authentication.name, true)
    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  private fun startSearch(orderSearchCriteria: OrderSearchCriteria, username: String, allowSpecials: Boolean = false): String {
    val queryExecutionId = orderService.getQueryExecutionId(orderSearchCriteria, allowSpecials)

    auditService.createEvent(
      username,
      "SEARCH_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "includesSpecialOrders" to "$allowSpecials",
        "queryExecutionId" to queryExecutionId,
      ),
    )

    return queryExecutionId
  }

  @GetMapping("/orders/{queryExecutionId}")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
  fun getOrdersSearchResults(
    authentication: Authentication,
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> {
    val results = getSearchResults(queryExecutionId, authentication.name, false)
    return ResponseEntity.ok(results)
  }

  @GetMapping("/orders/specials/{queryExecutionId}")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSpecialOrdersSearchResults(
    authentication: Authentication,
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> {
    val results = getSearchResults(queryExecutionId, authentication.name, true)
    return ResponseEntity.ok(results)
  }

  private fun getSearchResults(queryExecutionId: String, username: String, allowSpecials: Boolean = false): List<OrderSearchResult> {
    val results = orderService.getSearchResults(queryExecutionId, allowSpecials)

    auditService.createEvent(
      username,
      "RETRIEVE_ORDERS_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "includesSpecialOrders" to "$allowSpecials",
        "rows" to results.count().toString(),
      ),
    )

    return results
  }
}
