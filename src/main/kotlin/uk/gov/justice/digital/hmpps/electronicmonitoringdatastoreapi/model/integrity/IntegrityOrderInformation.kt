package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

data class IntegrityOrderInformation(
  var integrityKeyOrderInformation: IntegrityKeyOrderInformation,
  val integritySubjectHistoryReport: IntegritySubjectHistoryReport,
  val integrityDocuments: List<IntegrityDocument>,
)
