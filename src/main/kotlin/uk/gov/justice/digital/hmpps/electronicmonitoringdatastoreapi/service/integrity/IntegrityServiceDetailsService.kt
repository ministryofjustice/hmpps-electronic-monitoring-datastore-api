package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityServiceDetailsRepository

@Service
class IntegrityServiceDetailsService(
  @Autowired val integrityServiceDetailsRepository: IntegrityServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, role: AthenaRole): List<IntegrityServiceDetails> {
    val result = integrityServiceDetailsRepository.getServiceDetails(legacySubjectId, role)

    return result.map { item -> IntegrityServiceDetails(item) }
  }
}
