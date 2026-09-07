package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.config

import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.resource.NoResourceFoundException
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@RestControllerAdvice
class HmppsElectronicMonitoringDatastoreApiExceptionHandler {
  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(e: ValidationException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(
      ErrorResponse(
        status = HttpStatus.BAD_REQUEST,
        userMessage = "Validation failure: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.info("Validation exception: {}", e.message) }

  @ExceptionHandler(NoResourceFoundException::class)
  fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.NOT_FOUND)
    .body(
      ErrorResponse(
        status = HttpStatus.NOT_FOUND,
        userMessage = "No resource found failure: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.info("No resource found exception: {}", e.message) }

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.FORBIDDEN)
    .body(
      ErrorResponse(
        status = HttpStatus.FORBIDDEN,
        userMessage = "Forbidden: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.debug("Forbidden (403) returned: {}", e.message) }

  @ExceptionHandler(Exception::class)
  fun handleException(e: Exception): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.INTERNAL_SERVER_ERROR)
    .body(
      ErrorResponse(
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        userMessage = "Unexpected error: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.error("Unexpected exception", e) }

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
    var message = e.message
    if (message.contains("[ValidOrderSearchCriteria.orderSearchCriteria,ValidOrderSearchCriteria]")) {
      message = "This request is malformed, there must be at least one search criteria present"
    }

    return ResponseEntity
      .status(HttpStatus.UNPROCESSABLE_CONTENT)
      .body(
        ErrorResponse(
          status = HttpStatus.UNPROCESSABLE_CONTENT,
          userMessage = "Validation failure: $message",
          developerMessage = message,
        ),
      ).also { log.info("Validation of OrderSearchCriteria exception: {}", message) }
  }

  @ExceptionHandler(InvalidBearerTokenException::class)
  fun handleInvalidBearerTokenException(e: InvalidBearerTokenException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(
      ErrorResponse(
        status = HttpStatus.BAD_REQUEST,
        userMessage = "Invalid bearer token: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.debug("Invalid bearer token: {}", e.message) }

  @ExceptionHandler(MissingRequestHeaderException::class)
  fun handleMissingRequestHeaderException(e: MissingRequestHeaderException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.UNAUTHORIZED)
    .body(
      ErrorResponse(
        status = HttpStatus.UNAUTHORIZED,
        userMessage = "Missing required header '${e.headerName}'",
        developerMessage = e.message,
      ),
    ).also { log.warn("Missing request header: {}", e.headerName) }

  @ExceptionHandler(IllegalArgumentException::class, HttpMessageNotReadableException::class)
  fun handleIllegalArgumentException(e: Exception): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(
      ErrorResponse(
        status = HttpStatus.BAD_REQUEST,
        userMessage = "This request is malformed, and may be missing a body",
        developerMessage = e.message,
      ),
    ).also { log.warn("Message not readable exception: {}", e.message) }

  @ExceptionHandler(HandlerMethodValidationException::class)
  fun handleBadRequestException(e: Exception): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.BAD_REQUEST)
    .body(
      ErrorResponse(
        status = HttpStatus.BAD_REQUEST,
        userMessage = e.message,
        developerMessage = e.message,
      ),
    ).also { log.warn("Bad request exception: {}", e.message) }

  @ExceptionHandler(AthenaClientException::class)
  fun handleException(e: AthenaClientException): ResponseEntity<ErrorResponse> = ResponseEntity
    .status(HttpStatus.INTERNAL_SERVER_ERROR)
    .body(
      ErrorResponse(
        status = HttpStatus.INTERNAL_SERVER_ERROR,
        userMessage = "Athena service error: ${e.message}",
        developerMessage = e.message,
      ),
    ).also { log.error("Unexpected exception from Athena client", e) }

  private companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

class AthenaClientException(message: String) : Exception(message)
