package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.aws

data class AthenaProperties(
  val roleGeneral: String? = null,
  val roleRestricted: String? = null,
)
