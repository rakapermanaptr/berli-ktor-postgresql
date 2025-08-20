package com.creospace

import com.creospace.models.repository.BerliRepositoryImpl
import com.creospace.plugins.configureDatabases
import com.creospace.plugins.configureRouting
import com.creospace.plugins.configureSerialization
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.options
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.slf4j.event.Level

fun main(args: Array<String>) {
    embeddedServer(
        Netty,
        port = System.getenv("PORT")?.toInt() ?: 8080,
        host = "0.0.0.0"
    ) {
        debugModule()
        module()
    }.start(wait = true)
}

fun Application.module() {
    val repository = BerliRepositoryImpl()

    configureDatabases()
    configureRouting()
    configureSerialization(repository)
}

fun Application.debugModule() {
    // Log setiap request (method + uri)
    install(CallLogging) {
        level = Level.INFO
        format { call -> "${call.request.httpMethod.value} ${call.request.uri}" }
    }

    // CORS untuk mencegah masalah preflight selama testing
    install(CORS) {
        anyHost() // hanya untuk debug/testing — jangan pakai di production tanpa restrict
        allowNonSimpleContentTypes = true
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Options)
    }

    routing {
        // simple health
        get("/") {
            call.respondText("OK", ContentType.Text.Plain)
        }

        // endpoint untuk melihat semua method di /api/debug
        route("/api/debug") {
            get { call.respondText("GET ok") }
            post {
                val body = call.receiveText()
                call.application.environment.log.info("POST body: $body")
                call.respond(mapOf("method" to "POST", "body" to body))
            }
            options {
                call.respond(HttpStatusCode.OK)
            }
            // wildcard: log method + uri
            handle {
                val m = call.request.httpMethod.value
                val u = call.request.uri
                call.application.environment.log.info("WILDCARD HIT -> $m $u")
                call.respondText("wildcard $m $u")
            }
        }

        // kamu bisa tetap mount configureSerialization(repository) setelah debug ini
    }
}
