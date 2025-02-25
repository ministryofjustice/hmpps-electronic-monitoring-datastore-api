package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(
  val iamRole: String,
  val priority: Number,
) {
  ROLE_EM_DATASTORE_RESTRICTED_RO("TBC", 1),
  ROLE_EM_DATASTORE_GENERAL_RO("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev", 2),
  TEST("arn:aws:iam::396913731313:role/cmt_read_emds_data_test", 3),
  DEV("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev", 4),
  NONE("", 99),
}
