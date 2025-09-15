package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.alcoholMonitoring.AlcoholMonitoringVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.alcoholMonitoring.AlcoholMonitoringVisitDetailsRepository

@Service
class AlcoholMonitoringVisitDetailsService(
  val alcoholMonitoringVisitDetailsRepository: AlcoholMonitoringVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String): List<AlcoholMonitoringVisitDetails> {
    val result = alcoholMonitoringVisitDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringVisitDetails(item) }
  }
}
