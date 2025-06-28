package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderInformationRepository

@Service
class IntegrityOrderInformationService(
  @field:Autowired val integrityOrderInformationRepository: IntegrityOrderInformationRepository,
) {
  fun getOrderInformation(legacySubjectId: String, restricted: Boolean): IntegrityOrderInformation {
    val integrityOrderInformationDTO = integrityOrderInformationRepository.getOrderInformation(legacySubjectId, restricted)

    return IntegrityOrderInformation(integrityOrderInformationDTO)
  }
}
