package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.integrity

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
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
  @param:Value($$"${services.athena.database}")
  var athenaDatabase: String = "unknown_database",
) {
  fun getKeyOrderInformation(legacySubjectId: String, restricted: Boolean): AthenaIntegrityKeyOrderInformationDTO {
    val keyOrderInformationQuery = IntegrityOrderInformationQueryBuilder(athenaDatabase)
      .withLegacySubjectId(legacySubjectId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(keyOrderInformationQuery, restricted)

    val result = AthenaHelper.mapTo<AthenaIntegrityKeyOrderInformationDTO>(athenaResponse)

    return result.first()
  }

  fun getSubjectHistoryReport(orderId: String, role: AthenaRole): AthenaIntegritySubjectHistoryReportDTO {
    val subjectHistoryReportQuery = IntegritySubjectHistoryReportQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(subjectHistoryReportQuery, role)

    val result = AthenaHelper.mapTo<AthenaIntegritySubjectHistoryReportDTO>(athenaResponse)

    return result.first()
  }

  fun getDocumentList(orderId: String, role: AthenaRole): List<AthenaIntegrityDocumentDTO> {
    val documentListQuery = IntegrityDocumentsQueryBuilder(athenaDatabase)
      .withLegacySubjectId(orderId)
      .build()

    val athenaResponse = athenaClient.getQueryResult(documentListQuery, role)

    return AthenaHelper.mapTo<AthenaIntegrityDocumentDTO>(athenaResponse)
  }
}
