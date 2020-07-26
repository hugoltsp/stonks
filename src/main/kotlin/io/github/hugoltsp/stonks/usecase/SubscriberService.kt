package io.github.hugoltsp.stonks.usecase

import io.github.hugoltsp.stonks.data.repository.StockBySubscriberRepository
import io.github.hugoltsp.stonks.data.repository.SubscriberRepository
import org.jetbrains.exposed.sql.transactions.transaction

object SubscriberService {

    private val subscriberRepository = SubscriberRepository
    private val stockBySubscriberRepository = StockBySubscriberRepository

    fun findSubscriberByTelegramId(subscriberChatId: Long) = transaction {
        subscriberRepository.findByTelegramUserId(subscriberChatId)
    }

    fun findSubscriptions() = transaction { stockBySubscriberRepository.findSubscribersByStock() }

    fun removeSubscriptionByTelegramChatId(telegramChatId: Long) {
        findSubscriberByTelegramId(telegramChatId)?.let {
            transaction {
                stockBySubscriberRepository.deleteBySubscriberId(it.id)
                subscriberRepository.deleteById(it.id)
            }
        }
    }

    fun save(telegramChatId: Long) = transaction { subscriberRepository.save(telegramChatId) }

    fun addStock(subscriberId: Long, stockId: Long) {
        transaction {
            if (stockBySubscriberRepository.findByStockIdAndSubscriberId(stockId, subscriberId) == null) {
                stockBySubscriberRepository.save(stockId, subscriberId)
            }
        }
    }

}