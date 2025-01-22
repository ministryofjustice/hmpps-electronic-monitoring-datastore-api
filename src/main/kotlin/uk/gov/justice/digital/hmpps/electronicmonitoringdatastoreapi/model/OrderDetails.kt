package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaOrderDetailsDTO

data class OrderDetails(
  val specials: String,
  val legacySubjectId: String,
) {
  constructor(dto: AthenaOrderDetailsDTO) : this (
    specials = "no",
    legacySubjectId = dto.legacySubjectId,
  )

  companion object {
    fun createEmpty(): OrderDetails = OrderDetails("", "")
  }
}

// TODO: get fields from UI
