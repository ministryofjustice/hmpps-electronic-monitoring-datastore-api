package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegritySuspensionOfVisitsRepository

@Service
class IntegritySuspensionOfVisitsService(
  @Autowired val integritySuspensionOfVisitsRepository: IntegritySuspensionOfVisitsRepository,
) {
  fun getSuspensionOfVisits(legacySubjectId: String, role: AthenaRole): List<IntegritySuspensionOfVisits> {
    val result = integritySuspensionOfVisitsRepository.getSuspensionOfVisits(legacySubjectId, role)

    return result.map { item -> IntegritySuspensionOfVisits(item) }
  }
}
