package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
class SearchController(
  @Autowired val orderService: OrderService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Integrity orders"],
    summary = "Start a search for an Integrity Order using a set of search criteria",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(schema = Schema(implementation = QueryExecutionResponse::class))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "You are not authorized to view the resource",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Accessing the resource you were trying to reach is forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
    security = [
      SecurityRequirement(
        name = "hmpps-auth-token",
        scopes = ["ROLE_EM_DATASTORE_GENERAL_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/search/orders",
      "/integrity/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
  fun searchGeneralOrders(
    authentication: Authentication,
    @Parameter(description = "The search criteria for the query", required = true)
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> = startSearch(orderSearchCriteria, authentication.name)

  @Operation(
    tags = ["Integrity orders including restricted cases"],
    summary = "Start a search for an Integrity Order using a set of search criteria and include restricted cases",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(schema = Schema(implementation = QueryExecutionResponse::class))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "You are not authorized to view the resource",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Accessing the resource you were trying to reach is forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
    security = [
      SecurityRequirement(
        name = "hmpps-auth-token",
        scopes = ["ROLE_EM_DATASTORE_RESTRICTED_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/integrity/restricted/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun searchRestrictedOrders(
    authentication: Authentication,
    @Parameter(description = "The search criteria for the query", required = true)
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> = startSearch(orderSearchCriteria, authentication.name, true)

  @Operation(
    tags = ["Integrity orders"],
    summary = "Retrieve the Integrity Order search results for a set of search criteria",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(array = ArraySchema(schema = Schema(implementation = OrderSearchResult::class)))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "You are not authorized to view the resource",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Accessing the resource you were trying to reach is forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
    security = [
      SecurityRequirement(
        name = "hmpps-auth-token",
        scopes = ["ROLE_EM_DATASTORE_GENERAL_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/search/orders/{queryExecutionId}",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
  fun getSearchOrderResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> = startSearch(queryExecutionId, authentication.name)

  @Operation(
    tags = ["Integrity orders"],
    summary = "Retrieve the Integrity Order search results for a set of search criteria",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(array = ArraySchema(schema = Schema(implementation = OrderSearchResult::class)))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "You are not authorized to view the resource",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Accessing the resource you were trying to reach is forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
    security = [
      SecurityRequirement(
        name = "hmpps-auth-token",
        scopes = ["ROLE_EM_DATASTORE_GENERAL_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/integrity/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
  fun startSearch(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @RequestParam(name = "id", required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> = startSearch(queryExecutionId, authentication.name)

  @Operation(
    tags = ["Integrity orders including restricted cases"],
    summary = "Retrieve the Integrity Order search results for a set of search criteria that include restricted cases",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(array = ArraySchema(schema = Schema(implementation = OrderSearchResult::class)))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "You are not authorized to view the resource",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Accessing the resource you were trying to reach is forbidden",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
    security = [
      SecurityRequirement(
        name = "hmpps-auth-token",
        scopes = ["ROLE_EM_DATASTORE_RESTRICTED_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/integrity/restricted/orders",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSpecialOrderSearchResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @RequestParam(name = "id", required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> = startSearch(queryExecutionId, authentication.name, true)

  private fun startSearch(orderSearchCriteria: OrderSearchCriteria, username: String, allowSpecials: Boolean = false): ResponseEntity<QueryExecutionResponse> {
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

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  private fun startSearch(queryExecutionId: String, username: String, allowSpecials: Boolean = false): ResponseEntity<List<OrderSearchResult>> {
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

    return ResponseEntity.ok(results)
  }
}
