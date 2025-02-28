package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(
  val priority: Int,
) {
  ROLE_EM_DATASTORE_RESTRICTED_RO(
    priority = 20,
  ),
  ROLE_EM_DATASTORE_GENERAL_RO(
    priority = 10,
  ),
  NONE(
    priority = 0,
  ),
}
