package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class SearchCriteria(
  val searchType: String? = null,
  val legacySubjectId: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val alias: String? = null,
  val dobDay: String? = null,
  val dobMonth: String? = null,
  val dobYear: String? = null,
)
