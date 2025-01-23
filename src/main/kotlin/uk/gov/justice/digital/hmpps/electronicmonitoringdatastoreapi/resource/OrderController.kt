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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/orders"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderController(
  @Autowired val orderService: OrderService,
  val athenaRoleService: AthenaRoleService,

  // TODO: Re-enable audit as @autowired once Cloud Platform in place
  val auditService: AuditService? = null,
) {

  @GetMapping("/getMockOrderSummary/{orderId}")
  fun getMockOrderSummary(
    authentication: Authentication,
    @PathVariable orderId: String,
  ): ResponseEntity<OrderInformation> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderService.getOrderInformation(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_MOCK_ORDER_SUMMARY",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }

  // TODO: This is a temporary endpoint to validate code interacting with user claims
  @PreAuthorize("hasRole('ROLE_EM_DATASTORE_RESTRICTED_RO') and hasRole('ROLE_EM_DATASTORE_GENERAL_RO')")
  @GetMapping("/getOrderSummary/specials/{orderId}")
  fun getSpecialsOrder(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<OrderInformation> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderService.getOrderInformation(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_SPECIALS_ORDER_SUMMARY",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }

  @GetMapping("/getOrderSummary/{orderId}")
  fun getOrderSummary(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<OrderInformation> = getOrder(authentication, orderId)

  @GetMapping("/{orderId}")
  fun getOrder(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<OrderInformation> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderService.getOrderInformation(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_ORDER_SUMMARY",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }

  @GetMapping("/getOrderdetails/{orderId}")
  fun getOrderDetails(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<OrderDetails> {
    val validatedRole = AthenaRole.Companion.fromString(unvalidatedRole) ?: AthenaRole.DEV

    val result = OrderDetails.createEmpty()
//    val result = orderService.getOrderDetails(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_ORDER_DETAILS",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
