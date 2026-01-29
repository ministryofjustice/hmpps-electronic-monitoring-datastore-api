package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.tags.Tag
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

const val TOKEN_HMPPS_AUTH = "hmpps-electronic-monitoring-datastore-data-role"

const val TAG_ALCOHOL_ORDERS = "Alcohol monitoring orders"
const val TAG_INTEGRITY_ORDERS = "Integrity monitoring orders"
const val TAG_CONNECTIVITY = "Connection testing"

const val ROLE_EM_DATASTORE_GENERAL__RO = "ROLE_EM_DATASTORE_GENERAL_RO"
const val ROLE_EM_DATASTORE_RESTRICTED__RO = "ROLE_EM_DATASTORE_RESTRICTED_RO"

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version!!

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(serviceServers())
    .info(apiInfo())
    .components(
      Components()
        .addSecuritySchemes(
          TOKEN_HMPPS_AUTH,
          SecurityScheme()
            .addBearerJwtRequirement(ROLE_EM_DATASTORE_GENERAL__RO)
            .description("A HMPPS Auth access token with the `$ROLE_EM_DATASTORE_GENERAL__RO` or `$ROLE_EM_DATASTORE_RESTRICTED__RO` roles."),
        ),
    )
    .addSecurityItem(SecurityRequirement().addList(TOKEN_HMPPS_AUTH))
    .tags(apiTags())

  private fun serviceServers(): List<Server> = listOf(
    Server()
      .url("https://electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk")
      .description("Development"),
    Server()
      .url("https://electronic-monitoring-datastore-api-preprod.hmpps.service.justice.gov.uk")
      .description("Pre-Production"),
    Server()
      .url("https://electronic-monitoring-datastore-api.hmpps.service.justice.gov.uk")
      .description("Production"),
    Server()
      .url("http://localhost:8080")
      .description("Local development environment"),
  )

  private fun apiInfo(): Info = Info()
    .title("HMPPS Electronic Monitoring contract management information API")
    .version(version)
    .description(apiDescription())
    .contact(apiContact())
    .license(apiLicense())

  private fun apiDescription(): String = """
    |API for retrieving information and events related to electronic monitoring orders for historical orders (pre-transition to Serco).
    |
    |## Authentication
    |This API uses **OAuth2 with JWTs**. Pass your JWT in the `Authorization` header using the `Bearer` scheme.
    |**Important:** This service is designed for client tokens only; user tokens should not be used.
    |
    |## Authorization
    |Access to API endpoints is controlled by roles. The required roles are documented with each endpoint.
    |Integrations should request one of the following roles based on their needs:
    |
    |* `ROLE_EM_DATASTORE_GENERAL__RO`: Grants **read-only access** to general access monitoring order information (e.g., searching order details, retrieving monitoring event).
    |* `ROLE_EM_DATASTORE_RESTRICTED__RO`: Grants **read-only access** to restricted access monitoring order information (e.g., searching order details, retrieving monitoring event).
  """.trimMargin()

  private fun apiContact(): Contact = Contact()
    .name("HMPPS Digital Studio")
    .email("feedback@digital.justice.gov.uk")

  private fun apiLicense(): License = License()
    .name("The MIT License (MIT)")
    .url("https://github.com/ministryofjustice/hmpps-electronic-monitoring-datastore-api/blob/main/LICENSE")

  private fun SecurityScheme.addBearerJwtRequirement(role: String): SecurityScheme = type(SecurityScheme.Type.HTTP)
    .scheme("bearer")
    .bearerFormat("JWT")
    .`in`(SecurityScheme.In.HEADER)
    .name(HttpHeaders.AUTHORIZATION)
    .description("A HMPPS Auth access token with the `$ROLE_EM_DATASTORE_GENERAL__RO` or `$ROLE_EM_DATASTORE_RESTRICTED__RO` roles.")

  private fun apiTags(): List<Tag> = listOf(
    Tag()
      .name(TAG_INTEGRITY_ORDERS)
      .description("Provides access to Integrity monitoring order information."),
    Tag()
      .name(TAG_ALCOHOL_ORDERS)
      .description("Provides access to Alcohol monitoring order information."),
    Tag()
      .name(TAG_CONNECTIVITY),
  )
}
