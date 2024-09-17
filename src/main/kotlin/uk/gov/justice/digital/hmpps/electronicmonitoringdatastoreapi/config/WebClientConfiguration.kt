package uk.gov.justice.digital.hmpps.templatepackagename.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.hmpps.kotlin.auth.healthWebClient
import java.time.Duration
// import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
// import uk.gov.justice.hmpps.kotlin.auth.authorisedWebClient

@Configuration
class WebClientConfiguration(
  @Value("\${example-api.url}") val exampleApiBaseUri: String,
  @Value("\${hmpps-auth.url}") val hmppsAuthBaseUri: String,
  @Value("\${api.health-timeout:2s}") val healthTimeout: Duration,
  @Value("\${api.timeout:20s}") val timeout: Duration,
) {
  // HMPPS Auth health ping is required if your service calls HMPPS Auth to get a token to call other services
  // Remove the health ping if no call outs to other services are made
  @Bean
  fun hmppsAuthHealthWebClient(builder: WebClient.Builder): WebClient = builder.healthWebClient(hmppsAuthBaseUri, healthTimeout)

  // This is an example of a health bean for checking other services
  // @Bean
  // fun exampleApiHealthWebClient(builder: WebClient.Builder): WebClient = builder.healthWebClient(exampleApiBaseUri, healthTimeout)

  // This is an example of a bean for calling other services
  // @Bean
  // fun exampleApiWebClient(authorizedClientManager: OAuth2AuthorizedClientManager, builder: WebClient.Builder): WebClient =
  //   builder.authorisedWebClient(authorizedClientManager, registrationId = "example-api", url = exampleApiBaseUri, timeout)
}
