package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class ConnectivityController(
  @Autowired val orderService: OrderService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Connectivity"],
    summary = "Test the connectivity with athena",
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
