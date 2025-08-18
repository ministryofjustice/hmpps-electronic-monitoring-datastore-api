package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityVisitDetailsRepository

@Service
class IntegrityVisitDetailsService(
  val integrityVisitDetailsRepository: IntegrityVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityVisitDetails> {
    val result = integrityVisitDetailsRepository.getVisitDetails(legacySubjectId, restricted)

    return result.map { item -> IntegrityVisitDetails(item) }
  }
}
