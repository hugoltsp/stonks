package io.github.hugoltsp.stonks.data.repository

import io.github.hugoltsp.stonks.data.domain.SubscriberVO
import io.github.hugoltsp.stonks.data.orm.StockBySubscriber
import io.github.hugoltsp.stonks.data.orm.Subscribers
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.deleteWhere

object SubscriberRepository {

    fun findByTelegramUserId(telegramChatId: Long) =
        SubscriberDAO.find { Subscribers.telegramChatId eq telegramChatId }.firstOrNull()
            ?.run { SubscriberVO(telegramChatId, id.value) }

    fun save(telegramId: Long) = SubscriberDAO.find { Subscribers.telegramChatId eq telegramId }.firstOrNull()?.toVo()
        ?: SubscriberDAO.new {
            telegramChatId = telegramId
        }.toVo()

    fun deleteById(id: Long) {
        Subscribers.deleteWhere { Subscribers.id eq id }
    }

}

internal class SubscriberDAO(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<SubscriberDAO>(Subscribers)

    var telegramChatId by Subscribers.telegramChatId
    var stocks by StockDAO via StockBySubscriber

    fun toVo() = SubscriberVO(telegramChatId, id.value)

}