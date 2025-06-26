package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole

@Service
class AthenaRoleService(
  @param:Value("\${services.athena-roles.restricted:uninitialised}") private val restrictedRole: String,
  @param:Value("\${services.athena-roles.general:uninitialised}") private val generalRole: String,
) {
  fun fromString(name: String): AthenaRole = enumValues<AthenaRole>().find { it.name == name } ?: AthenaRole.NONE

  fun getRoleFromAuthentication(authentication: Authentication): AthenaRole {
    val roleStrings: List<String> = (
      authentication.authorities
        .map { authority -> authority.authority }
        as MutableList<String>
      )

    val mappedRoles: List<AthenaRole> = mapToOrderedUniqueRoles(roleStrings)

    return mappedRoles.firstOrNull() ?: AthenaRole.NONE
  }

  fun mapToOrderedUniqueRoles(roleStrings: List<String>): List<AthenaRole> = roleStrings
    .map { roleString ->
      enumValues<AthenaRole>()
        .find { it.name == roleString } ?: AthenaRole.NONE
    }.toSet().sortedByDescending { it.priority }

  fun getIamRole(athenaRole: AthenaRole): String = when (athenaRole.name) {
    "ROLE_EM_DATASTORE_RESTRICTED_RO" -> restrictedRole
    "ROLE_EM_DATASTORE_GENERAL_RO" -> generalRole
    else -> ""
  }
}

// Example user token:
    /*{
      "jti": "9b05e5ef-7ed5-48d3-bad3-76e900502d72",
      "sub": "JJACKSON_GEN",
      "authorities": "ROLE_EM_DATASTORE_RESTRICTED_RO,ROLE_EM_DATASTORE_GENERAL_RO,ROLE_PRISON",
      "name": "Jasper Jackson",
      "auth_source": "nomis",
      "user_id": "487331",
      "passed_mfa": false,
      "exp": 1737689911
    }*/
