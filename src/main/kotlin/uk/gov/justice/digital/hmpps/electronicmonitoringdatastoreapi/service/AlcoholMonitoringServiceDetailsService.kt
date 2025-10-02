package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.dto.AlcoholMonitoringServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AlcoholMonitoringServiceDetailsRepository

@Service
class AlcoholMonitoringServiceDetailsService(
  val alcoholMonitoringServiceDetailsRepository: AlcoholMonitoringServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String): List<AlcoholMonitoringServiceDetails> {
    val result = alcoholMonitoringServiceDetailsRepository.findByLegacySubjectId(legacySubjectId)

    return result.map { item -> AlcoholMonitoringServiceDetails(item) }
  }
}
