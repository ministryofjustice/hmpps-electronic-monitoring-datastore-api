package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import java.time.LocalDateTime

fun nullableLocalDateTime(date: String?, time: String? = "00:00:00"): LocalDateTime? = if (!date.isNullOrEmpty()) LocalDateTime.parse("${date}T$time") else null
