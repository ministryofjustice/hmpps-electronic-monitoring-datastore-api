package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.SuspensionOfVisits
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.SuspensionOfVisitsRepository

@Service
class SuspensionOfVisitsService(
  @Autowired val suspensionOfVisitsRepository: SuspensionOfVisitsRepository,
) {
  fun getSuspensionOfVisits(legacySubjectId: String, allowSpecials: Boolean = false): List<SuspensionOfVisits> {
    val result = suspensionOfVisitsRepository.getSuspensionOfVisits(legacySubjectId, allowSpecials)

    return result.map { item -> SuspensionOfVisits(item) }
  }
}
