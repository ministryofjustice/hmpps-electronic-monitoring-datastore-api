package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk").description("Development"),
        Server().url("https://electronic-monitoring-datastore-api-preprod.hmpps.service.justice.gov.uk").description("Pre-Production"),
        Server().url("https://electronic-monitoring-datastore-api.hmpps.service.justice.gov.uk").description("Production"),
        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .info(
      Info().title("HMPPS Electronic Monitoring Datastore Api").version(version)
        .contact(Contact().name("HMPPS Digital Studio").email("feedback@digital.justice.gov.uk")),
    )

  // TODO: Remove the default security schema and start adding your own schemas and roles to describe your
  // service authorisation requirements
//    .components(
//      Components().addSecuritySchemes(
//        "electronic-monitoring-datastore-api-ui-role",
//        SecurityScheme().addBearerJwtRequirement("ROLE_TEMPLATE_KOTLIN__UI"),
//      ),
//    )
//    .addSecurityItem(SecurityRequirement().addList("electronic-monitoring-datastore-api-ui-role", listOf("read")))
}

private fun SecurityScheme.addBearerJwtRequirement(role: String): SecurityScheme = type(SecurityScheme.Type.HTTP)
  .scheme("bearer")
  .bearerFormat("JWT")
  .`in`(SecurityScheme.In.HEADER)
  .name("Authorization")
  .description("A HMPPS Auth access token with the `$role` role.")
