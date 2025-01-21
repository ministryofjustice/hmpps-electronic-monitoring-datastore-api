package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(val iamRole: String) {
  DEV("arn:aws:iam::800964199911:role/cmt_read_emds_data_dev"),
  TEST("arn:aws:iam::396913731313:role/cmt_read_emds_data_test"),
  ;

  companion object {
    fun fromString(name: String): AthenaRole? = enumValues<AthenaRole>().find { it.name == name }
  }
}
