package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmVisitDetailsRepository

@Service
class AmVisitDetailsService(
  val amVisitDetailsRepository: AmVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String): List<AmVisitDetails> {
    val result = amVisitDetailsRepository.getVisitDetails(legacySubjectId)

    return result.map { item -> AmVisitDetails(item) }
  }
}
