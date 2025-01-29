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
  fun getServices(orderId: String, role: AthenaRole): List<CurfewTimetable> {
    val result = curfewTimetableRepository.getServicesList(orderId, role)

    return result.items.map { item -> CurfewTimetable(item) }
  }
}
