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
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ContactEventDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Event
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderEventsService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
class ContactEventsController(
  @Autowired val orderEventsService: OrderEventsService,
  @Autowired val auditService: AuditService,
) {

  @Operation(
    tags = ["Integrity orders"],
    summary = "Get the contact events for an order",
  )
  @RequestMapping(
    method = [RequestMethod.GET],
    path = [
      "/orders/getContactEvents/{legacySubjectId}",
      "/integrity/orders/{legacySubjectId}/contact-events",
    ],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  @PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
  fun getContactEvents(
    authentication: Authentication,
    @Parameter(description = "The legacy subject ID of the order", required = true)
    @Pattern(regexp = "^[0-9]+$", message = "Input contains illegal characters - legacy subject ID must be a number")
    @PathVariable legacySubjectId: String,
  ): ResponseEntity<List<Event<ContactEventDetails>>> {
    val result = orderEventsService.getContactEvents(legacySubjectId)

    auditService.createEvent(
      authentication.name,
      "GET_CONTACT_EVENTS",
      mapOf(
        "legacySubjectId" to legacySubjectId,
        "restrictedOrdersIncluded" to false,
      ),
    )

    return ResponseEntity.ok(result)
  }
}
