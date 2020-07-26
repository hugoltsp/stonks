package io.github.hugoltsp.stonks.usecase

import io.github.hugoltsp.stonks.usecase.domain.StockOutput

class RetrieveStockByIdentifier(
    private val stockService: StockService = StockService
) {

    fun retrieve(stockIdentifier: String): StockOutput? {

        val stockVO = stockService.findByIdentifier(stockIdentifier)

        if (stockVO != null) {
            return StockOutput.from(stockVO)
        }

        return null
    }

}