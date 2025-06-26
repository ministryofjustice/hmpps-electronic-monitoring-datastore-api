package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TAG_INTEGRITY_GENERAL_ORDERS
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TOKEN_HMPPS_AUTH
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegrityVisitDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
class VisitDetailsController(
  @field:Autowired val integrityVisitDetailsService: IntegrityVisitDetailsService,
  val athenaRoleService: AthenaRoleService,
  @field:Autowired val auditService: AuditService,
) {
  @Operation(
    tags = [TAG_INTEGRITY_GENERAL_ORDERS],
    summary = "Get the visit details for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/integrity/{legacySubjectId}/visit-details",
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
  fun getVisitDetails(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - legacy subject ID must be a number")
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<IntegrityVisitDetails>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = integrityVisitDetailsService.getVisitDetails(legacySubjectId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_VISIT_DETAILS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(result)
  }
}
