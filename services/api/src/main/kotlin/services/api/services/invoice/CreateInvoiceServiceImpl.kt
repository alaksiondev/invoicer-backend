package services.api.services.invoice

import kotlinx.datetime.LocalDate
import services.api.model.createinvoice.CreateInvoiceActivityModel
import services.api.model.createinvoice.CreateInvoiceModel
import services.api.model.createinvoice.CreateInvoiceResponseModel
import services.api.repository.InvoiceRepository
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.user.GetUserByIdService
import utils.date.api.DateProvider
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import utils.exceptions.httpError
import java.util.UUID

internal interface CreateInvoiceService {
    suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: String
    ): CreateInvoiceResponseModel
}

internal class CreateInvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val dateProvider: DateProvider,
    private val getUserByIdUseCase: GetUserByIdService,
    private val getBeneficiaryByIdUseCase: GetBeneficiaryByIdService,
    private val getIntermediaryByIdUseCase: GetIntermediaryByIdService
) : CreateInvoiceService {

    override suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: String,
    ): CreateInvoiceResponseModel {
        validateActivities(model.activities)

        validateDateRange(
            issueDate = model.issueDate,
            dueDate = model.dueDate
        )

        getBeneficiaryByIdUseCase.get(
            beneficiaryId = model.beneficiaryId,
            userId = userId
        )

        if (model.intermediaryId != null) {
            getIntermediaryByIdUseCase.get(
                intermediaryId = model.intermediaryId,
                userId = userId
            )
        }

        getUserByIdUseCase.get(userId)

        if (invoiceRepository.getInvoiceByExternalId(externalId = model.externalId) != null) {
            throw HttpError(
                message = "Invoice with externalId: ${model.externalId} already exists",
                statusCode = HttpCode.Conflict
            )
        }

        val response =
            invoiceRepository.createInvoice(data = model, userId = UUID.fromString(userId))

        return CreateInvoiceResponseModel(
            externalInvoiceId = model.externalId,
            invoiceId = response
        )
    }

    private fun validateDateRange(
        issueDate: LocalDate,
        dueDate: LocalDate
    ) {
        if (dateProvider.now() > issueDate) {
            httpError(
                message = "Issue date cannot be past date",
                code = HttpCode.BadRequest
            )
        }

        if (dateProvider.now() > dueDate) {
            httpError(
                message = "Due date cannot be past date",
                code = HttpCode.BadRequest
            )
        }

        if (issueDate > dueDate) {
            throw HttpError(
                message = "Issue date cannot be after due date",
                statusCode = HttpCode.BadRequest
            )
        }
    }

    private fun validateActivities(
        services: List<CreateInvoiceActivityModel>
    ) {
        if (services.isEmpty()) throw HttpError(
            message = "Invoice must have at least one service",
            statusCode = HttpCode.BadRequest
        )

        services.forEach {
            if (it.quantity <= 0) {
                throw HttpError(
                    message = "Invoice activity must have quantity > 0",
                    statusCode = HttpCode.BadRequest
                )
            }

            if (it.unitPrice <= 0) {
                throw HttpError(
                    message = "Invoice activity must have unitPrice > 0",
                    statusCode = HttpCode.BadRequest
                )
            }
        }
    }

}