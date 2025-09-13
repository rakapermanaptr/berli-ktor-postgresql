package com.creospace.plugins

import com.creospace.models.domain.Account
import com.creospace.models.repository.BerliRepository
import com.creospace.models.domain.Report
import com.creospace.models.request.ReportRequest
import com.creospace.models.response.LoginResponse
import com.creospace.models.utils.SupabaseStorageClient
import com.creospace.utils.errorResponse
import com.creospace.utils.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.request.LoginRequest
import plugins.routes.authRoutes
import plugins.routes.eventRoutes
import plugins.routes.reportRoutes
import java.util.Base64

fun Application.configureSerialization(repository: BerliRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/api") {
            reportRoutes(repository)
            eventRoutes(repository)
            authRoutes(repository)
        }
    }
}
