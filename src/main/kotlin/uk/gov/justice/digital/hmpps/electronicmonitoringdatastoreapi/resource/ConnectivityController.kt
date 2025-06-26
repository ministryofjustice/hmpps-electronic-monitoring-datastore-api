package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_GENERAL__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.ROLE_EM_DATASTORE_RESTRICTED__RO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.TAG_CONNECTIVITY
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.IntegrityOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@Tag(name = TAG_CONNECTIVITY)
@RestController
class ConnectivityController(
  @field:Autowired val integrityOrderService: IntegrityOrderService,
  val athenaRoleService: AthenaRoleService,
  @field:Autowired val auditService: AuditService,
) {

  @Operation(
    summary = "Test the connectivity with athena",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/test",
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
  // @SecurityRequirement(name = TOKEN_HMPPS_AUTH, scopes = [ROLE_EM_DATASTORE_GENERAL__RO, ROLE_EM_DATASTORE_RESTRICTED__RO])
  @PreAuthorize("hasAnyAuthority('$ROLE_EM_DATASTORE_GENERAL__RO', '$ROLE_EM_DATASTORE_RESTRICTED__RO')")
  fun test(authentication: Authentication): ResponseEntity<Map<String, String>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    var message = "API Connection successful"
    try {
      if (!integrityOrderService.checkAvailability(validatedRole)) {
        message = "API Connection successful, but no access to Athena"
      }
    } catch (_: Exception) {
      message = "Error determining Athena access"
    }
    val result = mapOf("message" to message)

    auditService.createEvent(
      authentication.principal.toString(),
      "CONFIRM_CONNECTION",
      mapOf("confirmConnection" to "true"),
    )

    return ResponseEntity.ok(result)
  }
}
