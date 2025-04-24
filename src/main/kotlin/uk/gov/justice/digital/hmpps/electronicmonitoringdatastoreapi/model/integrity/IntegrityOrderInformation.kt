package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

data class IntegrityOrderInformation(
  var keyOrderInformation: IntegrityKeyOrderInformation,
  val subjectHistoryReport: IntegritySubjectHistoryReport,
  val documents: List<IntegrityDocument>,
)
