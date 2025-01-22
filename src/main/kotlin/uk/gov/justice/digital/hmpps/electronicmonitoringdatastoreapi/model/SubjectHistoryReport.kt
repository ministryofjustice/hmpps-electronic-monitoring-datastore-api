package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class SubjectHistoryReport(
  val reportUrl: String,
  val name: String,
  val createdOn: String,
  val time: String,
) {
  companion object {
    fun createEmpty(): SubjectHistoryReport = SubjectHistoryReport("", "", "", "")
  }
}
