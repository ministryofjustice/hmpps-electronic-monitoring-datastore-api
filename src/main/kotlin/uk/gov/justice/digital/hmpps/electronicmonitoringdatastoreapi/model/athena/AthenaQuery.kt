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

data class AthenaOrderDetailsQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaMonitoringEventsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaIncidentEventsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaContactEventsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaServicesQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaSuspensionOfVisitsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaEquipmentDetailsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaVisitDetailsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

data class AthenaViolationEventsListQuery(
  override val queryString: String,
) : AthenaQuery(queryString)

// AM Query types
data class AthenaAmOrderDetailsQuery(
  override val queryString: String,
) : AthenaQuery(queryString)
