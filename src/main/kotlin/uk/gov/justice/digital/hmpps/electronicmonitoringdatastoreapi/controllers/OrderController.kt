package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import net.minidev.json.JSONObject
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository

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
//
//  @GetMapping("/getOrderSummary/{orderId}")
//  fun getOrderSummary(
//    @PathVariable orderId: String,
//    @RequestHeader(name = "X-User-Token", required = true) userToken: String,
//  ): JSONObject {
//    if (!checkValidUser(userToken)) {
//      return JSONObject(mapOf("data" to "Unauthorised request with user token $userToken"))
//    }
//
//    val orderInfo: OrderInformation? = OrderInformationRepository.getOrderInformation(orderId)
//    return if (orderInfo != null) {
//      JSONObject(mapOf("data" to orderInfo))
//    } else {
//      JSONObject(mapOf("data" to "No summary available for order ID $orderId"))
//    }
//  }
//
//  @GetMapping("/getMockOrderSummary/{orderId}")
//  fun getMockOrderSummary(
//    @PathVariable orderId: String,
//    @RequestHeader(name = "X-User-Token", required = true) userToken: String,
//  ): JSONObject {
//    val orderInfo: OrderInformation? = OrderInformationRepository.getMockOrderInformation(orderId)
//    return if (orderInfo != null) {
//      JSONObject(mapOf("data" to orderInfo))
//    } else {
//      JSONObject(mapOf("data" to "No summary available for order ID $orderId"))
//    }
//  }

  @GetMapping("/getOrderSummary/{orderId}")
  fun getOrderSummary(
    @PathVariable orderId: String,
    @RequestHeader(name = "X-User-Token", required = true) userToken: String,
  ): ResponseEntity<Any> {
    if (!checkValidUser(userToken)) {
      return ResponseEntity
        .status(401)
        .body(mapOf("error" to "Unauthorized request with user token $userToken"))
    }

    val orderInfo: OrderInformation? = OrderInformationRepository.getOrderInformation(orderId)
    return if (orderInfo != null) {
      ResponseEntity.ok(orderInfo)
    } else {
      ResponseEntity
        .notFound()
        .build()
    }
  }

  @GetMapping("/getMockOrderSummary/{orderId}")
  fun getMockOrderSummary(
    @PathVariable orderId: String,
    @RequestHeader(name = "X-User-Token", required = true) userToken: String,
  ): ResponseEntity<Any> {
    val orderInfo: OrderInformation? = OrderInformationRepository.getOrderInformation(orderId)
    return if (orderInfo != null) {
      ResponseEntity.ok(orderInfo)
    } else {
      ResponseEntity
        .notFound()
        .build()
    }
  }

  fun checkValidUser(userToken: String): Boolean {
    val validTokenValue: String = "real-token"

    return userToken == validTokenValue
  }
}
