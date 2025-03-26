package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model

import io.swagger.v3.oas.annotations.media.Schema

data class OrderSearchCriteria(
  @Schema(example = "am", description = "The type of search being performed")
  val searchType: String? = null,

  @Schema(example = "1234567", description = "The ID or part for the subject of the order to find")
  val legacySubjectId: String? = null,

  @Schema(example = "J", description = "A first name or part for the subject of the order to find")
  val firstName: String? = null,

  @Schema(example = "Spr", description = "A last name or part for the subject of the order to find")
  val lastName: String? = null,

  @Schema(example = "Jack", description = "An alias or part for the subject of the order to find")
  val alias: String? = null,

  @Schema(example = "05", description = "A birth day for the subject of the order to find")
  val dobDay: String? = null,

  @Schema(example = "03", description = "A birth month for the subject of the order to find")
  val dobMonth: String? = null,

  @Schema(example = "1989", description = "A birth year for the subject of the order to find")
  val dobYear: String? = null,
)
