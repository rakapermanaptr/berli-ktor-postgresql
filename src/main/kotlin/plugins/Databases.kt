package com.creospace.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import java.net.URI

fun Application.configureDatabases() {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc://postgres:HNCVIbsTwTeKNuLrdudgFGjpPVWhVXJI@postgres.railway.internal:5432/railway"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "HNCVIbsTwTeKNuLrdudgFGjpPVWhVXJI"
        isReadOnly = false
        maximumPoolSize = 8
        transactionIsolation = "TRANSACTION_SERIALIZABLE"
    }

    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
}
