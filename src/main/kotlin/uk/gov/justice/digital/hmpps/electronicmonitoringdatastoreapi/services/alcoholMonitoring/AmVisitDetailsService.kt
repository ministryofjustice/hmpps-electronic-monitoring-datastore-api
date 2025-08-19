package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmVisitDetailsRepository

@Service
class AmVisitDetailsService(
  val amVisitDetailsRepository: AmVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String): List<AmVisitDetails> {
    val result = amVisitDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AmVisitDetails(item) }
  }
}
