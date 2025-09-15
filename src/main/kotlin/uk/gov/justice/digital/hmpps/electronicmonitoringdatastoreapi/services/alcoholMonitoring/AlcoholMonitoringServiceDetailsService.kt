package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringServiceDetailsRepository

@Service
class AlcoholMonitoringServiceDetailsService(
  val alcoholMonitoringServiceDetailsRepository: AlcoholMonitoringServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String): List<AlcoholMonitoringServiceDetails> {
    val result = alcoholMonitoringServiceDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringServiceDetails(item) }
  }
}
