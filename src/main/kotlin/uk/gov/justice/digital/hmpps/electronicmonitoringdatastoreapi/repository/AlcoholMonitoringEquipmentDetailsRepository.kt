package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmEquipmentDetails

@Service
class AlcoholMonitoringEquipmentDetailsRepository(athenaClient: EmDatastoreClient) : AthenaRepository<AmEquipmentDetails>(athenaClient, AmEquipmentDetails::class)
