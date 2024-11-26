package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
    val result: String = athenaService.getQueryResult()

    val response: JSONObject = JSONObject(
      mapOf("data" to result),
    )

    return response
  }
}
