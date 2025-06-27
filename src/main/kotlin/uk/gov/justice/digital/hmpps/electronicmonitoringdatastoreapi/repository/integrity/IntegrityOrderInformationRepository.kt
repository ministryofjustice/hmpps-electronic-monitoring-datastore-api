package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClientInterface
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.AthenaHelper
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityDocumentsQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegrityOrderInformationQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders.integrity.IntegritySubjectHistoryReportQueryBuilder
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityDocumentDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegrityKeyOrderInformationDTO
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity.AthenaIntegritySubjectHistoryReportDTO

@Service
class IntegrityOrderInformationRepository(
  val athenaClient: EmDatastoreClientInterface,
) {
  fun getKeyOrderInformation(legacySubjectId: String, restricted: Boolean): AthenaIntegrityKeyOrderInformationDTO {
    val keyOrderInformationQuery = IntegrityOrderInformationQueryBuilder()
      .withLegacySubjectId(legacySubjectId)

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegrityKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, restricted: Boolean): AthenaIntegritySubjectHistoryReportDTO {
    val subjectHistoryReportQuery = IntegritySubjectHistoryReportQueryBuilder()
      .withLegacySubjectId(orderId)

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegritySubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, restricted: Boolean): List<AthenaIntegrityDocumentDTO> {
    val documentListQuery = IntegrityDocumentsQueryBuilder()
      .withLegacySubjectId(orderId)

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, restricted)

    return AthenaHelper.mapTo<AthenaIntegrityDocumentDTO>(athenaResponse)
  }
}
