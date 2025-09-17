package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityVisitDetailsRepository

@Service
class IntegrityVisitDetailsService(
  val integrityVisitDetailsRepository: IntegrityVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityVisitDetails> {
    val result = integrityVisitDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)

    return result.map { item -> IntegrityVisitDetails(item) }
  }
}
