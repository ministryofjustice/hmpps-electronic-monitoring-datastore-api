package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AlcoholMonitoringVisitDetailsRepository

@Service
class AlcoholMonitoringVisitDetailsService(
  val alcoholMonitoringVisitDetailsRepository: AlcoholMonitoringVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String): List<AlcoholMonitoringVisitDetails> {
    val result = alcoholMonitoringVisitDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringVisitDetails(item) }
  }
}
