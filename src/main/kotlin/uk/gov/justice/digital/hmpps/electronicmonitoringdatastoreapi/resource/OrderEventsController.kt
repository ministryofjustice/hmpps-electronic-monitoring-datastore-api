package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ViolationEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class OrderEventsController(
  @Autowired val orderEventsService: OrderEventsService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @GetMapping("/orders/getViolationEvents/{orderId}")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getViolationEvents(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<List<Event<ViolationEventDetails>>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderEventsService.getViolationEvents(orderId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_VIOLATION_EVENTS",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }

  @GetMapping("/orders/getContactEvents/{orderId}")
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getContactEvents(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<List<Event<ContactEventDetails>>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderEventsService.getContactEvents(orderId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_CONTACT_EVENTS",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
