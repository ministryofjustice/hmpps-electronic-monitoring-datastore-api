package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderSearchResult
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.OrderService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO', 'ROLE_ELECTRONIC_MONITORING_DATASTORE_API_SEARCH')")
@RequestMapping(value = ["/search"], produces = [MediaType.APPLICATION_JSON_VALUE])
class SearchController(
  @Autowired val orderService: OrderService,

  // TODO: Re-enable audit as @autowired once Cloud Platform in place
  val auditService: AuditService? = null,
) {
  @GetMapping("/testEndpoint")
  fun confirmAthenaAccess(
    authentication: Authentication,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<Boolean> {
    val validatedRole = AthenaRole.Companion.fromString(unvalidatedRole) ?: AthenaRole.DEV

    var isAvailable: Boolean
    try {
      isAvailable = orderService.checkAvailability(validatedRole)
    } catch (ex: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
    }

    auditService?.createEvent(
      authentication.principal.toString(),
      "CONFIRM_ATHENA_ACCESS",
      mapOf(
        "isAvailable" to isAvailable.toString(),
      ),
    )

    return ResponseEntity.ok(isAvailable)
  }

  @PostMapping("/custom-query")
  fun queryAthena(
    authentication: Authentication,
    @RequestBody(required = true) athenaQuery: AthenaQuery,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<AthenaQueryResponse<String>> {
    val validatedRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV

    val query = athenaQuery.queryString
    var result: AthenaQueryResponse<String>
    try {
      val queryResponse = orderService.query(athenaQuery, validatedRole)

      result = AthenaQueryResponse<String>(
        queryString = query,
        athenaRole = validatedRole.name,
        isErrored = false,
        queryResponse = queryResponse,
      )

      auditService?.createEvent(
        authentication.name,
        "SEARCH_WITH_CUSTOM_QUERY",
        mapOf(
          "query" to query,
          "isErrored" to "false",
        ),
      )
    } catch (ex: Exception) {
      auditService?.createEvent(
        authentication.name,
        "SEARCH_WITH_CUSTOM_QUERY",
        mapOf(
          "query" to query,
          "isErrored" to "true",
          "error" to ex.localizedMessage,
        ),
      )

      result = AthenaQueryResponse<String>(
        queryString = query,
        athenaRole = validatedRole.name,
        isErrored = true,
        errorMessage = ex.localizedMessage,
      )
    }

    return ResponseEntity.ok(result)
  }

  @PostMapping("/orders-old")
  fun searchOrdersFake(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<List<OrderSearchResult>> {
    val validatedRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV

    val results: List<OrderSearchResult>
    try {
      results = orderService.search(orderSearchCriteria, validatedRole, true)
    } catch (ex: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
    }

    auditService?.createEvent(
      authentication.name,
      "SEARCH_FAKE_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }

  @PostMapping("/orders")
  fun searchOrders(
    authentication: Authentication,
    @RequestBody orderSearchCriteria: OrderSearchCriteria,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
  ): ResponseEntity<List<OrderSearchResult>> {
    val validatedRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV

    val results: List<OrderSearchResult>
    try {
      results = orderService.search(orderSearchCriteria, validatedRole)
    } catch (ex: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST, ex.localizedMessage, ex)
    }

    auditService?.createEvent(
      authentication.name,
      "SEARCH_ORDERS",
      mapOf(
        "legacySubjectId" to orderSearchCriteria.legacySubjectId,
        "searchType" to orderSearchCriteria.searchType,
        "rows" to results.count().toString(),
      ),
    )

    return ResponseEntity.ok(results)
  }
}
