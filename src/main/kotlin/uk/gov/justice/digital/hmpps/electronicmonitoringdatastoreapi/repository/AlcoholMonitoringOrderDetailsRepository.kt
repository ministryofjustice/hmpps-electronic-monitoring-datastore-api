package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmOrderDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class AlcoholMonitoringOrderDetailsRepository(athenaClient: EmDatastoreClient) : AthenaRepository<AmOrderDetails>(athenaClient, AmOrderDetails::class)
