package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaCurfewTimetableDTO(
  val legacySubjectId: String,
  val legacyOrderId: String,
  val serviceId: Int,
  val serviceAddress1: String?,
  val serviceAddress2: String?,
  val serviceAddress3: String?,
  val serviceAddressPostcode: String?,
  val serviceStartDate: String?,
  val serviceEndDate: String?,
  val curfewStartDate: String?,
  val curfewEndDate: String?,
  val curfewStartTime: String?,
  val curfewEndTime: String?,
  val monday: Int,
  val tuesday: Int,
  val wednesday: Int,
  val thursday: Int,
  val friday: Int,
  val saturday: Int,
  val sunday: Int,
)
