package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource.integrity

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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity.IntegritySuspensionOfVisitsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class SuspensionOfVisitsController(
  @Autowired val integritySuspensionOfVisitsService: IntegritySuspensionOfVisitsService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Integrity orders"],
    summary = "Get the suspension of visits for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/integrity/{legacySubjectId}/suspension-of-visits",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getSuspensionOfVisits(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - legacy subject ID must be a number")
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<IntegritySuspensionOfVisits>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = integritySuspensionOfVisitsService.getSuspensionOfVisits(legacySubjectId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_SUSPENSION_OF_VISITS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(result)
  }
}
