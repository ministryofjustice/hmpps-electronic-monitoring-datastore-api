package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class Order(
  val dataType: String,
  val legacySubjectId: Long,
  val name: String,
  val address: String,
  val alias: String?,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
) {
  constructor(dto: AthenaOrderDTO) : this(
    dataType = "am",
    legacySubjectId = dto.legacySubjectId,
    name = dto.fullName,
    address = StringBuilder()
      .append(dto.primaryAddressLine1)
      .append(dto.primaryAddressLine2)
      .append(dto.primaryAddressLine3)
      .append(dto.primaryAddressPostCode)
      .toString(),
    alias = null,
    dateOfBirth = dto.dateOfBirth,
    orderStartDate = dto.orderStartDate,
    orderEndDate = dto.orderEndDate,
  )
}

data class AthenaOrderDTO(
  val legacySubjectId: Long,
  val fullName: String,
  val primaryAddressLine1: String,
  val primaryAddressLine2: String,
  val primaryAddressLine3: String,
  val primaryAddressPostCode: String,
  val dateOfBirth: String,
  val orderStartDate: String,
  val orderEndDate: String,
)
