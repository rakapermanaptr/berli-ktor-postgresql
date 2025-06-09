package com.creospace.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/berli_database"
        driverClassName = "org.postgresql.Driver"
        username = "admin"
        password = "secret"
        isReadOnly = false
        maximumPoolSize = 8
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}
