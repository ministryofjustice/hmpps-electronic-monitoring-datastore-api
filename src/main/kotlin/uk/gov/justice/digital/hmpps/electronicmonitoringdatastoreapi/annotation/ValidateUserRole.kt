package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.annotation

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ValidateUserRole(val targetHeader: String, val requiredRole: String)

// @Configuration
// class WebConfiguration : WebMvcConfigurer {
//
//  override fun addInterceptors(registry: InterceptorRegistry) {
//    registry.addInterceptor(ValidateUserRoleInterceptor())
//  }
// }

@Component
class ValidateUserRoleInterceptor : HandlerInterceptor {

  override fun preHandle(
    request: HttpServletRequest,
    response: HttpServletResponse,
    handler: Any,
  ): Boolean {
    if (handler is HandlerMethod) {
      // Check for annotation at method
      val methodAnnotation = handler.getMethodAnnotation(ValidateUserRole::class.java)

      // Check for annotation at class
      val classAnnotation = handler.beanType.getAnnotation(ValidateUserRole::class.java)

      val annotation: ValidateUserRole = methodAnnotation ?: classAnnotation

      // run auth logic
      annotation?.let {
        val header = request.getHeader(it.targetHeader)

        if (header.isNullOrBlank()) {
          response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing required header: ${it.targetHeader}")
          return false
        }

        // Validate the JWT here
      }
    }
    return true
  }
}
