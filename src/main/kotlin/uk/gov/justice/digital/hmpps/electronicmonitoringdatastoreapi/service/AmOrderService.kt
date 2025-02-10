package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaAmOrderDetailsDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AmOrderDetailsRepository
import kotlin.String

@Service
class AmOrderService(
  @Autowired val amOrderDetailsRepository: AmOrderDetailsRepository,
) {
  fun getAmOrderDetails(orderId: String, role: AthenaRole): AmOrderDetails {
    val amOrderDetailsDTO: AthenaAmOrderDetailsDTO = amOrderDetailsRepository.getAmOrderDetails(orderId, role)
    return AmOrderDetails(amOrderDetailsDTO)
  }
}
