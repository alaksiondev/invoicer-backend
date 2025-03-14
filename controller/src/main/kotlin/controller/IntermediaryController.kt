package controller

import controller.viewmodel.intermediary.*
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.intermediary.*

internal fun Routing.intermediaryController() {
    route("/v1/intermediary") {
        jwtProtected {
            post {
                val body = call.receive<CreateIntermediaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateIntermediaryService>()

                call.respond(
                    message =
                        CreateIntermediaryResponseViewModel(
                            id = useCase.create(
                                model = model,
                                userId = jwtUserId()
                            )
                        ),
                    status = HttpStatusCode.Created
                )
            }
        }
        jwtProtected {
            delete("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<DeleteIntermediaryService>()

                useCase.execute(
                    beneficiaryId = intermediaryId,
                    userId = jwtUserId()
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }

        jwtProtected {
            get {
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val useCase by closestDI().instance<GetUserIntermediariesService>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = UserIntermediariesViewModel(
                        intermediaries = useCase.execute(
                            userId = jwtUserId(),
                            page = page,
                            limit = limit
                        ).map { it.toViewModel() }
                    )
                )
            }
        }

        jwtProtected {
            put("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<UpdateIntermediaryService>()
                val userId = jwtUserId()
                val body = call.receive<UpdateIntermediaryViewModel>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.execute(
                        intermediaryId = intermediaryId,
                        model = body.toModel(),
                        userId = userId
                    ).toViewModel()
                )
            }
        }

        jwtProtected {
            get("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<GetIntermediaryDetailsService>()
                val userId = jwtUserId()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.getIntermediaryDetails(
                        userId = userId,
                        intermediaryId = intermediaryId
                    ).toViewModel()
                )
            }
        }
    }
}