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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/orders"], produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderController(
  @Autowired val orderService: OrderService,
  val amOrderService: AmOrderService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @GetMapping("/getOrderSummary/{orderId}")
  fun getOrderSummary(
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

  @GetMapping("/getOrderDetails/{orderId}")
  fun getOrderDetails(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<OrderDetails> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderService.getOrderDetails(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_ORDER_DETAILS",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }

  @GetMapping("/AM/getOrderDetails/{orderId}")
  fun getAmOrderDetails(
    authentication: Authentication,
    @PathVariable(required = true) orderId: String,
  ): ResponseEntity<AmOrderDetails> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = amOrderService.getAmOrderDetails(orderId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "GET_AM_ORDER_DETAILS",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
