package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderDetailsRepository
import kotlin.String

@Service
class AmOrderDetailsService(
  val orderDetailsRepository: AmOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String): AmOrderDetails {
    val orderDetails = orderDetailsRepository.getOrderDetails(legacySubjectId)

    return AmOrderDetails(orderDetails)
  }
}
