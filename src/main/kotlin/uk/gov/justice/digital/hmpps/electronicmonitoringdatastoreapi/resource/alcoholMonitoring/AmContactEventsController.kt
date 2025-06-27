package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TAG_ALCOHOL_ORDERS
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TOKEN_HMPPS_AUTH
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@Tag(name = TAG_ALCOHOL_ORDERS)
@RestController
class AmContactEventsController(
  @param:Autowired private val amOrderEventsService: AmOrderEventsService,
  @param:Autowired private val auditService: AuditService,
) {

  @Operation(
    summary = "Get the contact events for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/alcohol-monitoring/{legacySubjectId}/contact-events",
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
  @PreAuthorize("hasAnyAuthority('$ROLE_EM_DATASTORE_GENERAL__RO', '$ROLE_EM_DATASTORE_RESTRICTED__RO')")
  fun getContactEvents(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "Input contains illegal characters - legacy subject ID may only contain letters and numbers",
    )
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<AmEvent<AmContactEventDetails>>> {
    val result = amOrderEventsService.getContactEvents(legacySubjectId, AthenaRole.ROLE_EM_DATASTORE_GENERAL__RO)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_CONTACT_EVENTS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to false,
      ),
    )

    return ResponseEntity.ok(result)
  }
}
