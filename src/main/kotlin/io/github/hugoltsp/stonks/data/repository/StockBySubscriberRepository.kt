package io.github.hugoltsp.stonks.data.repository

import io.github.hugoltsp.stonks.data.domain.StockSubscriberVO
import io.github.hugoltsp.stonks.data.domain.StockSubscriptionVO
import io.github.hugoltsp.stonks.data.orm.StockBySubscriber
import io.github.hugoltsp.stonks.data.orm.Stocks
import io.github.hugoltsp.stonks.data.orm.Subscribers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object StockBySubscriberRepository {

    fun save(stockId: Long, subscriberId: Long) {

        StockBySubscriber.insert {
            it[stock] = stockId
            it[subscriber] = subscriberId
        }

    }

    fun findByStockIdAndSubscriberId(stockId: Long, subscriberId: Long) =
        StockBySubscriber.select {
            StockBySubscriber.subscriber.eq(subscriberId) and StockBySubscriber.stock.eq(stockId)
        }.singleOrNull()?.let {
            StockSubscriberVO(it[StockBySubscriber.stock], it[StockBySubscriber.subscriber])
        }

    fun findSubscribersByStock() = (StockBySubscriber innerJoin Stocks innerJoin Subscribers).slice(
        StockBySubscriber.subscriber,
        Stocks.name,
        Subscribers.telegramChatId
    ).selectAll().map { StockSubscriptionVO(it[Subscribers.telegramChatId], it[Stocks.name]) }

    fun deleteBySubscriberId(subscriberId: Long) {
        StockBySubscriber.deleteWhere { StockBySubscriber.subscriber eq subscriberId }
    }

}
