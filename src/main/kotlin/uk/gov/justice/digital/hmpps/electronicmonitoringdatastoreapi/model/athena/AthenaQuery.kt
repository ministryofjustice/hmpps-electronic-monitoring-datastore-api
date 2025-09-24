package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

import java.io.Serializable

class AthenaQuery(
  val queryString: String,
  val parameters: Array<String>,
) : Serializable
