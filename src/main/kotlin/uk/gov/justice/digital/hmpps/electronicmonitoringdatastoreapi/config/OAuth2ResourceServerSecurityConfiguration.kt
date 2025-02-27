package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
class OAuth2ResourceServerSecurityConfiguration(
  @Autowired private val authAwareTokenConverter: AuthAwareTokenConverter,
) {
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
          jwt.jwtAuthenticationConverter(authAwareTokenConverter)
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
