package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmOrderInformationRepository
import kotlin.String

@Service
class AmOrderInformationService(
  val orderInformationRepository: AmOrderInformationRepository,
) {

  fun getOrderInformation(legacySubjectId: String): AmOrderInformation {
    val orderInformation = orderInformationRepository.getOrderInformation(legacySubjectId)

    return AmOrderInformation(orderInformation)
  }
}
