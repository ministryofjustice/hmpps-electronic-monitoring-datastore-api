package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.LocalDate

data class OrderSearchCriteria(
  val searchType: String,
  val legacySubjectId: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val alias: String? = null,
  val dateOfBirth: LocalDate? = null,
)
