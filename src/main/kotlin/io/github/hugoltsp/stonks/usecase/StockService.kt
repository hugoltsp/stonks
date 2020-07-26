package io.github.hugoltsp.stonks.usecase

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.hugoltsp.stonks.data.domain.NewStockCommand
import io.github.hugoltsp.stonks.data.domain.StockVO
import io.github.hugoltsp.stonks.data.repository.StockRepository
import io.github.hugoltsp.stonks.data.resource.StockResource
import io.github.hugoltsp.stonks.infra.domain.Settings
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Duration

object StockService {

    private val cache = Caffeine.newBuilder()
        .maximumSize(256)
        .expireAfterWrite(Duration.ofMinutes(Settings.stockCacheEvictionInMinutes))
        .build<String, StockVO>()

    private val stockResource: StockResource = StockResource
    private val stockRepository: StockRepository = StockRepository

    fun findByIdentifier(identifier: String) = cache.get(identifier.toUpperCase()) {
        stockResource.get(identifier.toUpperCase())?.let {
            transaction {
                stockRepository.save(NewStockCommand(it.name, it.price, it.change, it.changePercent))
            }
        }
    }

    fun findByName(identifier: String) = transaction { stockRepository.findByName(identifier.toUpperCase()) }

    fun save(command: NewStockCommand) = transaction { stockRepository.save(command) }

}