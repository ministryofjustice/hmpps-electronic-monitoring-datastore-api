package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.hibernate.internal.util.collections.CollectionHelper.toSmallMap
import software.amazon.awssdk.services.athena.model.ResultSet
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class AthenaMapper<T : Any>(val dto: KClass<T>) {
  companion object {
    // do NOT create new ObjectMapper per each request!
    val mapper: ObjectMapper = jacksonObjectMapper()
      .registerKotlinModule()
      .apply {
        propertyNamingStrategy = AlphanumericSnakeCaseStrategy()
      }
  }

  private val strategy = AlphanumericSnakeCaseStrategy()

  fun mapTo(resultSet: ResultSet): List<T> {
    mapper.typeFactory.constructType(dto.java)

    val columnNames = resultSet.resultSetMetadata().columnInfo().map { it.name() }
    val mappedRows = resultSet.rows().drop(1).map { row ->
      row.data().mapIndexed { i, datum ->
        columnNames[i] to datum.varCharValue()
      }.toMap()
    }

    return mappedRows.map { row ->
      mapper.convertValue(row, dto.java)
    }
  }

  fun getColumns(): Array<String> {
    val keys = dto.memberProperties.map { strategy.translate(it.name)!! }.sorted()

    return keys.toTypedArray()
  }

  fun toCriteria(criteria: T): Map<String, Any?> {
    @Suppress("UNCHECKED_CAST")
    return (criteria::class as KClass<T>).memberProperties.associate { prop ->
      strategy.translate(prop.name)!! to prop.get(criteria)?.let { value ->
        if (value::class.isData) {
          toSmallMap<String, Any?>(value as Map<String?, Any?>?)
        } else {
          value
        }
      }
    }
  }
}
