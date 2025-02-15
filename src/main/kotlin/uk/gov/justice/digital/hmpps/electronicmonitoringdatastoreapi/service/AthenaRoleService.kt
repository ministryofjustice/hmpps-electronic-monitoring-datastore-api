package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.service

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.client.AthenaRole

@Service
class AthenaRoleService {
  fun fromString(name: String): AthenaRole = enumValues<AthenaRole>().find { it.name == name } ?: AthenaRole.DEV

  fun getRoleFromAuthentication(authentication: Authentication): AthenaRole {
    // TODO: Access passed_MFA which should be in the token, return NONE if so. Need to find it though.
    // val passedMFA: Boolean = authentication.details.GET_THE_DATA
    // if(!passedMFA) return AthenaRole.NONE

    return enumValues<AthenaRole>().find { it.name == "TODO!" } ?: AthenaRole.DEV
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
