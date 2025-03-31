package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderDetailsRepository
import kotlin.String

@Service
class AmOrderDetailsService(
  @Autowired val orderDetailsRepository: AmOrderDetailsRepository,
) {

  fun getOrderDetails(legacySubjectId: String, role: AthenaRole): AmOrderDetails {
    val orderDetails = orderDetailsRepository.getOrderDetails(legacySubjectId, role)

    return AmOrderDetails(orderDetails)
  }
}
