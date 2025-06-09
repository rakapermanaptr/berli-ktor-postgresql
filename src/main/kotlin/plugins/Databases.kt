package com.creospace.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import java.net.URI

fun Application.configureDatabases() {
    val rawUrl = System.getenv("DATABASE_URL") ?: error("Missing DATABASE_URL")
    val uri = URI(rawUrl)

    val dbUrl = "jdbc:postgresql://${uri.host}:${uri.port}${uri.path}"
    val dbUser = uri.userInfo.split(":")[0]
    val dbPassword = uri.userInfo.split(":")[1]

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
