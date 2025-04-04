package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.SecureFileLinkGenerator
import models.invoicepdf.InvoicePdfStatus
import repository.api.repository.InvoicePdfRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.pdf.InvoicePdfSecureLinkService
import utils.exceptions.badRequestError
import utils.exceptions.notFoundError
import utils.exceptions.serverException
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

internal class InvoicePdfSecureLinkServiceImpl(
    private val secureFileLinkGenerator: SecureFileLinkGenerator,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val invoicePdfRepository: InvoicePdfRepository
) : InvoicePdfSecureLinkService {

    override suspend fun generate(invoiceId: String, userId: String): String {
        getUserInvoiceByIdService.get(
            id = invoiceId,
            userId = userId
        )

        val invoicePdf = invoicePdfRepository.getInvoicePdf(invoiceId) ?: notFoundError("Invoice PDF not found")
        if (invoicePdf.status != InvoicePdfStatus.Success) notFoundError("Invoice PDF not found")

        return runCatching {
            secureFileLinkGenerator.generateLink(
                fileKey = invoicePdf.path,
                durationInHours = 7
            )
        }.fold(
            onSuccess = { response ->
                response
            },
            onFailure = {
                badRequestError(it.message.orEmpty())
            }
        )
    }
}