package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AthenaQueryResponse
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.KeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.OrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.OrderInformationRepository

@RestController
// @PreAuthorize("hasRole('ELECTRONIC_MONITORING_DATASTORE_API_SEARCH') and hasAuthority('ROLE_EM_DATASTORE_GENERAL_RO')")
@PreAuthorize("hasRole('ELECTRONIC_MONITORING_DATASTORE_API_SEARCH')")
@RequestMapping(value = ["/orders"], produces = ["application/json"])
class OrderController(
  @Autowired val repository: OrderInformationRepository,
) {

  @GetMapping("/getMockOrderSummary/{orderId}")
  fun getMockOrderSummary(
    @PathVariable orderId: String,
    @RequestHeader(
      name = "X-User-Token",
      required = true,
    ) userToken: String,
  ): ResponseEntity<OrderInformation> {
    val repository = OrderInformationRepository()
    val orderInfo: OrderInformation = repository.getMockOrderInformation(orderId)
    return ResponseEntity.ok(orderInfo)
  }

  // TODO: This is a temporary endpoint to validate code interacting with user claims
  @PreAuthorize("hasAuthority('ROLE_EM_DATASTORE_RESTRICTED_RO')")
  @GetMapping("/getOrderSummary/specials/{orderId}")
  fun getSpecialsOrder(
    @PathVariable(
      required = true,
    ) orderId: String,
    @RequestHeader(
      name = "X-User-Token",
      required = true,
    ) userToken: String,
  ): ResponseEntity<OrderInformation> {
    // TODO: code to interact with the user role claims to go here

    return ResponseEntity.ok(
      repository.getMockOrderInformation(orderId),
    )
  }

  @GetMapping("/getOrderSummary/{orderId}")
  fun getOrderSummary(
    @PathVariable(
      required = true,
    ) orderId: String,
    @RequestHeader(
      name = "X-User-Token",
      required = true,
    ) userToken: String,
  ): ResponseEntity<OrderInformation> {
    if (!checkValidUser(userToken)) {
      throw AccessDeniedException("Your token is valid for this service, but your user is not allowed to access this resource")
    }

    // get fake generic object
//    val repository = OrderInformationRepository()
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

    return ResponseEntity.ok(result)
  }

  fun checkValidUser(userToken: String): Boolean {
    val invalidTokenValue: String = "invalid-token"

    return userToken != invalidTokenValue
  }
}
