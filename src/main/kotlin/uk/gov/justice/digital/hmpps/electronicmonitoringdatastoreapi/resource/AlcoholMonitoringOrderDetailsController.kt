package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.links.Link
import io.swagger.v3.oas.annotations.links.LinkParameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TAG_ALCOHOL_ORDERS
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TOKEN_HMPPS_AUTH
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AlcoholMonitoringOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AlcoholMonitoringOrderDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@Tag(name = TAG_ALCOHOL_ORDERS)
@RestController
class AlcoholMonitoringOrderDetailsController(
  @param:Autowired private val alcoholMonitoringOrderDetailsService: AlcoholMonitoringOrderDetailsService,
  @param:Autowired private val auditService: AuditService,
) {

  @Operation(
    summary = "Execute a search for integrity orders",
    operationId = "executeAlcoholMonitoringOrderSearch",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request - invalid input data.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized - requires a valid OAuth2 token",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden - requires an appropriate role",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "500",
        description = "Internal Server Error - An unexpected error occurred.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.POST],
    path = [
      "/orders/alcohol-monitoring",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @SecurityRequirement(name = TOKEN_HMPPS_AUTH, scopes = [ROLE_EM_DATASTORE_GENERAL__RO, ROLE_EM_DATASTORE_RESTRICTED__RO])
  @PreAuthorize("hasAnyAuthority('$ROLE_EM_DATASTORE_GENERAL__RO', '$ROLE_EM_DATASTORE_RESTRICTED__RO')")
  fun executeSearch(
    authentication: Authentication,
    @Parameter(description = "The search criteria for the query", required = true)
    @RequestBody
    @Valid orderSearchCriteria: OrderSearchCriteria,
    @Parameter(description = "A flag to indicate whether to include restricted orders in the resultset")
    restricted: Boolean = false,
  ): ResponseEntity<QueryExecutionResponse> {
    val queryExecutionId = alcoholMonitoringOrderDetailsService.getQueryExecutionId(orderSearchCriteria)

    auditService.createEvent(
      authentication.name,
      "SEARCH_ALCOHOL_MONITORING_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "queryExecutionId" to queryExecutionId,
        "restrictedOrdersIncluded" to restricted,
      ),
    )

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @Operation(
    summary = "Get the search results for a previous job execution",
    operationId = "listAlcoholMonitoringOrderSearchResults",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/alcohol-monitoring",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @ApiResponses(
    value = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request - invalid input data.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized - requires a valid OAuth2 token",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden - requires an appropriate role",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "500",
        description = "Internal Server Error - An unexpected error occurred.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @SecurityRequirement(name = TOKEN_HMPPS_AUTH, scopes = [ROLE_EM_DATASTORE_GENERAL__RO, ROLE_EM_DATASTORE_RESTRICTED__RO])
  @PreAuthorize("( hasAuthority('$ROLE_EM_DATASTORE_GENERAL__RO') and #restricted == false ) or ( hasAuthority('$ROLE_EM_DATASTORE_RESTRICTED__RO') )")
  fun getSearchResults(
    authentication: Authentication,
    @Parameter(description = "The query execution ID of the search job", required = true)
    @RequestParam(name = "id", required = true) queryExecutionId: String,
    @Parameter(description = "A flag to indicate whether to include restricted orders in the resultset")
    restricted: Boolean = false,
  ): ResponseEntity<List<AlcoholMonitoringOrderDetails>> {
    val results = alcoholMonitoringOrderDetailsService.getSearchResults(queryExecutionId)

    auditService.createEvent(
      authentication.name,
      "RETRIEVE_ALCOHOL_MONITORING_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "restrictedOrdersIncluded" to restricted,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }

  @Operation(
    summary = "Get the details for an order",
    operationId = "getAlcoholMonitoringOrderDetails",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "OK",
        links = [
          Link(
            name = "Get contact events",
            operationId = "getAlcoholMonitoringContactEvents",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
          Link(
            name = "Get equipment details",
            operationId = "getAlcoholMonitoringEquipmentDetails",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
          Link(
            name = "Get incident events",
            operationId = "getAlcoholMonitoringIncidentEvents",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
          Link(
            name = "Get service details",
            operationId = "getAlcoholMonitoringServiceDetails",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
          Link(
            name = "Get violation events",
            operationId = "getAlcoholMonitoringViolationEvents",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
          Link(
            name = "Get visit details",
            operationId = "getAlcoholMonitoringVisitDetails",
            parameters = [
              LinkParameter(name = "legacySubjectId", expression = $$"$response.body#/legacySubjectId"),
            ],
          ),
        ],
      ),
      ApiResponse(
        responseCode = "400",
        description = "Bad request - invalid input data.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized - requires a valid OAuth2 token",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden - requires an appropriate role",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "500",
        description = "Internal Server Error - An unexpected error occurred.",
        content = [Content(schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/alcohol-monitoring/{legacySubjectId}",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @SecurityRequirement(name = TOKEN_HMPPS_AUTH, scopes = [ROLE_EM_DATASTORE_GENERAL__RO, ROLE_EM_DATASTORE_RESTRICTED__RO])
  @PreAuthorize("hasAnyAuthority('$ROLE_EM_DATASTORE_GENERAL__RO', '$ROLE_EM_DATASTORE_RESTRICTED__RO')")
  fun getOrderDetails(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "Input contains illegal characters - legacy subject ID may only contain letters and numbers",
    )
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<AlcoholMonitoringOrderDetails> {
    val result = alcoholMonitoringOrderDetailsService.getOrderDetails(legacySubjectId)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_ORDER_DETAILS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to false,
      ),
    )

    return ResponseEntity.ok(result)
  }
}
