package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class OrderInformation(
  var keyOrderInformation: OrderDetailsBase,
  val subjectHistoryReport: SubjectHistoryReport,
  val documents: List<Document>,
)
