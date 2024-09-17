package uk.gov.justice.digital.hmpps.templatepackagename

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HmppsEmDatastoreApi

fun main(args: Array<String>) {
  runApplication<HmppsEmDatastoreApi>(*args)
}
