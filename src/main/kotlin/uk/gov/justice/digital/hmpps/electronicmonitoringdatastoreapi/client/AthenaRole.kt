package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client

enum class AthenaRole(
  val priority: Int,
) {
  ROLE_EM_DATASTORE_RESTRICTED__RO(
    priority = 20,
  ),
  ROLE_EM_DATASTORE_GENERAL__RO(
    priority = 10,
  ),
  NONE(
    priority = 0,
  ),
}
