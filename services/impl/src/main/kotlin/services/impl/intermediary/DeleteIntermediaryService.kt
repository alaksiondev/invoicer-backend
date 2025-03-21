package services.impl.intermediary

import io.github.alaksion.invoicer.utils.http.HttpCode
import repository.api.repository.IntermediaryRepository
import repository.api.repository.InvoiceRepository
import services.api.services.intermediary.DeleteIntermediaryService
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.httpError
import java.util.*

internal class DeleteIntermediaryServiceImpl(
    private val getIntermediaryByIdService: GetIntermediaryByIdService,
    private val intermediaryRepo: IntermediaryRepository,
    private val getUserByIdUseCase: GetUserByIdService,
    private val invoiceRepository: InvoiceRepository
) : DeleteIntermediaryService {

    override suspend fun execute(beneficiaryId: String, userId: String) {
        getUserByIdUseCase.get(userId)

        getIntermediaryByIdService.get(
            intermediaryId = beneficiaryId,
            userId = userId
        )

        if (invoiceRepository.getInvoicesByIntermediaryId(
                intermediaryId = UUID.fromString(beneficiaryId),
                userId = UUID.fromString(userId)
            ).isNotEmpty()
        ) {
            httpError(message = "Cannot delete intermediary with invoices associated", code = HttpCode.Conflict)
        }

        intermediaryRepo.delete(
            intermediaryId = UUID.fromString(beneficiaryId),
            userId = UUID.fromString(userId)
        )
    }
}