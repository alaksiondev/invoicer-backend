package services.impl.intermediary

import io.github.alaksion.invoicer.utils.validation.IbanValidator
import io.github.alaksion.invoicer.utils.validation.SwiftValidator
import models.intermediary.CreateIntermediaryModel
import repository.IntermediaryRepository
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import services.api.services.intermediary.CreateIntermediaryService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.badRequestError
import utils.exceptions.http.httpError
import java.util.*

internal class CreateIntermediaryServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val repository: IntermediaryRepository,
    private val checkIntermediarySwiftAlreadyUsedService: CheckIntermediarySwiftAvailableService,
    private val swiftValidator: SwiftValidator,
    private val ibanValidator: IbanValidator
) : CreateIntermediaryService {

    override suspend fun create(model: CreateIntermediaryModel, userId: UUID): String {
        if (swiftValidator.validate(model.swift).not()) {
            badRequestError("Invalid swift code: ${model.swift}")
        }

        if (ibanValidator.validate(model.iban).not()) {
            badRequestError("Invalid iban code: ${model.iban}")
        }

        validateString(
            value = model.name,
            fieldName = "Name"
        )
        validateString(
            value = model.bankName,
            fieldName = "Bank name"
        )
        validateString(
            value = model.bankAddress,
            fieldName = "Bank address"
        )

        val user = getUserByIdService.get(userId)

        if (checkIntermediarySwiftAlreadyUsedService.execute(model.swift, userId)) {
            httpError(
                message = "Swift code: ${model.swift} is already in use by another intermediary",
                code = HttpCode.Conflict
            )
        }

        return repository.create(
            userId = user.id,
            model = model
        )
    }

    private fun validateString(
        value: String,
        fieldName: String
    ) {
        if (value.trim().isBlank()) {
            badRequestError("$fieldName cannot be blank")
        }
    }

}