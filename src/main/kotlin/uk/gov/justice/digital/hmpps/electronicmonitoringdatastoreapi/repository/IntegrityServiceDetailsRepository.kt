package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.EmDatastoreClient
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.entity.Services
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.athena.AthenaRepository

@Service
class IntegrityServiceDetailsRepository(athenaClient: EmDatastoreClient) : AthenaRepository<Services>(athenaClient, Services::class)
