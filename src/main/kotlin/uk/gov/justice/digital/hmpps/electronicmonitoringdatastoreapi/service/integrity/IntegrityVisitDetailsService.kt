package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityVisitDetailsRepository

@Service
class IntegrityVisitDetailsService(
  @Autowired val integrityVisitDetailsRepository: IntegrityVisitDetailsRepository,
) {
  fun getVisitDetails(legacySubjectId: String, role: AthenaRole): List<IntegrityVisitDetails> {
    val result = integrityVisitDetailsRepository.getVisitDetails(legacySubjectId, role)

    return result.map { item -> IntegrityVisitDetails(item) }
  }
}
