package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.resource

import net.minidev.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.internal.AuditService

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_EM_DATASTORE_GENERAL_RO', 'ROLE_EM_DATASTORE_RESTRICTED_RO')")
@RequestMapping(value = ["/orders"], produces = ["application/json"])
class OrderController(
  @Autowired val auditService: AuditService,
  @Autowired val repository: OrderInformationRepository,
) {

  @GetMapping("/getMockOrderSummary/{orderId}")
  fun getMockOrderSummary(
    authentication: Authentication,
    @PathVariable orderId: String,
    @RequestHeader("Authorization", required = true) authorization: String,
  ): ResponseEntity<OrderInformation> {
    val orderInfo: OrderInformation = repository.getMockOrderInformation(orderId)

    auditService.createEvent(
      authentication.principal.toString(),
      "GET_MOCK_ORDER_SUMMARY",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(orderInfo)
  }

  // TODO: This is a temporary endpoint to validate code interacting with user claims
  @PreAuthorize("hasRole('ROLE_EM_DATASTORE_RESTRICTED_RO') and hasRole('ROLE_EM_DATASTORE_GENERAL_RO')")
  @GetMapping("/getOrderSummary/specials/{orderId}")
  fun getSpecialsOrder(
    authentication: Authentication,
    @PathVariable(
      required = true,
    ) orderId: String,
    @RequestHeader(
      required = false,
      name = "User-Token",
    ) userToken: String = "no-token-supplied",
  ): JSONObject {
    auditService.createEvent("GET_ORDER", "order<$orderId> requested by user-token<$userToken>")

    // if (!checkValidUser(userToken)) {
    //   auditService.createEvent("GET_ORDER", "request for order<$orderId> blocked for user-token<$userToken>")

    //   return JSONObject(
    //     mapOf("data" to "Unauthorised request with user token $userToken"),
    //   )
    // }

    if (orderId == "invalid-order") {
      auditService.createEvent("GET_ORDER", "order<$orderId> could not be found for user-token<$userToken>")

      return JSONObject(
        mapOf("data" to "No order with ID $orderId could be found"),
        )
      }
      
      val response: JSONObject = JSONObject(
        mapOf("data" to "This is the data for order $orderId"),
        throw AccessDeniedException("Your token is valid for this service, but your user is not allowed to access this resource")
      }
      
      auditService.createEvent(
        "GET_SPECIALS_ORDER_SUMMARY",
        mapOf("orderId" to orderId),
      )
      // auditService.createEvent(
      //   authentication.principal.toString(),
      //   "GET_SPECIALS_ORDER_SUMMARY",
      //   mapOf("orderId" to orderId),
      // )

    return ResponseEntity.ok(
      repository.getMockOrderInformation(orderId),
    )

    return response
  }

  @GetMapping("/getOrderSummary/{orderId}")
  fun getOrderSummary(
    authentication: Authentication,
    @PathVariable(
      required = true,
    ) orderId: String,
    @RequestHeader("Authorization", required = true) authorization: String,
  ): ResponseEntity<OrderInformation> {
    // TODO: Real role validation stuff will go here

    // get fake generic object
    var fakeOrder: OrderInformation = repository.getMockOrderInformation(orderId)

    // get 'real' KeyOrderInfo from the DB
    val keyInfo: AthenaQueryResponse<KeyOrderInformation> = repository.getKeyOrderInformation(orderId)
    // val historyReport: AthenaQueryResponse<SubjectHistoryReport> = repository.getSubjectHistoryReport(orderId)
    // val documentList: AthenaQueryResponse<DocumentList> = repository.getDocumentList(orderId)

    // Put it together
    val result = OrderInformation(
      keyOrderInformation = keyInfo.queryResponse ?: fakeOrder.keyOrderInformation,
      subjectHistoryReport = fakeOrder.subjectHistoryReport,
      documents = fakeOrder.documents,
    )

    auditService.createEvent(
      authentication.principal.toString(),
      "GET_ORDER_SUMMARY",
      mapOf("orderId" to orderId),
    )

    return ResponseEntity.ok(result)
  }
}
