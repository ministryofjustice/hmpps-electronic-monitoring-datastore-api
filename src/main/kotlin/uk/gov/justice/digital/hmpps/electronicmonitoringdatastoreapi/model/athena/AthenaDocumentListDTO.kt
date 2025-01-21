package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaDocumentListDTO(
  val pageSize: Int,
  val orderDocuments: List<AthenaDocumentDTO>,
)
