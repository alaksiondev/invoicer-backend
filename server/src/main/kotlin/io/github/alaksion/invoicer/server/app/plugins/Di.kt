package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.domain.usecase.*
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoicePdfUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoiceUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.GetInvoiceByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.GetInvoiceByIdUseCaseImpl
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.filehandler.TempFileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.OpenPdfGenerator
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.github.alaksion.invoicer.server.repository.*
import io.github.alaksion.invoicer.server.util.DateProvider
import io.github.alaksion.invoicer.server.util.DateProviderImplementation
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Application.installDi() {
    di {

        bindProvider<InvoiceRepository> { InvoiceRepositoryImpl(dateProvider = instance()) }
        bindProvider<InvoiceActivityRepository> { InvoiceActivityRepositoryImpl(dateProvider = instance()) }
        bindProvider<InvoicePdfRepository> { InvoicePdfRepositoryImpl() }

        bindProvider<CreateInvoiceUseCase> {
            CreateInvoiceUseCaseImpl(
                invoiceRepository = instance(),
                invoiceActivityRepository = instance()
            )
        }
        bindProvider<CreateInvoicePdfUseCase> {
            CreateInvoicePdfUseCaseImpl(
                invoiceRepository = instance(),
                invoicePdfRepository = instance(),
                pdfGenerator = instance(DITags.OPEN_PDF_GENERATOR),
            )
        }
        bindProvider<GetInvoicesUseCase> {
            GetInvoicesUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                dispatcher = Dispatchers.IO,
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindProvider<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }

        bindProvider<DateProvider> { DateProviderImplementation }

        bindProvider<GetInvoiceByIdUseCase> {
            GetInvoiceByIdUseCaseImpl(repository = instance())
        }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}