package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
// @PreAuthorize("hasRole('ELECTRONIC_MONITORING_DATASTORE_API_SEARCH') and hasAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
@PreAuthorize("hasRole('ELECTRONIC_MONITORING_DATASTORE_API_SEARCH')")
@RequestMapping(value = ["/orders"], produces = ["application/json"])
class OrderController {

  @GetMapping("/{orderID}")
  fun getOrder(
    @PathVariable(
      required = true,
      name = "orderID",
    ) orderId: String,
    @RequestHeader(
      required = false,
      name = "User-Token",
    ) userToken: String = "no-token-supplied",
  ): JSONObject {
    if (!checkValidUser(userToken)) {
      return JSONObject(
        mapOf("data" to "Unauthorised request with user token $userToken"),
      )
    }

    if (orderId == "invalid-order") {
      return JSONObject(
        mapOf("data" to "No order with ID $orderId could be found"),
      )
    }

    val response: JSONObject = JSONObject(
      mapOf("data" to "This is the data for order $orderId"),
    )

    return response
  }

  fun checkValidUser(userToken: String): Boolean {
    val validTokenValue: String = "real-token"

    return userToken == validTokenValue
  }
}
