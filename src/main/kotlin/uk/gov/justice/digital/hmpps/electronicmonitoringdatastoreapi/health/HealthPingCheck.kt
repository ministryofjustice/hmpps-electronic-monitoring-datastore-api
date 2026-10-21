@file:Suppress("ktlint:standard:filename")

package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.health

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.health.contributor.Health
import org.springframework.boot.health.contributor.HealthIndicator
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.hmpps.kotlin.health.HealthPingCheck

abstract class AthenaHealthPingCheck(private val emDatastoreClient: EmDatastoreClient) : HealthIndicator {
  override fun health(): Health = emDatastoreClient.ping()
    .block() ?: Health.down().withDetail("HttpStatus", "No response returned from ping").build()
}

// HMPPS Auth health ping is required if your service calls HMPPS Auth to get a token to call other services
@Component("hmppsAuth")
class HmppsAuthHealthPing(@Qualifier("hmppsAuthHealthWebClient") webClient: WebClient) : HealthPingCheck(webClient)

@Component("hmppsElectronicMonitoringDatastore")
class HmppsElectronicMonitoringDatastoreHealthPing(athenaClient: EmDatastoreClient) : AthenaHealthPingCheck(athenaClient)
