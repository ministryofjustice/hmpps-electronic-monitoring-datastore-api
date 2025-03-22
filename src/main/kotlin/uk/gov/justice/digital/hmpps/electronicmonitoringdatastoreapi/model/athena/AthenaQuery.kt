@file:Suppress("ArrayInDataClass")

package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

abstract class AthenaQuery(
  open val queryString: String,
  open val parameters: Array<String>,
)

data class AthenaStringQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaDocumentListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaKeyOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaOrderSearchQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaSubjectHistoryReportQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaMonitoringEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaIncidentEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaContactEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaServicesQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaSuspensionOfVisitsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaEquipmentDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaVisitDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

data class AthenaViolationEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

// AM Query types
data class AthenaAmOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)
