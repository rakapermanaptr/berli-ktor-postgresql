package com.creospace.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabases() {
    val dbUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/berli_database"
    val dbUser = System.getenv("DB_USER") ?: "admin"
    val dbPassword = System.getenv("DB_PASSWORD") ?: "secret"

    val config = HikariConfig().apply {
        jdbcUrl = dbUrl
        driverClassName = "org.postgresql.Driver"
        username = dbUser
        password = dbPassword
        isReadOnly = false
        maximumPoolSize = 8
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}
