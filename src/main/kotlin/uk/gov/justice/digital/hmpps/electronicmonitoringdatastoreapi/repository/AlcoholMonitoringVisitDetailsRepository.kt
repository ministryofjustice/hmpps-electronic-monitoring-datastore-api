package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.AmVisitDetails
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class AlcoholMonitoringVisitDetailsRepository(athenaClient: EmDatastoreClient) : AthenaRepository<AmVisitDetails>(athenaClient, AmVisitDetails::class)
