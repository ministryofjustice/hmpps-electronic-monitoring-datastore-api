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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmServicesService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class AmServicesController(
  @Autowired val amServicesService: AmServicesService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {
  @Operation(
    tags = ["Alcohol monitoring orders"],
    summary = "Get the services for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/alcohol-monitoring/{legacySubjectId}/services",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getServices(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "Input contains illegal characters - legacy subject ID may only contain letters and numbers",
    )
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<AmService>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = amServicesService.getServices(legacySubjectId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_SERVICES",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(result)
  }
}
