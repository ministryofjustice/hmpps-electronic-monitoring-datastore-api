package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component

@Component
data class MfaFlag(
  @Value("\${services.hmpps-auth.mfa}") public val requireMFA: String = "true",
)

class AuthAwareAuthenticationToken(
  jwt: Jwt,
  private val aPrincipal: String,
  authorities: Collection<GrantedAuthority>,
  private val passedMfa: Boolean,
) : JwtAuthenticationToken(jwt, authorities, aPrincipal),
  Authentication {
  override fun getPrincipal(): String = aPrincipal
  fun passedMFA(): Boolean = passedMfa
}
