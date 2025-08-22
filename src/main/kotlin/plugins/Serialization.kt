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
import java.util.Base64

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
//                    val report = call.receive<Report>()
//                    repository.postReport(report)
//                    call.respond(successResponse(null, "Report created"))
                    val req = try {
                        call.receive<ReportRequest>()
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid request: ${e.message}"))
                        return@post
                    }

                    val finalImageUrl = try {
                        val bytes = Base64.getDecoder().decode(req.image.substringAfter("base64,"))
                        val ext = when (SupabaseStorageClient.detectImageContentType(bytes)) {
                            "image/png" -> "png"
                            "image/gif" -> "gif"
                            else -> "jpg"
                        }
                        val objectPath = "berli/${req.userId}/${System.currentTimeMillis()}.$ext"
                        SupabaseStorageClient.uploadBase64(objectPath, req.image)
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, errorResponse("Upload failed: ${e.message}"))
                        return@post
                    }

                    val report = ReportRequest(
                        id = null,
                        username = req.username,
                        userId = req.userId,
                        complaint = req.complaint,
                        complaintDetails = req.complaintDetails,
                        image = finalImageUrl,
                        location = req.location,
                        locationDetails = req.locationDetails,
                        status = req.status,
                        likeNumber = req.likeNumber,
                        dateCreated = req.dateCreated
                    )

                    try {
                        repository.postReport(report)
                        call.respond(HttpStatusCode.OK, successResponse(null, "Report created"))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, errorResponse("DB Failed: ${e.message}"))
                    }
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

            route("/auth") {
                route("/register") {
                    post {
                        val account = call.receive<Account>()
                        repository.postRegisterAccount(account)
                        call.respond(successResponse(null, "Account created"))
                    }
                }

                route("/login") {
                   post {
                       val req = try {
                           call.receive<LoginRequest>()
                       } catch (e: Exception) {
                           call.respond(HttpStatusCode.BadRequest, errorResponse("Invalid request body"))
                           return@post
                       }

                       val account = try {
                           repository.getAccountLogin(req.username, req.password)
                       } catch (e: Exception) {
                           call.application.environment.log.error("Login error", e)
                           call.respond(HttpStatusCode.InternalServerError, errorResponse("Internal server error"))
                           return@post
                       }

                       if (account == null) {
                           call.respond(HttpStatusCode.Unauthorized, errorResponse("Invalid username or password"))
                           return@post
                       }

                       val loginResponse = LoginResponse(
                           username = account.username,
                           phoneNumber = account.phoneNumber,
                           fullName = account.fullName
                       )
                       call.respond(HttpStatusCode.OK, successResponse(loginResponse))
                   }
                }
            }
        }
    }
}
