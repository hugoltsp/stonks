package io.github.hugoltsp.stonks.infra.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.hugoltsp.stonks.infra.domain.Settings
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Database.Companion.connect
import java.net.URI

object DatabaseConfig {

    private lateinit var connection: Database

    fun connect(): Database {

        if (!::connection.isInitialized) {

            connection = connect(
                HikariDataSource(hikariConfig())
            )

        }

        return connection
    }

    private fun hikariConfig() = System.getenv("DATABASE_URL")?.let {
        val dbUri = URI(it)
        val usernameDb = dbUri.userInfo.split(":")[0]
        val passwordDb = dbUri.userInfo.split(":")[1]
        HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://${dbUri.host}:${dbUri.port}${dbUri.path}"
            username = usernameDb
            password = passwordDb
            connectionTestQuery = "SELECT 1"
        }
    } ?: HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = Settings.jdbcUrl
        username = Settings.databaseUser
        password = Settings.databasePassword
        connectionTestQuery = "SELECT 1"
    }

}