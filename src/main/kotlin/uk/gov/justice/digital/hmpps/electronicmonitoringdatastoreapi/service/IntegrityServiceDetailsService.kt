package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.IntegrityServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.IntegrityServiceDetailsRepository

@Service
class IntegrityServiceDetailsService(
  val integrityServiceDetailsRepository: IntegrityServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, restricted: Boolean): List<IntegrityServiceDetails> {
    val result = integrityServiceDetailsRepository.findByLegacySubjectId(legacySubjectId, restricted)

    return result.map { item -> IntegrityServiceDetails(item) }
  }
}
