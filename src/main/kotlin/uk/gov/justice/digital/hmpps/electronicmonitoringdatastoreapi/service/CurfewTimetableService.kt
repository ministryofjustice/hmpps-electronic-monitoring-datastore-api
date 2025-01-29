package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.CurfewTimetable
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.CurfewTimetableRepository

@Service
class CurfewTimetableService(
  @Autowired val curfewTimetableRepository: CurfewTimetableRepository,
) {
  fun getCurfewTimetable(orderId: String, role: AthenaRole): List<CurfewTimetable> {
    val result = curfewTimetableRepository.getCurfewTimetable(orderId, role)

    return result.map { item -> CurfewTimetable(item) }
  }
}
