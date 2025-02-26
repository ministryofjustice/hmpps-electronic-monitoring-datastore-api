package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class OAuth2ResourceServerSecurityConfiguration {
  @Bean
  @Throws(Exception::class)
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    http
      .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }
      .authorizeHttpRequests { auth ->
        auth
          .requestMatchers("/health/**").permitAll()
          .requestMatchers("/info").permitAll()
          .requestMatchers("/swagger-ui.html").permitAll()
          .requestMatchers("/swagger-ui/**").permitAll()
          .requestMatchers("/v3/api-docs/**").permitAll()
          .anyRequest().authenticated()
      }
      .anonymous { anonymous ->
        anonymous.disable()
      }
      .oauth2ResourceServer { oauth2ResourceServer ->
        oauth2ResourceServer.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        oauth2ResourceServer.jwt { jwt ->
          jwt.jwtAuthenticationConverter(
            AuthAwareTokenConverter(),
          )
        }
      }
      .sessionManagement { sess: SessionManagementConfigurer<HttpSecurity?> ->
        sess.sessionCreationPolicy(
          SessionCreationPolicy.STATELESS,
        )
      }

    return http.build()
  }
}

class AuthAwareTokenConverter : Converter<Jwt, AbstractAuthenticationToken> {
  private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> =
    JwtGrantedAuthoritiesConverter()

  override fun convert(jwt: Jwt): AbstractAuthenticationToken {
    validatePassedMFA(jwt)

    val principal = extractPrincipal(jwt)
    val authorities = extractAuthorities(jwt)

    return AuthAwareAuthenticationToken(jwt, principal, authorities)
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

  @Throws(AuthenticationException::class)
  private fun validatePassedMFA(jwt: Jwt) {
    val passedMFA = jwt.claims["passed_mfa"] as Boolean?

    if (passedMFA != true) {
      throw InvalidBearerTokenException("MFA was '$passedMFA' which is not true")
    }
  }

  companion object {
    const val CLAIM_AUTHORITY = "authorities"
  }
}

class AuthAwareAuthenticationToken(
  jwt: Jwt,
  private val aPrincipal: String,
  authorities: Collection<GrantedAuthority>,
) : JwtAuthenticationToken(jwt, authorities, aPrincipal) {
  override fun getPrincipal(): String = aPrincipal
}
