package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaDocumentListDTO(
  val pageSize: Int,
  val orderDocuments: List<DocumentDTO>,
)

data class DocumentDTO(
  val name: String,
  val url: String,
  val createdOn: String,
  val time: String,
  val notes: String,
)