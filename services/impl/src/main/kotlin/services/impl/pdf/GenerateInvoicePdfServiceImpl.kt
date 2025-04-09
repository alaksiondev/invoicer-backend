package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.local.LocalStorage
import io.github.alaksion.invoicer.foundation.storage.remote.FileUploader
import models.invoicepdf.InvoicePdfStatus
import repository.api.repository.InvoicePdfRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.user.GetUserByIdService
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import java.util.*

internal class GenerateInvoicePdfServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val writer: InvoicePdfWriter,
    private val fileUploader: FileUploader,
    private val invoicePdfRepository: InvoicePdfRepository,
    private val localStorage: LocalStorage
) : GenerateInvoicePdfService {

    override suspend fun generate(invoiceId: UUID, userId: UUID) {
        getUserByIdService.get(userId.toString())

        val invoice = getUserInvoiceByIdService.get(
            userId = userId,
            invoiceId = invoiceId
        )

        localStorage.createDirectory("/temp/pdfs")

        invoicePdfRepository.createInvoicePdf(invoiceId.toString())

        val outputPath = localStorage.getRootPath() + "/temp/pdfs" + "/invoice-${invoice.id}.pdf"

        writer.write(
            invoice = invoice,
            outputPath = outputPath
        )

        runCatching {
            fileUploader.uploadFile(
                localFilePath = outputPath,
                fileName = "user/$userId/$invoiceId.pdf"
            )
        }.fold(
            onFailure = {
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId.toString(),
                    status = InvoicePdfStatus.Failed,
                    filePath = ""
                )
                throw it
            },
            onSuccess = { fileKey ->
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId.toString(),
                    status = InvoicePdfStatus.Success,
                    filePath = fileKey
                )
            }
        )

        localStorage.deleteFile(outputPath)
    }
}