@file:Suppress("ArrayInDataClass")

package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

abstract class AthenaQuery(
  open val queryString: String,
  open val parameters: Array<String>,
)

class AthenaStringQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaDocumentListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaKeyOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaOrderSearchQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaSubjectHistoryReportQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaMonitoringEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIncidentEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaContactEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaServicesQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaSuspensionOfVisitsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaEquipmentDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaVisitDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaViolationEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

// AM Query types
class AthenaAmOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAmOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAmServicesListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAmVisitDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)
