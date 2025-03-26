package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class SearchController(
  @Autowired val orderService: OrderService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @PostMapping("/search/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun searchOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val queryExecutionId = orderService.getQueryExecutionId(orderSearchCriteria, validatedRole)

    auditService.createEvent(
      authentication.name,
      "SEARCH_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "queryExecutionId" to queryExecutionId,
      ),
    )

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @GetMapping("/search/orders/{queryExecutionId}", produces = [MediaType.APPLICATION_JSON_VALUE])
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSearchResults(
    authentication: Authentication,
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val results = orderService.getSearchResults(queryExecutionId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "RETRIEVE_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }
}