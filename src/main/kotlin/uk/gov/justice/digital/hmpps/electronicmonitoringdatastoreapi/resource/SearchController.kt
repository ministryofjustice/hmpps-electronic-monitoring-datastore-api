package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.QueryExecutionResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRoleService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
class SearchController(
  @Autowired val orderService: OrderService,
  val athenaRoleService: AthenaRoleService,
  @Autowired val auditService: AuditService,
) {
  @GetMapping("/confirmConnection")
  fun confirmConnection(
    authentication: Authentication,
  ): ResponseEntity<Map<String, String>> {
    try {
      auditService.createEvent(
        authentication.principal.toString(),
        "CONFIRM_CONNECTION",
        mapOf("confirmConnection" to "true"),
      )

      val athenaAccess: Boolean = confirmAthenaAccess(authentication)
      val message: String = if (athenaAccess) "Connection successful" else "API Connection successful, but no access to Athena"

      return ResponseEntity(
        mapOf("message" to message),
        HttpStatus.OK,
      )
    } catch (e: Exception) {
      return ResponseEntity(
        mapOf("message" to "Error determining Athena access - potentially a logging/audit issue."),
        HttpStatus.OK,
      )
    }
  }

  fun confirmAthenaAccess(authentication: Authentication): Boolean {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    return try {
      orderService.checkAvailability(validatedRole)
    } catch (ex: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
    }
  }

  @PostMapping("/orders")
  fun searchOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
  ): ResponseEntity<QueryExecutionResponse> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val queryExecutionId = orderService.getQueryExecutionId(orderSearchCriteria, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "SEARCH_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "queryExecutionId" to queryExecutionId,
      ),
    )

    return ResponseEntity.ok(QueryExecutionResponse(queryExecutionId))
  }

  @GetMapping("/orders/{queryExecutionId}")
  fun getSearchResults(
    authentication: Authentication,
    @PathVariable(required = true) queryExecutionId: String,
  ): ResponseEntity<List<OrderSearchResult>> {
    val validatedRole = athenaRoleService.getRoleFromAuthentication(authentication)

    val results = orderService.getSearchResults(queryExecutionId, validatedRole)

    auditService?.createEvent(
      authentication.name,
      "RETRIEVE_SEARCH_RESULT",
      mapOf(
        "executionId" to queryExecutionId,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }
}
