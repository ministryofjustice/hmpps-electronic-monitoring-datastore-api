package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ConnectionStatus
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
class ConnectivityController(
  @Autowired val orderService: OrderService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Connectivity"],
    summary = "Confirm that a connection can be established with Athena",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = [Content(schema = Schema(implementation = ConnectionStatus::class))],
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
        scopes = ["ROLE_EM_DATASTORE_GENERAL_RO", "ROLE_EM_DATASTORE_RESTRICTED_RO"],
      ),
    ],
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/search/confirmConnection",
      "/test",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun test(authentication: Authentication): ResponseEntity<Map<String, String>> {
    auditService.createEvent(
      authentication.principal.toString(),
      "CONFIRM_CONNECTION",
      mapOf("confirmConnection" to "true"),
    )

    var message = "API Connection successful"
    try {
      if (!orderService.checkAvailability()) {
        message = "API Connection successful, but no access to Athena"
      }
    } catch (_: Exception) {
      message = "Error determining Athena access"
    }

    return ResponseEntity(
      mapOf("message" to message),
      HttpStatus.OK,
    )
  }
}
