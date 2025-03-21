package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.VisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.VisitDetailsRepository

@Service
class VisitDetailsService(
  @Autowired val visitDetailsRepository: VisitDetailsRepository,
) {
  fun getVisitDetails(orderId: String, allowSpecials: Boolean): List<VisitDetails> {
    val result = visitDetailsRepository.getVisitDetails(orderId, allowSpecials)

    return result.map { item -> VisitDetails(item) }
  }
}
