package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.models.integrity.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.integrity.IntegritySuspensionOfVisitsRepository

@Service
class IntegritySuspensionOfVisitsService(
  val integritySuspensionOfVisitsRepository: IntegritySuspensionOfVisitsRepository,
) {
  fun getSuspensionOfVisits(legacySubjectId: String, restricted: Boolean): List<IntegritySuspensionOfVisits> {
    val result = integritySuspensionOfVisitsRepository.getSuspensionOfVisits(legacySubjectId, restricted)

    return result.map { item -> IntegritySuspensionOfVisits(item) }
  }
}
