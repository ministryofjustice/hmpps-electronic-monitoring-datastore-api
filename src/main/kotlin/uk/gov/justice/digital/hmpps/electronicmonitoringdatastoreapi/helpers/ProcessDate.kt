package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import java.time.LocalDateTime

fun processDate(date: String?): LocalDateTime? = if (!date.isNullOrEmpty()) LocalDateTime.parse("${date}T00:00:00") else null
