package uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.repository.caching

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.electronicmonitoringdatastoreapi.model.caching.CacheEntry

@Repository
interface CacheEntryRepository : JpaRepository<CacheEntry, Long> {
  fun findByCacheNameAndCacheKey(cacheName: String, cacheKey: ByteArray): CacheEntry?

  fun deleteByCacheNameAndCacheKey(cacheName: String, cacheKey: ByteArray)

  fun deleteAllByCacheName(cacheName: String)
}
