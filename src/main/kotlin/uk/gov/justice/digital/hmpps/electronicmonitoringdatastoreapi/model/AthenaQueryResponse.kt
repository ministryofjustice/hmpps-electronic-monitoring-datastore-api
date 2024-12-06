package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

data class CustomQueryResponse(
  val queryString: String,
  val athenaRole: String,
  val queryResponse: String? = null,
  val isErrored: Boolean = false,
  val errorMessage: String? = null,
)
