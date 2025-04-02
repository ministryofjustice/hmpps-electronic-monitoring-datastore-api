package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service.alcoholMonitoring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.alcoholMonitoring.AmService
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.alcoholMonitoring.AmServicesRepository

@Service
class AmServicesService(
  @Autowired val amServicesRepository: AmServicesRepository,
) {
  fun getServices(legacySubjectId: String, role: AthenaRole): List<AmService> {
    val result = amServicesRepository.getServices(legacySubjectId, role)

    return result.map { item -> AmService(item) }
  }
}
