package plugins.routes

import com.creospace.models.domain.Account
import com.creospace.models.repository.BerliRepository
import com.creospace.models.response.LoginResponse
import com.creospace.utils.errorResponse
import com.creospace.utils.successResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import models.request.LoginRequest

fun Route.authRoutes(repository: BerliRepository) {

    route("/auth") {
        route("/register") {
            post {
                val account = call.receive<Account>()

                // check existing email
                val existingAccount = repository.findByEmail(account.email)
                if (existingAccount != null) {
                    call.respond(
                        HttpStatusCode.Conflict,
                        errorResponse("Email sudah terdaftar")
                    )
                    return@post
                }

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