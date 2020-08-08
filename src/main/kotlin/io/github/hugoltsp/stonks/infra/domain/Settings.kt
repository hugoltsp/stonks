package io.github.hugoltsp.stonks.infra.domain

import io.github.cdimascio.dotenv.dotenv
import java.util.*

object Settings {

    val telegramToken = dotenv()["TELEGRAM_TOKEN"]!!
    val jdbcUrl = dotenv()["DATABASE_URL"]!!
    val databaseUser = dotenv()["DATABASE_USER"]!!
    val databasePassword = dotenv()["DATABASE_PASSWORD"]!!
    val stockCacheEvictionInMinutes = dotenv()["STOCK_CACHE_EVICTION_MINUTES"]!!.toLong()
    val notificationScheduleMinutes = dotenv()["NOTIFICATION_SCHEDULE_MINUTES"]!!.toLong()
    val locale: Locale = Locale.Builder().setLanguage("pt").setRegion("br").build()

    init {
        dotenv {
            ignoreIfMissing = true
        }
    }

}