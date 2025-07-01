package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AvailabilityRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.AvailabilityService

class AvailabilityServiceTest {
  private lateinit var availabilityRepository: AvailabilityRepository
  private lateinit var service: AvailabilityService

  @BeforeEach
  fun setup() {
    availabilityRepository = mock<AvailabilityRepository>()
    service = AvailabilityService(availabilityRepository)
  }

  @Nested
  inner class CheckAvailability {
    @Test
    fun `calls listLegacyIds from order repository`() {
      whenever(availabilityRepository.listLegacyIds(false)).thenReturn(listOf("fake-id"))

      service.checkAvailability(false)

      verify(availabilityRepository, times(1)).listLegacyIds(false)
    }

    @Test
    fun `confirms AWS athena is available if successful`() {
      whenever(availabilityRepository.listLegacyIds(false)).thenReturn(listOf("fake-id"))

      val result = service.checkAvailability(false)

      Assertions.assertThat(result).isTrue
    }

    @Test
    fun `confirms AWS athena is unavailable if not successful by not handling error`() {
      val errorMessage = "fake error message"

      whenever(availabilityRepository.listLegacyIds(false)).thenThrow(NullPointerException(errorMessage))

      Assertions.assertThatThrownBy { service.checkAvailability(false) }.isInstanceOf(RuntimeException::class.java)
    }
  }
}
