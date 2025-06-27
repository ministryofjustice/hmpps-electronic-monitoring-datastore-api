package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmServiceDetailsRepository

@Service
class AmServiceDetailsService(
  val amServiceDetailsRepository: AmServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String): List<AmServiceDetails> {
    val result = amServiceDetailsRepository.getServiceDetails(legacySubjectId)

    return result.map { item -> AmServiceDetails(item) }
  }
}
