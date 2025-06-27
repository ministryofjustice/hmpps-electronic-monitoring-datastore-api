package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityServiceDetailsRepository

@Service
class IntegrityServiceDetailsService(
  val integrityServiceDetailsRepository: IntegrityServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityServiceDetails> {
    val result = integrityServiceDetailsRepository.getServiceDetails(legacySubjectId, restricted)

    return result.map { item -> IntegrityServiceDetails(item) }
  }
}
