package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaDocumentListDTO

data class DocumentList(
  val pageSize: Int,
  val orderDocuments: List<Document>,
) {
  companion object {
    fun createEmpty(): DocumentList = DocumentList(1, listOf())
  }

  constructor(dto: AthenaDocumentListDTO) : this (
    pageSize = dto.pageSize,
    orderDocuments = dto.orderDocuments.map { document ->
      Document(
        name = document.name,
        url = document.url,
        notes = document.notes,
        createdOn = document.createdOn,
        time = document.time,
      )
    },
  )
}
