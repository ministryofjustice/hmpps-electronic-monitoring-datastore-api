package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.ServiceDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.ServiceDetailsRepository

@Service
class ServiceDetailsService(
  @Autowired val serviceDetailsRepository: ServiceDetailsRepository,
) {
  fun getServiceDetails(legacySubjectId: String, role: AthenaRole): List<ServiceDetails> {
    val result = serviceDetailsRepository.getServiceDetails(legacySubjectId, role)

    return result.map { item -> ServiceDetails(item) }
  }
}
