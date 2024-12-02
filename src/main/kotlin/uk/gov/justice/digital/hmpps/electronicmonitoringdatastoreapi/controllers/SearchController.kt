package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CustomQuery
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CustomQueryResponse
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
    @RequestBody customQuery: CustomQuery,
  ): CustomQueryResponse {
    val queryString: String = customQuery.queryString
    val validatedRole: AthenaRole = AthenaRole.fromString(unvalidatedRole) ?: AthenaRole.DEV
    val result: String

    try {
      val athenaService = AthenaService()
      result = athenaService.getQueryResult(validatedRole, queryString)
    } catch (e: Exception) {
      return CustomQueryResponse(
        queryString = queryString,
        athenaRole = validatedRole.name,
        isErrored = true,
        errorMessage = e.message.toString(),
      )
    }

    return CustomQueryResponse(
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
    return OrderRepository.getOrders()
  }
}
