package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.alcoholMonitoring

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderDetailsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class AmOrderDetailsController(
  @Autowired val orderDetailsService: AmOrderDetailsService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Alcohol monitoring orders"],
    summary = "Get the details for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/alcohol-monitoring/{legacySubjectId}/details",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getOrderDetails(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "Input contains illegal characters - legacy subject ID may only contain letters and numbers",
    )
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<AmOrderDetails> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = orderDetailsService.getOrderDetails(legacySubjectId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_ORDER_DETAILS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(result)
  }
}
