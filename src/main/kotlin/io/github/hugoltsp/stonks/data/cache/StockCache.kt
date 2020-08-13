package io.github.hugoltsp.stonks.data.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.infra.domain.Settings
import java.time.Duration

object StockCache {

    private val caffeineCache = Caffeine.newBuilder()
        .maximumSize(256)
        .expireAfterWrite(Duration.ofMinutes(Settings.stockCacheEvictionInMinutes))
        .build<String, StockVO>()

    fun get(stockIdentifier: String, mapping: (String) -> StockVO?) = caffeineCache.get(stockIdentifier, mapping)

}