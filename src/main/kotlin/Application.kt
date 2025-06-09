package com.creospace

import com.creospace.models.repository.BerliRepositoryImpl
import com.creospace.plugins.configureDatabases
import com.creospace.plugins.configureRouting
import com.creospace.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toInt() ?: 8080,
        host = "0.0.0.0"
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    val repository = BerliRepositoryImpl()

    configureDatabases()
    configureRouting()
    configureSerialization(repository)
}
