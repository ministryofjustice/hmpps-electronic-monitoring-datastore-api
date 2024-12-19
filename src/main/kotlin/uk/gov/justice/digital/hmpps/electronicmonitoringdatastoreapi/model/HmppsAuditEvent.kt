package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class HmppsAuditEvent(
  val what: String,
  val details: String,
  val who: String,
) {
  @Suppress("ktlint:standard:property-naming")
  val `when`: String = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault()).format(ZonedDateTime.now())
  val service = "electronic-monitoring-datastore-api"
}
