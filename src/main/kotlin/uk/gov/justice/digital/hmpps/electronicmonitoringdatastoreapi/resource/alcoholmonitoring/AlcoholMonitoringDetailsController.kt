package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholmonitoring

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AmOrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class AlcoholMonitoringDetailsController(
  @Autowired val orderService: OrderService,
  val amOrderService: AmOrderService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Alcohol monitoring orders"],
    summary = "Get the details for an alcohol monitoring order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/AM/getOrderDetails/{legacySubjectId}",
      "/alcohol-monitoring/orders/{legacySubjectId}/details",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getAmOrderDetails(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - legacy subject ID must be a number")
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<AmOrderDetails> {
    val result = amOrderService.getAmOrderDetails(legacySubjectId)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_ORDER_DETAILS",
      mapOf("legacySubjectId" to legacySubjectId),
    )

    return ResponseEntity.ok(result)
  }
}
