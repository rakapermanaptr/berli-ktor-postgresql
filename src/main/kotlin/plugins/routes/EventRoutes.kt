package plugins.routes

import com.creospace.models.repository.BerliRepository
import com.creospace.utils.errorResponse
import com.creospace.utils.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.eventRoutes(repository: BerliRepository) {
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