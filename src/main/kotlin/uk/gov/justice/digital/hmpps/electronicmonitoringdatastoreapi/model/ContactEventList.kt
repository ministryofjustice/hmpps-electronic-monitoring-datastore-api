package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena.AthenaContactEventListDTO
import java.time.LocalDateTime

data class ContactEventList(
  val pageSize: Int,
  val events: List<Event<ContactEventDetails>>,
) {
  constructor(dto: AthenaContactEventListDTO) : this (
    pageSize = dto.pageSize,
    events = dto.events.map { event ->
      Event<ContactEventDetails>(
        legacyOrderId = event.legacyOrderId,
        legacySubjectId = event.legacySubjectId,
        type = "Contact",
        dateTime = LocalDateTime.parse("${event.contactDate}T${event.contactTime}"),
        details = ContactEventDetails(
          outcome = event.outcome,
          contactType = event.contactType,
          reason = event.reason,
          channel = event.channel,
          userId = event.userId,
          userName = event.userName,
          modifiedDateTime = LocalDateTime.parse("${event.modifiedDate}T${event.modifiedTime}"),
        ),
      )
    },
  )
}
