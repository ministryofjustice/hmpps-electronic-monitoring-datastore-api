package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repositories.AvailabilityRepository

@Service
class AvailabilityService(
  @field:Autowired val availabilityRepository: AvailabilityRepository,
) {
  fun checkAvailability(restricted: Boolean = false): Boolean = availabilityRepository.listLegacyIds(restricted).count() > 0
}
