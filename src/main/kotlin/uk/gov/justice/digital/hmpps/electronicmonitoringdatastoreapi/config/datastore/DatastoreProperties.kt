package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config.datastore

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "datastore")
data class DatastoreProperties(
  val database: String,
  val outputBucketArn: String,
  val retryIntervalMs: Long = 1000,
)
