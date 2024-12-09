package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.AthenaClientException
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.HmppsElectronicMonitoringDatastoreApiExceptionHandler
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.*
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestController
@PreAuthorize("hasRole('ELECTRONIC_MONITORING_DATASTORE_API_SEARCH')")
@RequestMapping(value = ["/search"], produces = ["application/json"])
class SearchController {

  @GetMapping("/cases/{caseID}")
  fun getCases(
    @PathVariable(
      required = true,
      name = "caseID",
    ) caseId: String,
  ): JSONObject {
    val response: JSONObject = JSONObject(
      mapOf("data" to "You have successfully queried case $caseId"),
    )

    return response
  }

  @GetMapping("/testEndpoint")
  fun confirmAthenaAccess(): JSONObject {
    val athenaService = AthenaService()
    val result: String = athenaService.getQueryResult(AthenaRole.DEV)

    val response: JSONObject = JSONObject(
      mapOf("data" to result),
    )

    return response
  }

  @PostMapping("/custom-query")
  fun queryAthena(
    @RequestHeader("X-User-Token", required = true) userToken: String,
    @RequestHeader("X-Role", required = false) unvalidatedRole: String = "unset",
    @RequestBody athenaQuery: AthenaQuery,
  ): AthenaQueryResponse<String> {
    val queryString: String = athenaQuery.queryString
    val validatedRole: AthenaRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV
    val result: String

    try {
      val athenaService = AthenaService()
      result = athenaService.getQueryResult(validatedRole, queryString)
    } catch (e: Exception) {
      return AthenaQueryResponse(
        queryString = queryString,
        athenaRole = validatedRole.name,
        isErrored = true,
        errorMessage = e.message.toString(),
      )
    }

    return AthenaQueryResponse<String>(
      queryString = queryString,
      athenaRole = validatedRole.name,
      isErrored = false,
      queryResponse = result,
    )
  }

  @PostMapping("/orders")
  fun searchOrders(
    @RequestHeader("X-User-Token", required = true) userToken: String,
    @RequestBody searchCriteria: SearchCriteria,
  ): List<Order> {
    return OrderRepository.getFakeOrders()
  }

  /*
  * Plan: Hex. architecture: use my AthenaQueryResponse and AthenaQuery to send data ABOUT queries internally to my application
  *
  * So, what goes *in*: commands: so the query, and any command objects
  * Repository vs service: service manages the CONNECTION - so will sit below the data layer
  * => different 'repositories' implement the service
  *
  * => put a bunch into the repository layer
  * OR, service -> orders repository -> generic repository ~ CLIENT
  *
  * refactor!
  * 1) OrderService [holds and manages objects of the <Order> type
  * 2) Queries AthenaService [generic]
  *
  * so for now: we just call it OrderRepository ; move AthenaService calls to there
  * Also add a GenericQueryService -> Nah, this can just be the base athena service
  */

  @PostMapping("/orders-temp")
  fun searchOrdersTemp(
    @RequestHeader("X-User-Token", required = true) userToken: String,
    @RequestBody searchCriteria: SearchCriteria,
  ): ResponseEntity<List<Order>> {
    val repository = OrderRepository()

    // 2: query repository
    val result: AthenaQueryResponse<List<Order>> = repository.getOrders(searchCriteria);

    if (result.isErrored) {
      throw AthenaClientException(result.errorMessage ?: "no error message sent")
    }

    return ResponseEntity<List<Order>>(
      result.queryResponse,
      HttpStatus.OK)
  }
}
