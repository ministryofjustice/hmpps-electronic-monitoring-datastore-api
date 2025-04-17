package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
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

  @Operation(
    tags = ["Searching orders"],
    summary = "Execute a search for integrity orders",
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun executeSearch(
    authentication: Authentication,
    @Parameter(description = "The search criteria for the query", required = true)
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
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @Operation(
    tags = ["Searching orders"],
    summary = "Get the search results for a previous job execution",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSearchResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @RequestParam(name = "id", required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val results = orderService.getSearchResults(queryExecutionId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "RETRIEVE_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }
}
