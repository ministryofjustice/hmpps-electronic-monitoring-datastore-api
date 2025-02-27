package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class AuthAwareAuthenticationToken(
  jwt: Jwt,
  private val aPrincipal: String,
  authorities: Collection<GrantedAuthority>,
) : JwtAuthenticationToken(jwt, authorities, aPrincipal) {
  val passedMFA: Boolean? = jwt.claims["passed_mfa"] as Boolean?
  override fun getPrincipal(): String = aPrincipal
}
