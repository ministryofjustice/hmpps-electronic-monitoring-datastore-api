package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class OrderSearchCriteria(
  val searchType: String,
  val legacySubjectId: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val alias: String? = null,
  val dobDay: String? = null,
  val dobMonth: String? = null,
  val dobYear: String? = null,
)
