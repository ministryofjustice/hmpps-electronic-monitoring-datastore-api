package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import software.amazon.awssdk.services.athena.model.ResultSet
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AthenaService

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
    val response: JSONObject = JSONObject()
    response.put("data", "You have successfully queried case $caseId")

    return response
  }

  @GetMapping("/testEndpoint")
  fun confirmAthenaAccess(): JSONObject {
    val athenaService = AthenaService()
    val fakeQuery: String = "SELECT * FROM dummy_table_1 limit 10;"
    val resultSet: ResultSet = athenaService.getQueryResult(AthenaRole.DEV, fakeQuery)
    val stringResult: String = resultSet.toString()

//    val response: JSONObject = JSONObject()
//    response.put("data", stringResult)
    val response: JSONObject = JSONObject(resultSet)

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
      val resultSet: ResultSet = athenaService.getQueryResult(validatedRole, queryString)
      result = resultSet.toString()
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
  ): List<Order> = OrderRepository.getFakeOrders()

  @PostMapping("/orders-temp")
  fun searchOrdersTemp(
    @RequestHeader("X-User-Token", required = true) userToken: String,
    @RequestBody searchCriteria: SearchCriteria,
  ): ResponseEntity<List<Order>> {
    val repository = OrderRepository()

    // 2: query repository
    val result: AthenaQueryResponse<List<Order>> = repository.getOrders(searchCriteria)

    return ResponseEntity<List<Order>>(
      result.queryResponse,
      HttpStatus.OK,
    )
  }
}
