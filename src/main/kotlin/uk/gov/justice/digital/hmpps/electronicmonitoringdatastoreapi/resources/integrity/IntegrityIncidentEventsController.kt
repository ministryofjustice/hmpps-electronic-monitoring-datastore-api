package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resources.integrity

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Pattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TAG_INTEGRITY_ORDERS
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TOKEN_HMPPS_AUTH
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IncidentEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity.IntegrityOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@Tag(name = TAG_INTEGRITY_ORDERS)
@RestController
class IntegrityIncidentEventsController(
  @field:Autowired val integrityOrderEventsService: IntegrityOrderEventsService,
  @field:Autowired val auditService: AuditService,
) {
  @Operation(
    summary = "Get the incident events for an integrity order",
    operationId = "getIntegrityIncidentEvents",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/integrity/{legacySubjectId}/incident-events",
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
  fun getIncidentEvents(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the integrity order", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - legacy subject ID must be a number")
    @PathVariable legacySubjectId: String,
    @Parameter(description = "A flag to indicate whether to include restricted orders in the resultset")
    restricted: Boolean = false,
  ): ResponseEntity<List<Event<IncidentEventDetails>>> {
    val result = integrityOrderEventsService.getIncidentEvents(legacySubjectId, restricted)

    auditService.createEvent(
      authentication.name,
      "GET_INTEGRITY_INCIDENT_EVENTS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to restricted,
      ),
    )

    return ResponseEntity.ok(result)
  }
}
