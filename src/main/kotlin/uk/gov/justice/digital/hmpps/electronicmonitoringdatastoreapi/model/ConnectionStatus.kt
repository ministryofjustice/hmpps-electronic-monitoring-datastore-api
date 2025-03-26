package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import io.swagger.v3.oas.annotations.media.Schema

data class ConnectionStatus(
  @Schema(example = "Connection successful", required = true, description = "A message describing the connection available")
  val message: String,
)
