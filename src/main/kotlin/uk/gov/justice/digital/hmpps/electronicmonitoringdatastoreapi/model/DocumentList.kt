package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class DocumentList(
  val pageSize: Int,
  val orderDocuments: List<Document>,
) {
  companion object {
    fun createEmpty(): DocumentList = DocumentList(1, listOf())
  }
}
