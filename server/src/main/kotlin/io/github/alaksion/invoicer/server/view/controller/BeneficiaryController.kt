package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CreateBeneficiaryUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.CreateBeneficiaryViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.toModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

fun Routing.beneficiaryController() {
    route("beneficiary") {
        jwtProtected {
            post {
                val body = call.receive<CreateBeneficiaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateBeneficiaryUseCase>()

                call.respond(
                    message =
                    CreateBeneficiaryResponseViewModel(
                        id = useCase.create(
                            model = model,
                            userId = jwtUserId()
                        )
                    ),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}