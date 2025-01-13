package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

abstract class AthenaQuery(
  open val queryString: String,
)

data class AthenaStringQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaDocumentListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaKeyOrderInformationQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaOrderSearchQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaSubjectHistoryReportQuery(
  override val queryString: String,
) : AthenaQuery(queryString)
