package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderServicesService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/orders"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderServicesController(
  @Autowired val orderServicesService: OrderServicesService,
  val athenaRoleService: AthenaRoleService,

  // TODO: Re-enable audit as @autowired once Cloud Platform in place
  val auditService: AuditService? = null,
) {
  @GetMapping("/{orderId}/services")
  fun getServices(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<List<OrderService>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderServicesService.getServices(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_ORDER_SERVICES",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
