package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class OrderInformation(
  var keyOrderInformation: IKeyOrderInformation,
  val subjectHistoryReport: SubjectHistoryReport,
  val documents: List<Document>,
)
