package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmServiceDetailsRepository

@Service
class AmServiceDetailsService(
  @Autowired val amServiceDetailsRepository: AmServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, role: AthenaRole): List<AmServiceDetails> {
    val result = amServiceDetailsRepository.getServiceDetails(legacySubjectId, role)

    return result.map { item -> AmServiceDetails(item) }
  }
}
