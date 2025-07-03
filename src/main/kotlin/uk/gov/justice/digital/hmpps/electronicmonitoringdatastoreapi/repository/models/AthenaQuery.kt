package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.models

abstract class AthenaQuery(
  open val queryString: String,
  open val parameters: Array<String>,
)

class AthenaOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaOrderSearchQuery(
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

class AthenaServiceDetailsQuery(
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

class AthenaAmEquipmentDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

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
