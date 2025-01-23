package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(val iamRole: String) {
  DEV("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev"),
  TEST("arn:aws:iam::396913731313:role/cmt_read_emds_data_test"),
  NONE(""),
  SPECIALS("TBC"),
  GENERAL("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev"),
}
