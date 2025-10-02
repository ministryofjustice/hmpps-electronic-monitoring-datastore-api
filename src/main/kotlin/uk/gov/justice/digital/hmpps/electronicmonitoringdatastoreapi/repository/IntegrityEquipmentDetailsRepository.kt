package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.EquipmentDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class IntegrityEquipmentDetailsRepository(athenaClient: EmDatastoreClient) : AthenaRepository<EquipmentDetails>(athenaClient, EquipmentDetails::class)
