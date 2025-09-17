package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegrityServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegrityServiceDetailsRepository

@Service
class IntegrityServiceDetailsService(
  val integrityServiceDetailsRepository: IntegrityServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityServiceDetails> {
    val result = integrityServiceDetailsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)

    return result.map { item -> IntegrityServiceDetails(item) }
  }
}
