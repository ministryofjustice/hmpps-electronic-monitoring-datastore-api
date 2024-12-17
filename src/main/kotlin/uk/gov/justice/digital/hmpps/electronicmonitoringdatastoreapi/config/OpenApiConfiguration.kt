package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@OpenAPIDefinition(
  info = io.swagger.v3.oas.annotations.info.Info(
    title = "HMPPS Electronic Monitoring Datastore API Documentation",
    description = "A service to view Electronic Monitoring datastore records for historical orders (pre-transition to Serco).",
    license = io.swagger.v3.oas.annotations.info.License(
      name = "The MIT License (MIT)",
      url = "https://github.com/ministryofjustice/court-case-service/blob/main/LICENSE",
    ),
    version = "0.1",
    contact = io.swagger.v3.oas.annotations.info.Contact(
      name = "HMPPS Digital Studio",
      email = "feedback@digital.justice.gov.uk",
    ),
  ),
  security = [SecurityRequirement(name = "hmpps-auth-token")],
  servers = [
    Server(url = "https://electronic-monitoring-datastore-api-dev.hmpps.service.justice.gov.uk", description = "Development"),
    Server(url = "https://electronic-monitoring-datastore-api-preprod.hmpps.service.justice.gov.uk", description = "Pre-Production"),
    Server(url = "https://electronic-monitoring-datastore-api.hmpps.service.justice.gov.uk", description = "Production"),
    Server(url = "http://localhost:8080", description = "Local"),
  ],
)
@SecurityScheme(
  name = "hmpps-auth-token",
  scheme = "bearer",
  bearerFormat = "JWT",
  type = SecuritySchemeType.HTTP,
  `in` = SecuritySchemeIn.HEADER,
  paramName = HttpHeaders.AUTHORIZATION,
  description = "A HMPPS Auth access token with one or both of the `ROLE_EM_DATASTORE_RESTRICTED_RO` or `ROLE_EM_DATASTORE_GENERAL_RO` roles.",
)
@Configuration
class OpenApiConfiguration
