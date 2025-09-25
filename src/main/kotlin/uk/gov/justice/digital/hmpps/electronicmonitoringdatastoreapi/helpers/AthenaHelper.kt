package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import software.amazon.awssdk.services.athena.model.ResultSet
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class AthenaHelper {
  companion object {
    // do NOT create new ObjectMapper per each request!
    val mapper: ObjectMapper = jacksonObjectMapper()
      .registerKotlinModule()
      .apply {
        propertyNamingStrategy = AlphanumericSnakeCaseStrategy()
      }

    // NOTE: This SNAKE_CASE strategy also adds underscores before numbers which we need
    class AlphanumericSnakeCaseStrategy : PropertyNamingStrategies.NamingBase() {
      override fun translate(input: String?): String? = if (input == null) {
        null
      } else {
        "([A-Z]+|[0-9]+)".toRegex()
          .replace(input.replace("'", "")) { "_${it.groupValues[1]}".lowercase() }
          .replace("^_".toRegex(), "")
      }
    }

    inline fun <reified T> mapTo(resultSet: ResultSet): List<T> {
      mapper.typeFactory.constructType(T::class.java)

      val columnNames = resultSet.resultSetMetadata().columnInfo().map { it.name() }
      val mappedRows = resultSet.rows().drop(1).map { row ->
        row.data().mapIndexed { i, datum ->
          columnNames[i] to datum.varCharValue()
        }.toMap()
      }

      return mappedRows.map { row ->
        mapper.convertValue(row, T::class.java)
      }
    }

    inline fun <reified T : Any> toColumns(dto: KClass<T>): Array<String> {
      val strategy = AlphanumericSnakeCaseStrategy()
      val keys = dto.memberProperties.map { strategy.translate(it.name)!! }.sorted()

      return keys.toTypedArray()
    }
  }
}
