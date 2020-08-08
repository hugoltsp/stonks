package io.github.hugoltsp.stonks.usecase

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.hugoltsp.stonks.data.domain.NewStockCommand
import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.data.repository.StockRepository
import io.github.hugoltsp.stonks.data.resource.StockResource
import io.github.hugoltsp.stonks.infra.domain.Settings
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration

class StockService(
    private val stockResource: StockResource = StockResource,
    private val stockRepository: StockRepository = StockRepository
) {

    fun persist(identifier: String) = cache.get(identifier.toUpperCase()) {
        stockResource.get(identifier.toUpperCase())?.let {
            save(NewStockCommand(it.name, it.price, it.change, it.changePercent))
        }
    }

    fun findByName(identifier: String) = transaction { stockRepository.findByName(identifier.toUpperCase()) }

    private fun save(command: NewStockCommand) = transaction { stockRepository.save(command) }

    private companion object {

        val cache = Caffeine.newBuilder()
            .maximumSize(256)
            .expireAfterWrite(Duration.ofMinutes(Settings.stockCacheEvictionInMinutes))
            .build<String, StockVO>()

    }

}