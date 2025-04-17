package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.integrity

data class AthenaIntegrityDocumentDTO(
  val name: String,
  val url: String,
  val createdOn: String,
  val time: String,
  val notes: String,
)
