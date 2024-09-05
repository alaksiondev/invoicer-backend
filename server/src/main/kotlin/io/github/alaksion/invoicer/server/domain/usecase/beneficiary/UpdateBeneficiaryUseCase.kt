package io.github.alaksion.invoicer.server.domain.usecase.beneficiary

import foundation.validator.api.SwiftValidator
import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.beneficiary.UpdateBeneficiaryModel
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.ktor.http.HttpStatusCode
import utils.exceptions.httpError
import java.util.UUID

interface UpdateBeneficiaryUseCase {
    suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel
}

internal class UpdateBeneficiaryUseCaseImpl(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdUseCase,
    private val checkBeneficiarySwiftAvailableUseCase: CheckBeneficiarySwiftAvailableUseCase,
    private val beneficiaryRepository: BeneficiaryRepository,
    private val swiftValidator: SwiftValidator
) : UpdateBeneficiaryUseCase {

    override suspend fun execute(
        model: UpdateBeneficiaryModel,
        userId: String,
        beneficiaryId: String
    ): BeneficiaryModel {
        if (swiftValidator.validate(model.swift).not()) {
            httpError("Invalid swift code: ${model.swift}", HttpStatusCode.BadRequest)
        }

        getUserByIdUseCase.get(userId)

        getBeneficiaryByIdUseCase.get(
            beneficiaryId = beneficiaryId,
            userId = userId
        )

        if (checkBeneficiarySwiftAvailableUseCase.execute(
                swift = model.swift,
                userId = userId
            )
        ) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another beneficiary",
                code = HttpStatusCode.Conflict
            )
        }

        return beneficiaryRepository.update(
            userId = UUID.fromString(userId),
            beneficiaryId = UUID.fromString(beneficiaryId),
            model = model
        )
    }

}