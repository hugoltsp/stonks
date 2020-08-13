package io.github.hugoltsp.stonks.usecase

import io.github.hugoltsp.stonks.data.cache.StockCache
import io.github.hugoltsp.stonks.data.domain.NewStockCommand
import io.github.hugoltsp.stonks.data.repository.StockRepository
import io.github.hugoltsp.stonks.data.resource.StockResource
import org.jetbrains.exposed.sql.transactions.transaction

class StockService(
    private val stockResource: StockResource = StockResource,
    private val stockRepository: StockRepository = StockRepository,
    private val cache: StockCache = StockCache
) {

    fun persist(identifier: String) = cache.get(identifier.toUpperCase()) {
        stockResource.get(identifier.toUpperCase())?.let {
            save(NewStockCommand(it.name, it.price, it.change, it.changePercent))
        }
    }

    fun findByName(identifier: String) = transaction { stockRepository.findByName(identifier.toUpperCase()) }

    private fun save(command: NewStockCommand) = transaction { stockRepository.save(command) }

}