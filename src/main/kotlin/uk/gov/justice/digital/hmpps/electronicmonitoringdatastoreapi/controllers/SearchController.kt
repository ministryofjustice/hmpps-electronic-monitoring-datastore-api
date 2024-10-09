package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
  ): String {
    val response = "Successful search with ID $caseId"
    return response
  }
}
