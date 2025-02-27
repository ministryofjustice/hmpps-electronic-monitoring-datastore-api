package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(
  val iamRole: String,
  val priority: Int,
) {
  DEV("arn:aws:iam::396913731313:role/cmt_read_emds_data_test", 5),

/*  TODO: Currently the 'restricted' role is the same as the non-restricted role.
*    We will need to replace this with the IAM role that is allowed to access Specials data
*    This is dependent on the security being in place and approved for that change
* */
  ROLE_EM_DATASTORE_RESTRICTED_RO(
    "arn:aws:iam::396913731313:role/cmt_read_emds_data_test",
    priority = 20,
  ),
  ROLE_EM_DATASTORE_GENERAL_RO(
    "arn:aws:iam::396913731313:role/cmt_read_emds_data_test",
    priority = 10,
  ),
  NONE(
    "",
    priority = 0,
  ),
}
