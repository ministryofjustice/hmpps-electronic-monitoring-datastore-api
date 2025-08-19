package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AmServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AmServiceDetailsRepository

@Service
class AmServiceDetailsService(
  val amServiceDetailsRepository: AmServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String): List<AmServiceDetails> {
    val result = amServiceDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AmServiceDetails(item) }
  }
}
