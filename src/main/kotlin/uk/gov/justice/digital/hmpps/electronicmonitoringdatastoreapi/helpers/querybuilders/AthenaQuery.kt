package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers.querybuilders

abstract class AthenaQuery(
  open val queryString: String,
  open val parameters: Array<String>,
)

class AthenaIntegrityOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityOrderSearchQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityMonitoringEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityIncidentEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityContactEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityServiceDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegritySuspensionOfVisitsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityEquipmentDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityVisitDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaIntegrityViolationEventsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

// AM Query types

class AthenaAlcoholMonitoringEquipmentDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringOrderDetailsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringOrderInformationQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringServicesQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringViolationEventsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AlcoholMonitoringContactEventsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringIncidentEventsQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)

class AthenaAlcoholMonitoringVisitDetailsListQuery(
  override val queryString: String,
  override val parameters: Array<String>,
) : AthenaQuery(queryString, parameters)
