package plugins.routes

import com.creospace.models.domain.Report
import com.creospace.models.repository.BerliRepository
import com.creospace.models.utils.SupabaseStorageClient
import com.creospace.utils.errorResponse
import com.creospace.utils.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.util.Base64

fun Route.reportRoutes(repository: BerliRepository) {
    get("/reports") {
        val reports = repository.getAllReport()
        call.respond(successResponse(reports, "Reports retrieved"))
    }

    route("/report") {
        post {
            val req = try {
                call.receive<Report>()
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

            val report = Report(
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
}