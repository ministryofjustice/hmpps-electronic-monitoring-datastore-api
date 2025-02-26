package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException

class AuthAwareTokenConverterTest {
  @Test
  fun `Can be instantiated`() {
    val sut = AuthAwareTokenConverter()
    Assertions.assertThat(sut).isNotNull()
  }

  @Test
  fun `Verifies a valid token`() {
    val sut = AuthAwareTokenConverter()
    val jwt = Jwt.withTokenValue("token")
      .header("alg", "none")
      .claim("sub", "user")
      .claim("passed_mfa", true)
      .claim("authorities", listOf("SCOPE_read"))
      .build()

    val token = sut.convert(jwt)

    Assertions.assertThat(token).isNotNull()
  }

  @Nested
  inner class VerifyPassedMFA {
    @Test
    fun `Verifies MFA was passed if true`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("passed_mfa", true)
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      val token = sut.convert(jwt)

      Assertions.assertThat(token).isNotNull()
    }

    @Test
    fun `Verifies MFA was not passed if false`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("passed_mfa", false)
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      Assertions.assertThatExceptionOfType(InvalidBearerTokenException::class.java).isThrownBy {
        sut.convert(jwt)
      }.withMessage("Multi-factor authentication must have been used as part of your authentication")
    }

    @Test
    fun `Verifies MFA was not passed if not present`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      Assertions.assertThatExceptionOfType(InvalidBearerTokenException::class.java).isThrownBy {
        sut.convert(jwt)
      }.withMessage("Multi-factor authentication must have been used as part of your authentication")
    }
  }

  @Nested
  inner class ExtractPrincipal {
    @Test
    fun `Extracts principal if passed`() {
      val expectedPrincipal = "barney rubble"

      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", expectedPrincipal)
        .claim("passed_mfa", true)
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      val token = sut.convert(jwt)

      Assertions.assertThat(token.principal).isEqualTo(expectedPrincipal)
    }

    @Test
    fun `Throws error if subject not present`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("passed_mfa", true)
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      Assertions.assertThatExceptionOfType(InvalidBearerTokenException::class.java).isThrownBy {
        sut.convert(jwt)
      }.withMessage("Username is not in token")
    }
  }

  @Nested
  inner class ExtractAuthorities {
    @Test
    fun `Extracts authorities if passed`() {
      val authorities = listOf("SCOPE_read")
      val expectedAuthorities = authorities.map(::SimpleGrantedAuthority)

      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("passed_mfa", true)
        .claim("authorities", listOf("SCOPE_read"))
        .build()

      val token = sut.convert(jwt)

      Assertions.assertThat(token.authorities).isEqualTo(expectedAuthorities)
    }

    @Test
    fun `Throws error if authorities not present`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("passed_mfa", true)
        .build()

      Assertions.assertThatExceptionOfType(InvalidBearerTokenException::class.java).isThrownBy {
        sut.convert(jwt)
      }.withMessage("No roles in token")
    }

    @Test
    fun `Throws error if authorities not a collection`() {
      val sut = AuthAwareTokenConverter()
      val jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("sub", "user")
        .claim("passed_mfa", true)
        .claim("authorities", "SCOPE_read")
        .build()

      Assertions.assertThatExceptionOfType(ClassCastException::class.java).isThrownBy {
        sut.convert(jwt)
      }
    }
  }
}
