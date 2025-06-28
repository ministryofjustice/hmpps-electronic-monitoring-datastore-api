package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.integrity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityKeyOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegrityOrderInformation
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity.IntegritySubjectHistoryReport
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity.IntegrityOrderInformationRepository

@Service
class IntegrityOrderInformationService(
  @field:Autowired val integrityOrderInformationRepository: IntegrityOrderInformationRepository,
) {
  fun getOrderInformation(legacySubjectId: String, restricted: Boolean): IntegrityOrderInformation {
    val keyOrderInformation = integrityOrderInformationRepository.getKeyOrderInformation(legacySubjectId, restricted)
    val parsedIntegrityKeyOrderInformation = IntegrityKeyOrderInformation(keyOrderInformation)

    val emptyHistoryReport: IntegritySubjectHistoryReport = IntegritySubjectHistoryReport.Companion.createEmpty()
//    val subjectHistoryReport = orderInformationRepository.getSubjectHistoryReport(orderId, role)
//    val parsedSubjectHistoryReport = parseSubjectHistoryReport(subjectHistoryReport)

//    val documentList = orderInformationRepository.getDocumentList(orderId, role)
//    val parsedDocumentList = parseDocumentList(documentList)

    // Put it together
    return IntegrityOrderInformation(
      keyOrderInformation = parsedIntegrityKeyOrderInformation,
      subjectHistoryReport = emptyHistoryReport,
      documents = listOf(),
    )
  }
}
