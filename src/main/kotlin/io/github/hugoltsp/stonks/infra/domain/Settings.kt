package io.github.hugoltsp.stonks.infra.domain

import io.github.cdimascio.dotenv.dotenv
import java.util.*

object Settings {

    private val dotenv = dotenv {
        ignoreIfMissing = true
        ignoreIfMalformed = true
    }

    val telegramToken = dotenv["TELEGRAM_TOKEN"]!!
    val jdbcUrl = dotenv["JDBC_URL"]!!
    val databaseUser = dotenv["JDBC_USER"]!!
    val databasePassword = dotenv["JDBC_PASSWORD"]!!
    val stockCacheEvictionInMinutes = dotenv["STOCK_CACHE_EVICTION_MINUTES"]!!.toLong()
    val notificationScheduleMinutes = dotenv["NOTIFICATION_SCHEDULE_MINUTES"]!!.toLong()
    val locale: Locale = Locale.Builder().setLanguage("pt").setRegion("br").build()

}