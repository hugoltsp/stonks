package io.github.hugoltsp.stonks.data.orm

import org.jetbrains.exposed.dao.id.LongIdTable

object Subscribers : LongIdTable("subscriber") {

    val telegramChatId = Subscribers.long("telegram_user_id").uniqueIndex()

}