package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.IntegrityVisitDetailsRepository

@Service
class IntegrityVisitDetailsService(
  val integrityVisitDetailsRepository: IntegrityVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityVisitDetails> {
    val result = integrityVisitDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)

    return result.map { item -> IntegrityVisitDetails(item) }
  }
}
