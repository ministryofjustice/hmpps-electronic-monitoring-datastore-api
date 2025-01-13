package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.athena

data class AthenaQuery<T>(
  val queryString: String,
) {
  inline fun <reified T> responseType(): Class<T> = T::class.java
}
