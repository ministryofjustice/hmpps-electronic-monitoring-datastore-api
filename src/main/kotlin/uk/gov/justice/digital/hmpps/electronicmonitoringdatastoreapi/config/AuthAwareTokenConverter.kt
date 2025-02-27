package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component

@Component
class AuthAwareTokenConverter(
  @Value("\${services.hmpps-auth.mfa}") private val requireMFA: Boolean = true,
) : Converter<Jwt, AbstractAuthenticationToken> {
  private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> =
    JwtGrantedAuthoritiesConverter()

  override fun convert(jwt: Jwt): AbstractAuthenticationToken {
    if (requireMFA) {
      verifyPassedMFA(jwt)
    }

    val principal = extractPrincipal(jwt)
    val authorities = extractAuthorities(jwt)

    return AuthAwareAuthenticationToken(jwt, principal, authorities)
  }

  @Throws(AuthenticationException::class)
  private fun verifyPassedMFA(jwt: Jwt) {
    val passedMFA = jwt.claims["passed_mfa"] as Boolean?

    if (passedMFA != true) {
      throw InvalidBearerTokenException("Multi-factor authentication must have been used as part of your authentication")
    }
  }

  @Throws(AuthenticationException::class)
  private fun extractPrincipal(jwt: Jwt): String {
    val subject = jwt.subject

    if (subject == null) {
      throw InvalidBearerTokenException("Username is not in token")
    }

    return subject
  }

  private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
    val authorities = mutableListOf<GrantedAuthority>().apply {
      addAll(jwtGrantedAuthoritiesConverter.convert(jwt)!!)
    }

    if (jwt.claims.containsKey(CLAIM_AUTHORITY)) {
      @Suppress("UNCHECKED_CAST")
      val claimAuthorities = (jwt.claims[CLAIM_AUTHORITY] as Collection<String>).toList()
      authorities.addAll(claimAuthorities.map(::SimpleGrantedAuthority))
    }

    if (authorities.isEmpty()) {
      throw InvalidBearerTokenException("No roles in token")
    }

    return authorities.toSet()
  }

  companion object {
    const val CLAIM_AUTHORITY = "authorities"
  }
}
