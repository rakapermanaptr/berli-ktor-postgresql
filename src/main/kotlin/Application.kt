package com.creospace

import com.creospace.models.repository.BerliRepositoryImpl
import com.creospace.plugins.configureDatabases
import com.creospace.plugins.configureRouting
import com.creospace.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = BerliRepositoryImpl()

    configureDatabases()
    configureRouting()
    configureSerialization(repository)
}
