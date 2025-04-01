package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmVisitDetailsRepository

@Service
class AmVisitDetailsService(
  @Autowired val amVisitDetailsRepository: AmVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String, role: AthenaRole): List<AmVisitDetails> {
    val result = amVisitDetailsRepository.getVisitDetails(legacySubjectId, role)

    return result.map { item -> AmVisitDetails(item) }
  }
}
