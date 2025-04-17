package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.integrity

data class IntegrityDocument(
  val name: String,
  val url: String,
  val createdOn: String,
  val time: String,
  val notes: String,
)
