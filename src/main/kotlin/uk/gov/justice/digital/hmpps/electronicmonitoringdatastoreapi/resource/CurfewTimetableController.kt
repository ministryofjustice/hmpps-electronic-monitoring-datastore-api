package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import jakarta.validation.constraints.Pattern
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CurfewTimetable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.CurfewTimetableService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/orders"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CurfewTimetableController(
  @Autowired val curfewTimetableService: CurfewTimetableService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {
  @GetMapping("/getCurfewTimetable/{orderId}")
  fun getCurfewTimetable(
    authentication: Authentication,
    @PathVariable(required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - ID must be a number")
    orderId: String,
  ): ResponseEntity<List<CurfewTimetable>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = curfewTimetableService.getCurfewTimetable(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_CURFEW_TIMETABLE",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
