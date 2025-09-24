package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AthenaAvailabilityRepository

class AthenaAvailabilityServiceTest {
  private lateinit var athenaAvailabilityRepository: AthenaAvailabilityRepository
  private lateinit var service: AthenaAvailabilityService

  @BeforeEach
  fun setup() {
    athenaAvailabilityRepository = mock<AthenaAvailabilityRepository>()
    service = AthenaAvailabilityService(athenaAvailabilityRepository)
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      whenever(athenaAvailabilityRepository.test(false)).thenReturn(true)

      service.checkAvailability(false)

      verify(athenaAvailabilityRepository, times(1)).test(false)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      whenever(athenaAvailabilityRepository.test(false)).thenReturn(true)

      val result = service.checkAvailability(false)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful by not handling error`() {
      val errorMessage = "fake error message"

      whenever(athenaAvailabilityRepository.test(false)).thenThrow(NullPointerException(errorMessage))

      Assertions.assertThatThrownBy { service.checkAvailability(false) }.isInstanceOf(RuntimeException::class.java)
    }
  }
}
