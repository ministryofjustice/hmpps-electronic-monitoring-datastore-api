package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.IntegritySuspensionOfVisitsRepository

@Service
class IntegritySuspensionOfVisitsService(
  val integritySuspensionOfVisitsRepository: IntegritySuspensionOfVisitsRepository,
) {
  fun getSuspensionOfVisits(legacySubjectId: String, restricted: Boolean): List<IntegritySuspensionOfVisits> {
    val result = integritySuspensionOfVisitsRepository.findByLegacySubjectIdAndRestricted(legacySubjectId, restricted)

    return result.map { item -> IntegritySuspensionOfVisits(item) }
  }
}
