package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole

@Service
class AthenaRoleService {
  fun fromString(name: String): AthenaRole = enumValues<AthenaRole>().find { it.name == name } ?: AthenaRole.DEV

  fun getRoleFromAuthentication(authentication: Authentication): AthenaRole {
    val name: String = "fakefakefake"
    return enumValues<AthenaRole>().find { it.name == name } ?: AthenaRole.DEV
  }
}
