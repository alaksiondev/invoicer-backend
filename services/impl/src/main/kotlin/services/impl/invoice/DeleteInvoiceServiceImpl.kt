package services.impl.invoice

import repository.api.repository.InvoiceRepository
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.unauthorizedResourceError
import java.util.*

internal class DeleteInvoiceServiceImpl(
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val getUserByIdUseCase: GetUserByIdService,
    private val repository: InvoiceRepository
) : DeleteInvoiceService {

    override suspend fun delete(invoiceId: String, userId: String) {
        val user = getUserByIdUseCase.get(userId)
        val invoice = getUserInvoiceByIdService.get(
            id = invoiceId,
            userId = userId
        )

        if (user.id != invoice.user.id) {
            unauthorizedResourceError()
        }

        repository.delete(UUID.fromString(invoiceId))
    }

}