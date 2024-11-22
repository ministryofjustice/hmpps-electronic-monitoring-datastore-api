package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.Order
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SearchCriteria

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

  @PostMapping("/orders")
  fun searchOrders(@RequestBody searchCriteria: SearchCriteria): List<Order> {
    return listOf(
      Order(
        dataType = "am",
        legacySubjectId = 1000000,
        name = "Amy Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "01-01-1970",
        orderStartDate = "08-02-2019",
        orderEndDate = "08-02-2020",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 2000000,
        name = "Bill Smith",
        address = "First line of address",
        alias = "Plato",
        dateOfBirth = "01-02-1971",
        orderStartDate = "03-11-2020",
        orderEndDate = "03-11-2021",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 3000000,
        name = "Claire Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "09-04-1962",
        orderStartDate = "05-08-2001",
        orderEndDate = "05-08-2002",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 8000000,
        name = "Daniel Smith",
        address = "First line of address",
        alias = "Aristotle",
        dateOfBirth = "12-11-1978",
        orderStartDate = "18-02-2012",
        orderEndDate = "18-02-2014",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 30000,
        name = "Emma Smith",
        address = "First line of address",
        alias = "Socrates",
        dateOfBirth = "03-03-2001",
        orderStartDate = "24-01-2017",
        orderEndDate = "24-01-2020",
      ),
      Order(
        dataType = "am",
        legacySubjectId = 4000000,
        name = "Fred Smith",
        address = "First line of address",
        alias = null,
        dateOfBirth = "08-10-1980",
        orderStartDate = "01-05-2021",
        orderEndDate = "01-05-2022",
      ),
    )
  }
}
