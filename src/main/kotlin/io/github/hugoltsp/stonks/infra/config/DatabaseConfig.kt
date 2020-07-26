package io.github.hugoltsp.stonks.infra.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.hugoltsp.stonks.infra.domain.Settings
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Database.Companion.connect

object DatabaseConfig {

    private lateinit var connection: Database

    fun connect(): Database {

        if (!::connection.isInitialized) {

            connection = connect(
                HikariDataSource(
                    HikariConfig().apply {
                        driverClassName = "org.mariadb.jdbc.Driver"
                        jdbcUrl = Settings.jdbcUrl
                        username = Settings.databaseUser
                        password = Settings.databasePassword
                        connectionTestQuery = "SELECT 1"
                    }
                )
            )

        }

        return connection
    }

}