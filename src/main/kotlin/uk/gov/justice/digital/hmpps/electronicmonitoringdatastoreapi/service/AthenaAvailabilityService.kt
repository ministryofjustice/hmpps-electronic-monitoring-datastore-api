package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.AthenaAvailabilityRepository

@Service
class AthenaAvailabilityService(
  @field:Autowired val athenaAvailabilityRepository: AthenaAvailabilityRepository,
) {
  fun checkAvailability(restricted: Boolean = false): Boolean = athenaAvailabilityRepository.test(restricted)
}
