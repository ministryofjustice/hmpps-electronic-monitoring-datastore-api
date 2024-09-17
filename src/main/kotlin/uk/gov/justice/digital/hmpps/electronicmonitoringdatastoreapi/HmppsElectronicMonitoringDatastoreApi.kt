package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HmppsElectronicMonitoringDatastoreApi

fun main(args: Array<String>) {
  runApplication<HmppsElectronicMonitoringDatastoreApi>(*args)
}
