package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderInformationRepository
import kotlin.String

@Service
class AmOrderInformationService(
  @Autowired val orderInformationRepository: AmOrderInformationRepository,
) {

  fun getOrderInformation(legacySubjectId: String, role: AthenaRole): AmOrderInformation {
    val orderInformation = orderInformationRepository.getOrderInformation(legacySubjectId, role)

    return AmOrderInformation(orderInformation)
  }
}
