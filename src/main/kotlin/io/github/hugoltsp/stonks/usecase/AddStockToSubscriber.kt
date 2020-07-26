package io.github.hugoltsp.stonks.usecase

import io.github.hugoltsp.stonks.data.domain.NewStockCommand
import io.github.hugoltsp.stonks.data.resource.StockResource
import io.github.hugoltsp.stonks.infra.extensions.getLogger

class AddStockToSubscriber(
    private val stockService: StockService = StockService,
    private val subscriberService: SubscriberService = SubscriberService,
    private val stockResource: StockResource = StockResource
) {

    fun add(telegramId: Long, stocks: List<String>) {

        val upperCaseStocks = stocks.map(String::toUpperCase).toList()
        val subscriber = subscriberService.findSubscriberByTelegramId(telegramId) ?: subscriberService.save(telegramId)

        logger.info("Adding [{}] to User [{}]", upperCaseStocks, subscriber.id)

        val existingStocks = upperCaseStocks
            .asSequence()
            .map(stockService::findByName)
            .associateBy { it?.name }

        upperCaseStocks.asSequence()
            .map {
                existingStocks.getOrElse(it) {
                    stockResource.get(it)?.run {
                        stockService.save(NewStockCommand(name, price, change, changePercent))
                    }
                }
            }
            .forEach {
                it?.let {
                    logger.info("Adding stock [{}] to subscriber id [{}]", it.id, subscriber.id)
                    subscriberService.addStock(subscriber.id, it.id)
                }
            }

    }

    private companion object {

        val logger = getLogger()

    }

}