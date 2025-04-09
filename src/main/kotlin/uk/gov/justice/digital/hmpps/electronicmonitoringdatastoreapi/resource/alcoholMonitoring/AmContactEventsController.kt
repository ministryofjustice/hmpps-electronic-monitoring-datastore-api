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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmEvent
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring.AmOrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class AmContactEventsController(
  @Autowired val amOrderEventsService: AmOrderEventsService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Alcohol monitoring orders"],
    summary = "Get the contact events for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/getContactEvents/{legacySubjectId}",
      "/alcohol-monitoring/{legacySubjectId}/contact-events",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getContactEvents(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(
      regexp = "^[a-zA-Z0-9]+$",
      message = "Input contains illegal characters - legacy subject ID may only contain letters and numbers",
    )
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<AmEvent<AmContactEventDetails>>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val result = amOrderEventsService.getContactEvents(legacySubjectId, validatedRole)

    auditService.createEvent(
      authentication.name,
      "GET_ALCOHOL_MONITORING_CONTACT_EVENTS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to (validatedRole == AthenaRole.ROLE_EM_DATASTORE_RESTRICTED_RO),
      ),
    )

    return ResponseEntity.ok(result)
  }
}
