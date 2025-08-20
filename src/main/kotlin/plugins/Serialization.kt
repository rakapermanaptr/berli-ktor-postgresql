package com.creospace.plugins

import com.creospace.models.domain.Event
import com.creospace.models.repository.BerliRepository
import com.creospace.models.domain.Report
import com.creospace.utils.errorResponse
import com.creospace.utils.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: BerliRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/api") {
            get("/reports") {
                val reports = repository.getAllReport()
                call.respond(successResponse(reports, "Reports retrieved"))
            }

            route("/report") {
                post {
                    val report = call.receive<Report>()
                    repository.postReport(report)
                    call.respond(successResponse(null, "Report created"))
                }
                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid or missing ID"))
                        return@get
                    }

                    val report = repository.getReport(id)
                    if (report == null) {
                        call.respond(HttpStatusCode.NotFound, errorResponse("Report not found"))
                        return@get
                    }

                    call.respond(successResponse(report, "Report retrieved"))
                }
            }

            get("/events") {
                val events = repository.getAllEvent()
                call.respond(successResponse(events, "Events retrieved"))
            }

            get("/event/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid or missing ID"))
                    return@get
                }

                val event = repository.getEvent(id)
                if (event == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Event not found"))
                    return@get
                }

                call.respond(successResponse(event, "Event retrieved"))
            }
        }
    }
}
